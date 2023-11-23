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
class Plot extends JPanel {

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
    XAxis xAxis = new XAxis();
    YAxis yAxis = new YAxis();
    Plotter plotter = new SimplePlotter();

    // Dane do wykreślania są gromadzone na liście.
    //
    // Uwaga: obecna wersja programu ma mechanizmy dodawania danych do listy,
    //        ale nie ma mechanizmów usuwania tych danych z listy. Oczywiście
    //        tę niedogodność można łatwo usunąć.
    //
    List<DataSet> dataSets = new LinkedList<>();

    // Nazwa całeo wykresu jest tu. Nazwy osi są w obiektach xAxis i yAxis.
    //
    private String title = "";

    public Plot() {
        super();

        // Moglibyśmy pozostawić "naturalny" kolor okna, ale biały lepiej
        // wygląda z wykresem. Przynajmniej takie jest moje subiektywne czucie.
        //
        this.setBackground(Color.WHITE);
    }

    /**
     * Dodawanie do wykresu danych które mają być potem wykreślane.
     *
     * @param x     tablica double[] zawierającą wartości odciętych.
     * @param y     tablica double[] zawierającą wartości rzędnych.
     * @param color kolor jakim ma być wykreślana inia.
     */
    void addDataSet(double[] x, double[] y, Color color) {
        dataSets.add(new DataSet(x, y, color));
    }

    /**
     * Metoda odpowiedzialna za odmalowywanie komponentu.
     *
     * @param graphics jest obiektem <code>Graphics</code>.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;

        // Bez włączenia antyaliasingu obraz nie jest zbyt ładny, włączamy
        // antyaliasing, co na współczesnych komputerach nie będzie problemem.
        //
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int width = getWidth();
        final int height = getHeight();
        final int client_width = width - leftMargin - rightMargin;
        final int client_height = height - topMargin - bottomMargin;
        final int xOffset = leftMargin;
        final int yOffset = topMargin + client_height;

        final FontMetrics metrics = graphics.getFontMetrics();
        final int fontHeight = metrics.getHeight();
        final int leading = metrics.getLeading();
        final int titleWidth = metrics.stringWidth(title);

        xAxis.paint(graphics, xOffset, yOffset, client_width, client_height);
        yAxis.paint(graphics, xOffset, yOffset, client_width, client_height);

        graphics.drawString(title, xOffset + (client_width - titleWidth) / 2, yOffset - client_height - fontHeight - leading);
        graphics.drawRect(leftMargin, topMargin, client_width, client_height);

        graphics.clipRect(xOffset, yOffset - client_height, client_width, client_height);
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
    }

    public void setXAxisLabel(String string) {
        xAxis.setLabel(string);
    }

    public void setYAxisLabel(String string) {
        yAxis.setLabel(string);
    }
}