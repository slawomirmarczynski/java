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
package mousepaint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 * Program demonstrujący jak można rysować za pomocą myszy używając tylko
 * Swing'a.
 *
 * @author Sławomir Marczyński
 */
public class Program implements Runnable {

    /**
     * Uruchamianie programu.
     *
     * Metoda main musi być statyczna, bo uruchamia się gdy jeszcze nie żadnego
     * obiektu - więc nie można wywoływać nie-statycznych metod. Fakt że to jest
     * metoda statyczna uniemożliwia np. używanie zmiennej this, odwoływanie się
     * do nie-statycznych pól klasy i może stwarzać jeszcze inne trudności.
     * Dlatego main służy zasadniczo tylko do utworzenia obiektu klasy Program i
     * uruchomienia jego metody run.
     *
     * @param args parametry wywołania programu, obecnie nie wykorzystywane, ale
     * w przyszłości mogą się do czegoś przydać, np. do wyboru L&F, itp. itd.
     */
    public static void main(String[] args) {

        // Java Swing wspiera rozmaite L&F, czyli może naśladować rozmaite
        // systemy operacyjne lub tworzyć interfejs graficzny właściwy tylko
        // dla Swing'u. Aby nie zaśmiecać kodu szczegółami wybór L&F jest
        // delegowany do statycznej metody setupLookAndFeel.
        //
        setupLookAndFeel();

        // Tworzenie obiektu klasy Program i uruchamianie jego metody run().
        // W zasadzie można zrobić to krócej, po prostu (new Program()).run(),
        // albo nawet new Program.run(), ale tak jak poniżej też jest dobrze.
        // A może nawet bardziej czytelnie - najpierw tworzymy obiekt, kolejna
        // linia aktywuje metodę run tego obiektu - w razie debugowania można
        // spokojnie przejść te kroki osobno.
        //
        Program program = new Program();
        program.run();
    }

