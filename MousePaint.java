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
package example.mousepaint;

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
                 */
                @Override
                public void paint(Graphics graphics) {

                    // Graphics2D jest subklasą klasy Graphics, udostępnia wiele
                    // więcej niż sama klasa Graphics. Metoda paint ma jako
                    // parametr zwykłe Graphics, ale de facto dostaje zwawsze
                    // Graphics2D. Aby jednak używać rozszerzonych możliwości
                    // trzeba najpierw "rzutować w dół" (downcast) graphics.
                    //
                    Graphics2D graphics2d = (Graphics2D) graphics;

                    // Teraz jest bardzo prosto - rysujemy wszystkie linie.
                    //
                    // Nie jest to zrobione w najlepszy możliwy sposób
                    // - niepotrzebnie są powtarzane (jeżeli kompilator tego
                    // nie zoptymalizował) niektóre operacje - ale działa.
                    //
                    // @todo: Dane są zapisane jako lista list z punktami,
                    //        a jakoś tak nieszczęśliwie nie ma w Graphics2D
                    //        rysowania łamanej zadanej jako lista punktów.
                    //        Być może należałoby zrobić taką metodę (rysowanie
                    //        listy punktów) i z niej korzystać - zamiast tego
                    //        co jest poniżej - czyli konwersji na tablice
                    //        i potem rysowania danych z tablicy.
                    //        Być może należałoby od razu dane przechowywać
                    //        osobno x, osobno y, a wtedy dałoby się użyć
                    //        graphics2d.drawPolyline(...)
                    //
                    for (var line : lines) {
                        Point[] p = line.toArray(new Point[0]);
                        for (int i = 1; i < p.length; i++) {
                            var x1 = p[i - 1].x;
                            var y1 = p[i - 1].y;
                            var x2 = p[i].x;
                            var y2 = p[i].y;
                            graphics2d.drawLine(x1, y1, x2, y2);
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

                // Ten fragment programu przechwytuje zdarzenia kliknięcia
                // - czyli takie że najpiew guzik myszy został naciśnięty,
                // a następnie puszczony w tym samym miejscu.
                //
                // Zamiast mouseClicked można użyć mousePressed - wtedy reakcja
                // będzie na samo naciśnięcie (czyli po naciśnięciu będzie można
                // przesunąć mysz i też się wywoła).
                //
                @Override
                public void mouseClicked(MouseEvent event) {

                    Graphics2D g = (Graphics2D) canvas.getGraphics();
                    g.fillRect(event.getX(), event.getY(), 5, 5);

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
