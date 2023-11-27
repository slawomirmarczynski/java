/*
 * The MIT License
 *
 * Copyright (c) 2023 Sławomir Marczyński.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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

package swingplot;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Obiekty klasy Plot są po prostu wykresami XY.
 */
public class Plot extends JPanel {

    // Dlaczego to są finalne pola statyczne klasy, a nie zmienne lokalne metody
    // paintComponent? W przyszłości planujemy możliwość automatycznego
    // ustalania jak duże mają być marginesy i ewentualnie ich zmiany w razie
    // potrzeby. To oznacza że powstaną metody takie jak setTopMargin itp.
    //
    private final static int topMargin = 50;
    private final static int bottomMargin = 50;
    private final static int leftMargin = 80;
    private final static int rightMargin = 50;

    // Wykres zawiera dwie osie - jedną odciętych, drugą rzędnych.
    //
    final XAxis xAxis = new XAxis();
    final YAxis yAxis = new YAxis();
    final Plotter plotter = new AdvancedPlotter();

    // Dane do wykreślania są gromadzone na liście.
    //
    // Uwaga: obecna wersja programu ma mechanizmy dodawania danych do listy,
    //        ale nie ma mechanizmów usuwania tych danych z listy. Oczywiście
    //        tę niedogodność można łatwo usunąć.
    //
    final List<DataSet> dataSets = new LinkedList<>();

    // Nazwa całego wykresu jest tu. Nazwy osi są w obiektach xAxis i yAxis.
    // Lepiej dać pusty łańcuch znaków niż null, bo null wymagałby odrębnego
    // sprawdzania, a pusty łańcuch znaków może (powinien) być bezpiecznie
    // rysowany zawsze.
    //
    private String title = "";

    public Plot() {
        super();  // wywołanie konstruktora klasy bazowej

        // Moglibyśmy pozostawić "naturalny" kolor okna, ale biały lepiej
        // wygląda z wykresem. Przynajmniej takie jest moje subiektywne czucie.
        //
        this.setBackground(Color.WHITE);
    }

    /**
     * Dodawanie do wykresu danych które mają być potem wykreślane.
     *
     * @param x    tablica double[] zawierającą wartości odciętych.
     * @param y    tablica double[] zawierającą wartości rzędnych.
     * @param code kod taki jak w Matlab, litera oznaczaja kolor, minusy
     *             oznaczają linię ciągłą lub (gdy są dwa) przerywaną,
     *             litera o oznacza wstawianie okręgów jako symboli punktów.
     */
    void addDataSet(double[] x, double[] y, String code) {
        dataSets.add(new DataSet(x, y, code));
    }

    /**
     * Metoda odpowiedzialna za odmalowywanie komponentu.
     *
     * @param graphics jest obiektem <code>Graphics</code>.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);  // rysowanie tła
        Graphics2D g2d = (Graphics2D) graphics;

        // Bez włączenia antyaliasingu obraz nie jest zbyt ładny, włączamy
        // antyaliasing, co na współczesnych komputerach nie będzie problemem.
        //
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Aby wszystko rozmieścić, potrzebujemy wiedzieć, ile mamy miejsca,
        // gdzie je mamy i jakie są rozmiary czcionek. To dodatkowy narzut
        // i być może dałoby się to (przynajmniej częściowo) robić poza metodą
        // paintComponent. Ale wzrósłby wtedy poziom komplikacji programu,
        // musielibyśmy się też zastanawiać czy mamy aktualne dane.
        //
        // @todo: dla niewielkich rozmiarów okna możliwe jest aby client_width
        //        i/lub client_height były ujemne, co doprowadzi do dziwacznych
        //        rezultatów - należałoby temu przeciwdziałać.
        //
        final int client_width = getWidth() - leftMargin - rightMargin;
        final int client_height = getHeight() - topMargin - bottomMargin;
        final int xOffset = leftMargin;
        final int yOffset = topMargin + client_height;
        final FontMetrics metrics = graphics.getFontMetrics();
        final int fontHeight = metrics.getHeight();
        final int leading = metrics.getLeading();

        // Rysowanie osi. Ponieważ osie rysujemy na początku, to efektywnie
        // będą one "na spodzie" wykresu. Czyli to co narysujemy później może
        // zakryć osie i linie siatki.
        //
        xAxis.paint(graphics, xOffset, yOffset, client_width, client_height);
        yAxis.paint(graphics, xOffset, yOffset, client_width, client_height);

        // Rysowanie tytułu wykresu.
        //
        final int titleWidth = metrics.stringWidth(title);
        final int centered = xOffset + (client_width - titleWidth) / 2;
        final int above = yOffset - client_height - fontHeight - leading;
        graphics.drawString(title, centered, above);

        // Rysowanie danych poprzedzone zawężeniem obszaru przycinania tak,
        // aby wypadał on wyłącznie wewnątrz osi współrzędnych.
        //
        graphics.drawRect(leftMargin, topMargin, client_width, client_height);
        graphics.clipRect(leftMargin, topMargin, client_width, client_height);
        for (DataSet data : dataSets) {
            plotter.paint(graphics, xAxis, yAxis, data);
        }
    }

    /**
     * Po prostu nadawanie tytułu całemu wykresowi.
     *
     * @param title tytuł wykresu.
     */
    public void setTitle(String title) {
        this.title = title;
        invalidate();
    }

    /**
     * Po prostu nadanie nazwy dla osi x.
     *
     * @param string nazwa osi
     */
    public void setXAxisLabel(String string) {
        xAxis.setLabel(string);
        invalidate();
    }

    /**
     * Po prostu nadanie nazwy dla osi y.
     * @param string nazwa osi
     */
    public void setYAxisLabel(String string) {
        yAxis.setLabel(string);
        invalidate();
    }
}
