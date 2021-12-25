/*
 * The MIT License
 *
 * Copyright 2021 PI_stanowisko_1.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package berkeleysocketstcp;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;


/**
 * Prosty program ilustrujący jak nawiązać łączność jednocześnie tworząc serwer
 * TCP (wielowątkowo zapewniający łączność z wieloma klientami) i jednocześnie
 * tworząc klienta TCP (nawiązującego łączność ze wskazanym serwerem TCP).
 *
 * @author Sławomir Marczyński
 * @version 0.3
 */
public class Program {

    public static void main(String[] args) {

        // Pozyskanie danych nt. parametrów połączenia. Następnie próba otwarcia
        // nowego serwera TCP (jeżeli nie był otwarty, tj. jeżeli port nie był
        // zajęty). Niezależnie od ustanowienia serwera otwarcie łącza TCP jako
        // klient: być może połączenie będzie z innym komputerem, albo z inną
        // instancją programu, albo z tą samą instancją.

        Configuration config = Configuration.create();
        if (config != null) {

            // Udało się pomyślnie uzyskać dane nt. konfiguracji. Kolejnym
            // etapami są próba otwarcia servera i próba otwarcia klienta.
            //
            // Aby zwolnić zasoby (takie jak port) wzorzec RAII wymaga wywołania
            // metod close() dla serwera i dla klienta. Najłatwiej zagwarantować
            // to przez użycie try-with-resources. Ale nie jest prosto mieszać
            // try-with-resources z if-else, więc zamiast tego użyte jest
            // finally. Warto zauważyć, że metody create() klas Server i Client
            // nie rzucają wyjątków (nie powinny).

            Server server = null;
            Client client = null;
            try {
                if (config.runServer) {
                    server = Server.create(config.portNumber);
                    if (server != null) {
                        server.setReply(config.replyString);
                    }
                }
                if (config.runClient) {
                    client = Client.create(config.hostName, config.portNumber);
                    if (client != null) {
                        client.setSendString(config.sendString);
                    }
                }

                String string = (server != null ? "Server is running. " : "")
                              + (client != null ? "Client is running. " : "")
                              + "Stop program?";

                // Dodajemy otwarcie dialogu modalnego do kolejki wątku EDT.
                // Dialog, gdy zostanie aktywowany, zablokuje wątek EDT (jest
                // modalny) . Zablokowany wątek EDT zatrzyma wykonanie obecnego
                // wątku (nie-EDT). Zatrzymanie wykonania w dwóch wątkach (EDT i
                // głównym) nie zablokuje jednak servera, ani klienta - te mają
                // zupełnie własne niezależne i niezależne wątki.

                EventQueue.invokeAndWait(() -> {
                    JOptionPane.showMessageDialog(null, string);
                });
            } catch (InterruptedException | InvocationTargetException ex) {

                // Jeżeli tu jesteśmy, to coś poszło nie tak z invokeAndWait().
                // Zasadniczo nigdy nie powinniśmy się tu znaleźć. Teoretycznie.

                Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                /*
                 * Cokolwiek się zdarzy musimy zamknąć to co musimy zamknąć.
                 */
                if (server != null) {
                    server.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        }
    }
}


/**
 * Klasa Debug jest aby łatwiej wychwycić co w programie się dzieje.
 *
 * @author Sławomir Marczyński
 * @version 0.1
 */
class Debug {

    /**
     * Aby włączyć debugowanie musi być debug = true; Aby wyłączyć debugowanie
     * należy dać debug = false;
     */
    public static final boolean DEBUG = true;

    /**
     * Łańcuch tekstu wysłany do output jest po prostu wyświetlany na konsoli,
     * ale tylko wtedy, gdy debug = true. Gdy (wcześniej w klasie) ustawimy
     * debug = false to nic się nie wyświetli. W ten sposób łatwo pozbyć z
     * całego programu wszystkich komunikatów diagnostycznych.
     *
     * @param string łańcuch znaków jaki ma być wypisany na ekran.
     */
    public static void output(String string) {
        if (DEBUG) {
            System.out.println(string);
        }
    }

}


/**
 * Obiekt klasy Configuration, jeżeli zostaną skonstruowane, przechowują nazwę
 * hosta i numer portu pozyskane za pomocą ConfigurationGUI, .
 *
 * Obiekt klasy ConfigurationGUI jest tworzony ad hoc i ma dostęp, do pól
 * nieprywatnych obiektu Configuration. ConfigurationGUI jest klasą pochodną od
 * JDialog i dlatego wymaga zapisania operacji tworzenia obiektów tej klasy w
 * wątku EDT, czyli stosowania EventQueue.invokeLater. Konstruktor klasy
 * Configuration zawiesza swoje działanie aż EDT zezwoli na dalsze działanie (co
 * następuje po zaakceptowaniu ustawień naciśnięciem guzika "Ok" w dialogu).
 *
 * @author Sławomir Marczyński
 */
class Configuration {

    // Można ew. zamiast hostName i portNumber od razu użyć InetSocketAddress,
    // ew. podobnej gotowej klasy. Ale czy byłoby to lepsze niż to co jest?

    String hostName;
    int portNumber = 4444;

    boolean runServer = true;
    boolean runClient = true;
    String replyString = "[ECHO]";
    String sendString = "<message from client>";

    /*
     * Zablokowany konstruktor, tworzenie obiektów będzie możliwe tylko poprzez
     * fabrykę obiektów, tj. metodę statyczną create.
     */
    private Configuration() throws UnknownHostException {
        this.hostName = InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * Fabryka obiektów Configuration
     *
     * @return Obiekt klasy Configuration, albo null jeżeli nie udało się
     *         takiego obiektu utworzyć.
     */
    static Configuration create() {
        try {
            final Configuration that = new Configuration();
            EventQueue.invokeAndWait(() -> {
                new ConfigurationGUI(that).setVisible(true);
            });
            return that;
        } catch (Exception ex) {

            // Nie sprawdzamy szczegółowo - c przeszkodziło utworzyć obiekt
            // konfiguracja i tak zmusza nas do zwrócenia null, bo obiektu nie
            // udało się utworzyć.

            return null;
        }
    }

}


/**
 * ConfigurationGUI jest klasą dialogu modalnego.
 *
 * Tworzy graficzny interfejs użytkownika; przekształca podane przez użytkownika
 * dane na nazwę hosta i numer portu wpisywane bezpośrednio do obiektu klasy
 * Configuration. Ponieważ jest to dialog modalny, to setVisble(true) jest
 * operacją blokującą (prawidłowe użycie powinno uruchamiać dialog w wątku EDT).
 * Czyli dialog ten powinien być uruchamiany przez EventQueue.invokeAndWait().
 *
 * @author Sławomir Marczyński
 */
class ConfigurationGUI extends JDialog {

    private final Configuration owner;

    private JCheckBox runClient;
    private JCheckBox runServer;
    private JTextField hostName;
    private JTextField portNumber;
    private JTextField replyString;
    private JTextField sendString;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JButton okButton;
    
    ConfigurationGUI(Configuration owner) {
        super();
        this.owner = owner;
        initComponents();
        portNumber.setText("" + owner.portNumber);
        hostName.setText(owner.hostName);
        runServer.setSelected(owner.runServer);
        runClient.setSelected(owner.runClient);
        replyString.setText(owner.replyString);
        sendString.setText(owner.sendString);
    }

    private void initComponents() {

        runServer = new JCheckBox();
        runClient = new JCheckBox();
        hostName = new JTextField();
        portNumber = new JTextField();
        replyString = new JTextField();
        sendString = new JTextField();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        okButton = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Client/Server");
        setModal(true);

        runServer.setSelected(true);
        runServer.setText("run server");
        runClient.setSelected(true);
        runClient.setText("run client");
        hostName.setText("127.0.0.1");
        portNumber.setText("4444");
        okButton.setText("Ok");
        replyString.setText("[ECHO]");
        sendString.setText("<message from client>");
        jLabel1.setText("Host");
        jLabel2.setText("Port");
        jLabel3.setText("reply with");
        jLabel4.setText("send string");
        okButton.addActionListener(this::okButtonActionPerformed);

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">        
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(runClient)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
                                                .addGap(6, 6, 6)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(portNumber)
                                                        .addComponent(hostName, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
                                        .addComponent(okButton, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(runServer)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(sendString, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(replyString, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(hostName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(portNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(runServer)
                                        .addComponent(jLabel3)
                                        .addComponent(replyString, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(runClient)
                                        .addComponent(jLabel4)
                                        .addComponent(sendString, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(okButton)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        // </editor-fold>

        pack();
    }
    
    private void okButtonActionPerformed(ActionEvent evt) {

        // TODO Nie jest sprawdzana poprawność danych wpisanych przez
        // użytkownika i dlatego nie da się zapobiec np. próbie przetłumaczenia
        // napisu "AZ?" na numer portu... itp. itd. Jedną z możliwosci byłoby
        // sprawdzanie poprawności w tym miejscu (tj. na ActionPreformed). Inną
        // sprawdzanie w czasie pisania, na bieżąco.

        owner.portNumber = Integer.parseInt(portNumber.getText());
        owner.hostName = hostName.getText();
        owner.runServer = runServer.isSelected();
        owner.runClient = runClient.isSelected();
        owner.replyString = replyString.getText();
        owner.sendString = sendString.getText();
        dispose();
    }
}


/**
 * Klasa obiektów będących klientami łączącymi się z serwerem. Każdy taki obiekt
 * ma własny wątek, pozwalający na asynchroniczną transmisję danych.
 *
 * @author Sławomir Marczyński
 */
class Client implements Runnable, AutoCloseable {

    private final Socket clientSocket;
    private final Thread clientThread;
    private String sendString;

    private Client(String hostName, int portNumber) throws IOException {
        clientSocket = new Socket(hostName, portNumber);
        //clientSocket = new Socket();
        //clientSocket.connect(new InetSocketAddress(hostName, portNumber), 10000);
        clientThread = new Thread(this);
    }

    public static Client create(String hostName, int portNumber) {
        try {
            Client that = new Client(hostName, portNumber);
            that.clientSocket.setSoTimeout(1000);
            that.clientThread.start();
            Debug.output("Client created");
            return that;
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public void run() {
        try (
                BufferedReader input
                = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output
                = new PrintWriter(clientSocket.getOutputStream(), true);) {

            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(137);
                try {
                    output.println(sendString);
                    String line = input.readLine();
                    if (line != null) {
                        Debug.output("Client received: " + line);
                    } else {
                        Debug.output("Client detected break connection");
                        break;
                    }
                } catch (SocketTimeoutException ex) {
                    // Nie ma komunikatu? Nie odpowiadamy!
                }
            }

        } catch (IOException | InterruptedException ex) {
        }
    }

    @Override
    public void close() {
        clientThread.interrupt();
        try {
            clientSocket.close();
        } catch (IOException ex) {
        }
    }

    void setSendString(String sendString) {
        this.sendString = sendString;
    }
}


/**
 * Klasa Server tworzy wątek, w którym tworzone są nowe Connections. Jest to
 * oddzielny wątek, zajmuje się on tylko i wyłącznie tworzeniem Connections
 * (które nota bene każde tworzy swój własny wątek). Klasa Server nie
 * czyta/pisze z TCP, to jest zarezerwowane dla Connections.
 *
 * @author Sławomir Marczyński
 * @version 0.2
 */
class Server implements Runnable, AutoCloseable {

    private final ServerSocket serverSocket;
    private final Thread serverThread;
    /*
     * Lista ArrayList zawiera wszystkie nawiązane połączenia. Jest pomocna przy
     * zamykaniu serwera - zamknięcie serwera pociaga za sobą zamknięcie, jedno
     * po drugim, wszystkich connections. Co pociągać powinno zamknięcie gniazd.
     */
    private final ArrayList<Connection> connections = new ArrayList<>();
    private String replyString;

    /*
     * Prywatny konstrukor blokuje możliwość wywołania konstruktora spoza
     * wnętrza klasy (nie jest tworzony konstruktor domyślny).
     */
    private Server(int portNumber) throws IOException {
        serverSocket = new ServerSocket(portNumber);
        serverThread = new Thread(this);
    }

    /**
     * Tworzenie obiektu Server, z podanym numerem portu, w fabryce. Obiekt ten
     * ma interfejs Runnable, dlatego może być przekazany do wywoływania w innym
     * wątku (Thread).
     *
     * @param portNumber numer portu
     * @return utworzony obiekt typu Server.
     */
    public static Server create(int portNumber) {
        try {
            Server that = new Server(portNumber);
            that.serverSocket.setSoTimeout(1000); // 1000 ms timeout
            that.serverThread.start();
            Debug.output("Server started");
            return that;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Najważniejsza metoda klasy - czekanie na połączenia przychodzące.
     */
    @Override
    public void run() {

        Debug.output("Server is running");

        /*
         * Wątki Javy są "nie do zabicia" z zewnątrz, tj. metoda stop() nigdy
         * nie powinna być używana, bo zostawia po sobie bałagan. Dlatego pętla
         * sprawdza, okresowo, z okresem timeout, czy wątek nie jest przerwany.
         */
        while (!serverThread.isInterrupted()) {
            try {
                /*
                 * Próba nawiązania łączności - czekanie na klienta który się
                 * dołączy. Jeżeli nastąpi timeout, to nie zostanie wywołana
                 * druga linia, tzn. nie zostanie stworzone nowe Connection.
                 *
                 * UWAGA: stworzenie Connection nie oznacza jeszcze poprawności
                 * połączenia! Np. może być tak, że klient nie będzie tym czym
                 * chcemy aby był - tzn. "naszym klientem".
                 */
                Socket clientSocket = serverSocket.accept();
                Connection connection = Connection.create(clientSocket);
                connection.setReplyString(replyString);
                connections.add(connection);
            } catch (IOException ex) {
                /*
                 * Nic się nie stało. Po prostu długie czekanie wywołało timeout
                 * - jeszcze raz będziemy (w pętli) czekać na podłączenie się
                 * klienta - jednak ustawienie timeout'u daje szansę sprawdzenia
                 * czy nie należy zamknąć serwera. Dałoby się to zrobić siłowo,
                 * wywołując stop() dla wątku - jednak jest to BARDZO ZŁE
                 * ROZWIĄZANIE - i nigdy nie należy (według Sun/Oracle) tego
                 * robić.
                 */
            }
        }

        /*
         * W tym miejscu WIEMY że wątek obiektu Server interrupted. Czyli że
         * przerwywamy pracę serwera, a to oznacza zamknięcie wszystkich
         * otwartych (tzn. zapisanych do connections) połączeń.
         */
        Debug.output("Server run interrupted");

        for (Connection connection : connections) {
            connection.close();
        }

        Debug.output("Server has closed all conections");
    }

    /**
     * Metoda close przerywa dzialanie wątku skojarzonego z obiektem Server.
     * Jest wymagana dla obiektów z interfejsem AutoCloseable.
     */
    @Override
    public void close() {
        Debug.output("Server interrupt server thread");
        serverThread.interrupt();
    }

    void setReply(String replyString) {
        this.replyString = replyString;
    }

}


/**
 * Klasa Connection opakowuje, widziane ze strony serwera, pojedyncze połączenie
 * z klientem. Serwer odbiera wiele połączeń - każde z nich po odebraniu służy
 * stworzeniu obiektu Connection. Każdy obiekt Connection tworzy sobie swój
 * własny wątek tylko i wyłącznie na potrzeby jednego połączenia.
 *
 * @author Sławomir Marczyński
 * @version 0.2
 */
class Connection implements Runnable, AutoCloseable {

    private final Socket socket;
    private final Thread thread;
    private String replyString;

    private Connection(Socket clientSocket) {
        socket = clientSocket;
        thread = new Thread(this);
    }

    static Connection create(Socket clientSocket) {
        try {
            Connection connection = new Connection(clientSocket);
            connection.socket.setSoTimeout(1000);
            connection.thread.start();
            return connection;
        } catch (SocketException ex) {
            return null;
        }
    }

    @Override
    public void run() {
        try (
                BufferedReader input
                = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output
                = new PrintWriter(socket.getOutputStream(), true);) {

            while (!Thread.currentThread().isInterrupted()) {
                String line = input.readLine();
                if (line != null) {
                    Debug.output("Server [client->server] : " + line);
                    output.println(replyString + line);
                    Debug.output("Server send echo");
                }
                Thread.sleep(100);
            }

        } catch (IOException | InterruptedException ex) {
        }
    }

    @Override
    public void close() {
        thread.interrupt();
    }

    void setReplyString(String replyString) {
        this.replyString = replyString;
    }

}
