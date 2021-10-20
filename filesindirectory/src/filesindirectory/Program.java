/*
 * The MIT License
 *
 * Copyright 2020 Sławomir Marczyński.
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
package filesindirectory;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Sławomir Marczyński
 */
public class Program implements Runnable {

    protected ResourceBundle resourceBundle;

    public Program() {
        setupLookAndFeel();
        setupL10n();
    }

    public static void main(String[] args) {
        Program program = new Program();
        program.run();
    }

    @Override
    public void run() {

        EventQueue.invokeLater(() -> {
            // Najpierw pytamy o rozmiar ekranu. Uwaga: w systemach z wieloma
            // monitorami dostaniemy rozmiar monitora "primary".
            //
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenDimension = toolkit.getScreenSize();
            final int height = screenDimension.height / 3;
            final int width = screenDimension.width / 3;

            // Tworzymy głowne (ale jeszcze puste) okno programu i określamy
            // jego preferowany rozmiar.
            //
            JFrame frame = new JFrame(translate("An example application"));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Dimension frameDimension = new Dimension(width, height);
            frame.setPreferredSize(frameDimension);

            // Tworzymy menadżera layout'u (parametr FlowLayout.LEADING
            // dosuwa elementy do tej strony ramki od której zaczyna się czytać
            // tekst - czyli do lewej jeżeli tekst jest od lewej-do-prawej).
            // Podpinamy menadżera do okna (a w zasadzie do "contentu" okna).
            //
            LayoutManager layoutManager = new FlowLayout(FlowLayout.LEADING);
            frame.setLayout(layoutManager);

            // Tworzymy guzik i wstawiamy go do "contentu" okna.
            // Naciśnięcie guzika zostanie zgłoszone do "listenera" (wzorzec
            // Obserwator), którym - w tym konkretnym przypadku - jest klasa
            // anonimowa implementująca interfejs ActionListener zapisana
            // w notacji lambda. Yep, to jest trochę skomplikowane i zakręcone.
            //
            // W czasie tworzenia guzika tłumaczymy nazwę na tę która odpowiada
            // danemu "locale". Możnaby zorganizować to także i w ten sposób,
            // że zamiast tworzyć JButton tworzylibyśmy obiekt subklasy klasy
            // JButton, np. MyL10nJButton, którego konstruktor (ew. fabryka)
            // kapsułkowałby proces tłumaczenia. Pozostaje jednak problem
            // - jak zmieniać język w trakcie działania programu - czyli już
            // po rozpoczęciu jego pracy.
            //
            JButton openButton = new JButton(translate("open"));
            openButton.addActionListener((ActionEvent event) -> {

                // Ten fragment programu zostaje wywołany kiedyś, gdzieś,
                // w odpowiedzi na naciśnięcie guzika. Czyli nie bezpośrednio
                // przed dodaniem/pokazaniem guzika - ale dopiero gdy ten guzik
                // zostanie naciśnięty.
                //
                // Najpierw tworzymy sobie obiekt chooser i konfigurujemy go.
                // Potem każemu mu wyświetlić dialog wyboru plików i katalogów.
                // Następnie - ale tylko wtedy gdy użytkownik zaakceptował
                // wybór ("ok" lub coś podobnego) - robimy co trzeba.
                //
                // Patrz też https://mkyong.com/swing/java-swing-jfilechooser-example
                //
                final JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setMultiSelectionEnabled(true);
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {

                    // processFiles jest napisaną przez nas metodą - patrz niżej
                    //
                    processFiles(chooser.getSelectedFiles());
                }

            });
            frame.getContentPane().add(openButton);

            // Dopasowujemy do siebie rozmiaru elementów (tak aby każdy był
            // nie mniejszy niż preferowana wielkość) i pokazywanie/uruchamianie
            // okna.
            //
            frame.pack();
            frame.setVisible(true);
        }
        );
    }
    String translate(String s)
    {
        try{
            return resourceBundle.getString(s.toLowerCase().replaceAll(" ", "_"));
        }catch(MissingResourceException ex) {
            return s;
        }
    }


    /**
     * Metoda która dostaje tablicę files - z plikami jako takimi i z katalogami
     * (folderami) w których mogą być pliki oraz katalogi - i która wypisuje
     * nazwy wszystkich plików jakie tylko odnajdzie.
     *
     * @param files tablica obiektów File
     */
    private void processFiles(File[] files) {
        if (files != null) {
            for (File file : files) {
                //
                // Nie ma konieczności sprawdzania czy file != null, bo nie
                // powinno być. Ale chyba też nie zaszkodzi. Zamiast println
                // może być oczywiście robione coś innego - to czego akurat
                // będziemy potrzebowali we własnym programie.
                //
                // Sprawdzanie czy plik nie jest plikiem ukrytym ukrywa pliki.
                //
                if ((file != null) && (file.isHidden() == false)) {
                    if (file.isFile()) {
                        System.out.println(translate("zwykły plik ") + file.getPath());
                    } else if (file.isDirectory()) {
                        System.out.println(translate("katalog     ") + file.getPath());
                        processFiles(file.listFiles());
                    } else {
                        // Nie wiadomo co - ani katalog, ani zwykły plik
                    }
                }
            }
        }
    }

    /**
     * Lokalizacja programu: dopasowywanie jego działania do potrzeb użytkownika
     * tak aby mógł go obsługiwać w swoim języku naturalnym oraz wszystkimi
     * innymi ustawieniami jakie są dla niego najodpowiedniejsze.
     */
    private void setupL10n() {
        UIManager.getDefaults().addResourceBundle("example.filesindirectory.messages.jfilechooser");
        resourceBundle = ResourceBundle.getBundle("example.filesindirectory.messages.program");
    }

    /**
     * Ustalanie look-and-feel (tzw. laf), czyli jaki mają wygladać kontrolki -
     * czy mają przypominać te znane z MS Windows, czy raczej takie jakie są na
     * komputerach Apple, czy może jeszcze inne?!
     */
    private void setupLookAndFeel() {
        // Tablica zawierająca preferowane i tablica zawierające dostępne LaF.
        // Są one final, bo nie będą modyfikowane po utworzeniu.
        //
        final String[] preferredNames = {"Windows", "Nimbus"}; // @todo: dopisać więcej
        final UIManager.LookAndFeelInfo[] installed = UIManager.getInstalledLookAndFeels();

        // Negocjowanie jaki LaF ma być użyty - ponieważ dostępnych LaF jest
        // niewiele (kilka, może kilkanaście) i niewiele jest preferowanych LaF
        // - to użycie dwóch pętli for (jak poniżej) nie jest aż tak złe... jak
        // mogłoby się to wydawać. Przechwytywanie wyjątków (try-catch) jest
        // konieczne, ale - poza odnotowaniem że coś się dzieje - nie wymaga
        // szczególnych kroków - w najgorszym razie nic się nie uda i pozostanie
        // standardowy wygląd kontrolek - co jest dobrym rozwiązaniem.
        //
        try {
            for (String bestName : preferredNames) {
                for (UIManager.LookAndFeelInfo avaliable : installed) {
                    if (avaliable.getName().equals(bestName)) {
                        UIManager.setLookAndFeel(avaliable.getClassName());
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