    /**
     * Metoda run jest konieczna aby klasa Program była Runnable, tzn. miała
     * interfejs Runnable. W zasadzie wszystko jedno jak ją nazwiemy, mogłaby
     * nazywać się command albo execute, albo jeszcze inaczej. Zysk z Runnable
     * polega na tym, że jest to standard w Javie.
     */
    @Override
    public void run() {

        // NIE WOLNO bezpośrenio wywoływać funkcji interfejsu graficznego Swing
        // - bo oznacza to użycie wątku EDT (events-dispaching-thread) z innego
        // (naszego) wątku - i może doprowadzić do małej katastrofy. Ostępstwem
        // od tej reguły jest sytuacja gdy wątek EDT sam się do nas zgłasza, ale
        // tym razem tak nie jest. Dlatego konieczne jest "zakolejkowanie"
        // istrukcji koniecznych do wykonania w wątku EDT w kolejce EventQueue
        // tak jak poniżej.
        //
        // Do EventQueue wrzuca się obiekt Runnable, niezłym pomysłem jest
        // użycie klasy anonimowej new Runnable() {...}, ale jeszcze lepsza jest
        // notacja lambda, czyli użycie ()->{...}. Zamiast kropek oczywiście są
        // konkretne instrukcje przekazane do wykonania w wątku EDT.
        //
        // Po wywołaniu EventQueue.invokeLater program nie czeka na wykonanie
        // zakolejkowanych instrukcji, tylko od razu przechodzi dalej.
        //
        EventQueue.invokeLater(() -> {

            // Najpierw pytamy o rozmiar ekranu. Uwaga: w systemach z wieloma
            // monitorami dostaniemy rozmiar monitora "primary".
            //
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenDimension = toolkit.getScreenSize();

            // Znając rozmiary ekranu tworzone jest odpowiednio duże okno.
            // Unikamy jawnego podawania wielkości, dzięki temu program zadziała
            // na ekranach 8K i na dużo skromniejszych ekranach.
            //
            final int height = screenDimension.height / 2;
            final int width = screenDimension.width / 2;

            // Tworzymy głowne (ale jeszcze puste) okno programu i określamy
            // jego preferowany rozmiar. Ustawiamy zachowanie programu po
            // zamknięciu głownego okna programu - program ma EXIT_ON_CLOSE
            // czuli ma zostać zamknięty.
            //
            JFrame frame = new JFrame("Rysowanie");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Dimension frameDimension = new Dimension(width, height);
            frame.setPreferredSize(frameDimension);

            // Uwaga: pułapka - BoxLayout potrzebuje ContentPane a nie frame.
            //
            BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS);
            frame.setLayout(layout);

            // Tworzymy obiekt na którym można rysować - zywkle najkorzystniej
            // wybrać do tego JComponent lub JPanel. Użycie var uwalnia nas od
            // konieczności używania zmiennej o type nadklasy do przechowywania
            // wyspecjalizowanego obiektu anonimowej podklasy.
            //
            // Zauważmy, że użycie po prostu klasy JComponent, zamiast obiektu
            // dziedziczącego po JComponent, spowodowałoby standardowe działanie
            // metody paint - a tym samym nie mogłoby nic narysować.
            //
            var canvas = new JComponent() {

                // Zaszalejemy - tworzymy (pustą na razie) listę linii.
                // Nie jest to aż tak trudne jak mogłoby się wydawać.
                //
                LinkedList<LinkedList<Point>> lines = new LinkedList<>();

                /**
                 * Odmalowywanie zawartości okna.
                 *
                 * Można to robić w paint, można to robić w paintComponent.
                 * Dla kontrolek Swing'a paint wywołuje paintComponent, potem
                 * paintBorder i wreszcie paintChildren. Więc jeżeli zrobimy
                 * to w paint (a nie w paintComponent), to jeżeli nie wywołamy
                 * super.paint() - a nie wywołamy, bo tego się nie robi gdy
                 * nadpisuje się metodę paint - nie zostaną narysowane boki
                 * i dzieci - bo nie będą wywołane paintBorder i paintChildrens.
                 * Różnica nieistotna, bo i tak dla JComponent nic tam ciekawego
                 * nie było. Ale warto wiedzieć.
                 */
                @Override
                public void paintComponent(Graphics graphics) {

                    // Graphics2D jest subklasą klasy Graphics, udostępnia wiele
                    // więcej niż sama klasa Graphics. Metoda paint ma jako
                    // parametr zwykłe Graphics, ale de facto dostaje zwawsze
                    // Graphics2D. Aby jednak używać rozszerzonych możliwości
                    // trzeba najpierw "rzutować w dół" (downcast) graphics.
                    //
                    Graphics2D graphics2d = (Graphics2D) graphics;

                    // Teraz jest bardzo prosto - rysujemy wszystkie linie.
                    //
                    for (var polyLine : lines) {
                        paintPolyLine(graphics2d, polyLine);
                    }

                    // Jeżeli obiekt Graphics jest uzystany jako parametr
                    // wywołania metody paint to nie wywołuje się dispose,
                    // bo to nie my jesteśmy odpowiedzialni wtedy za recykling.
                    // Reguła jest prosta - nie nasze - nie niszczymy.
                    //
                    // NIE DAJEMY graphics2d.dispose(); - bo moglibyśmy oddać
                    // do recyclingu kontekst grafiki (obiekt klasy Graphics)
                    // który byłby jeszcze potrzebny.
                }

                // Kto powiedział że klasa anonimowa nie może mieć takich metod?
                //
                private void paintPolyLine(Graphics2D graphics2d, LinkedList<Point> list) {
                    if (list != null && !list.isEmpty()) {
                        Point p = list.getFirst();
                        for (Point q : list) {
                            graphics2d.drawLine(p.x, p.y, q.x, q.y);
                            p = q;
                        }
                    }
                }
            };

            // Wywołując addMouseListener dodajemy do obiektu canvas obserwatora
            // do którego będą kierowane zdarzenia generowane przez różne rzeczy
            // robione za pomocą myszy: kliknięcia itp. Sam obserwator musi być
            // obiektem mającym interfejs MouseListener, czyli umieć obsługiwać
            // wszystko. Dla ułatwienia używa się MouseAdapter'a - klasy która
            // implementuje cały interfejs MouseListener jako nic nie robiące
            // metody. W subklasie (poniżej jest anoniomowa subklasa) można
            // nadpisać tylko te z metod MouseAdapter'a które naprawdę musimy
            // nadpisać - pozostałe pozostaną bez zmian - i o to chodzi.
            //
            // Nota bene, większość kodu w NetBeans można pisać naciskając
            // ctrl+space - pojawi się menu z którego wystarczy wybrać co ma
            // być wstawione do programu.
            //
            canvas.addMouseListener(new MouseAdapter() {

                // Tu jest dość ciekawy trik - ponieważ używamy klasy anonimowej
                // to nie możemy napisać dla niej konstruktora, bo nie wiemy jak
                // się ta klasa nazywa - ale możemy przeprowadzić inicjalizację
                // przy pomocy takiego bloku jak poniżej. Nota bene, poza takimi
                // blokami można użyć także bloków ze słowem kluczowym static
                // do inicjalizowania składowych statycznych.
                //
                // Jak to działa? Utworzenie nowego obiektu doda do canvas.lines
                // nową pustą listę do zbierania wyklikiwanych punktów.
                //
                {
                    canvas.lines.add(new LinkedList<Point>());
                }

                // Zamiast mousePressed można użyć mouseClicked - ale działa to
                // trochę gorzej - mały ruch myszy i nie łapie clicków...
                //
                @Override
                public void mousePressed(MouseEvent event) {

                    // Tym razem to my występujemy z inicjatywą o kontekst
                    // graficzny Graphics - więc to my wywołamy dispose na rzecz
                    // tego obiektu.
                    //
                    Graphics2D g = (Graphics2D) canvas.getGraphics();
                    g.setColor(Color.BLUE);
                    g.drawRect(event.getX(), event.getY(), 25, 25);
                    g.setColor(Color.RED);
                    g.fillOval(event.getX(), event.getY(), 15, 15);

                    // Ekologiczny recycling - bez tego też będzie działać,
                    // ale uwalnianie kontekstu zwalnia zasoby - i to może być
                    // bardzo ważne, bo z tym kontekstem wiązać się mogą bardzo
                    // ograniczone zasoby systemowe.
                    //
                    g.dispose();

                    // Trochę pracy przed nami - trzeba sprawdzić który guzik
                    // myszy został naciśnięty. Problem w tym że różne myszy
                    // mogą mieć różne liczby guzików. Dlatego niezależnie od
                    // guzików 2 i 3 można będzie używać guzika 1 z wciśniętym
                    // shiftem lub ctrl.
                    //
                    int mouseButton = event.getButton();
                    if (mouseButton == MouseEvent.BUTTON1) {
                        if (event.isShiftDown()) {
                            mouseButton = MouseEvent.BUTTON2;
                        }
                        if (event.isControlDown()) {
                            mouseButton = MouseEvent.BUTTON3;
                        }
                    }

                    // Tu podejmujemy działanie - albo dodajemy kolejny punkt
                    // i odmalowujemy (wywołuje się przez to paint) canvas
                    // - albo dodajemy nową listę punktów do listy list (czyli
                    // kończymy rysowanie łamanej) - albo usuwamy wszystkie
                    // punkty i przygotowujemy się do rysowania od nowa.
                    //
                    if (mouseButton == MouseEvent.BUTTON1) {
                        canvas.lines.getLast().add(event.getPoint());
                        canvas.repaint();
                    } else if (mouseButton == MouseEvent.BUTTON3) {
                        canvas.lines.add(new LinkedList<Point>());
                    } else if (mouseButton == MouseEvent.BUTTON2) {
                        canvas.lines.clear();
                        canvas.lines.add(new LinkedList<Point>());
                        canvas.repaint();
                    }
                }
            });

            // Oczywiście canvas, jako kontrolka, jest bardzo istotnym elementem
            // programu, to można mieć jeszcze inne kontrolki, np. - tak jak tu
            // - etykietkę JLabel z tekstem.
            //
            var label = new JLabel("LMB/RMB rysowanie, MMB kasowanie.");

            // Wstawianie kontrolek do ramki frame, a ściślej do panelu jaki
            // jest wstawiony do ramki frame. Ramka jako taka zawieraj jeszcze
            // różne "dekoracje" - czyli np. ikonki, tytuł okienka, ...
            //
            frame.getContentPane().add(canvas);
            frame.getContentPane().add(label);

            // Dopasowujemy do siebie rozmiaru elementów (tak aby każdy był
            // nie mniejszy niż preferowana wielkość) i pokazywanie/uruchamianie
            // okna.
            //
            frame.pack();
            frame.setVisible(true);
        }
        );
    }

    /**
     * Ustalanie look-and-feel (tzw. laf), czyli jaki mają wygladać kontrolki -
     * czy mają przypominać te znane z MS Windows, czy raczej takie jakie są na
     * komputerach Apple, czy może jeszcze inne?!
     */
    private static void setupLookAndFeel() {
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
