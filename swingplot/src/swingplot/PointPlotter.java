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

import java.awt.*;

/**
 * Klasa PointPlotter jest prostą implementacją Plottera umiejącą rysować
 * dane jako oddzielone od siebie punkty.
 */
public class PointPlotter implements Plotter {

    /**
     * Rysuje dane jako punkty używając danych osi współrzędnych.
     *
     * @param graphics obiekt Graphics jaki dostaje z nadrzędnego paintComponent.
     * @param xAxis oś odciętych.
     * @param yAxis oś rzędnych.
     * @param data dane (ciąg punktów), jakie mają być wykreślone.
     */
    @Override
    public void paint(Graphics graphics, Axis xAxis, Axis yAxis, DataSet data) {
        Graphics2D g2d = (Graphics2D) graphics;
        final int strokeWidth = 2;
        g2d.setStroke(new BasicStroke(strokeWidth));
        Color color = getDatasetColor(data);
        final int n = data.getNumberOfDataPoints();
        if (n >= 1) {
            for (int i = 0; i < n; i++) {
                int x = xAxis.valueToPixel(data.getX(i));
                int y = yAxis.valueToPixel(data.getY(i));
                graphics.setColor(Color.WHITE);
                final int RADIUS = 2;
                final int DIAMETER = 2 * RADIUS;
                graphics.fillOval(x - RADIUS, y - RADIUS, DIAMETER, DIAMETER);
                graphics.setColor(color);
                graphics.drawOval(x - RADIUS, y - RADIUS, DIAMETER, DIAMETER);
            }
        }
    }

    /**
     * Przetwarza kod określający szczegóły wykresu tak, aby wyciągnąć z niego
     * informację o kolorze.
     *
     * @param data zbiór danych
     * @return wartość koloru
     */
    public static Color getDatasetColor(DataSet data) {

        // Wydawać mogłoby się że ta metoda powinna być określona np. w klasie
        // Plotter jako klasie bazowej. Jednak zdefiniowanie getDatabaseColor
        // w PointColor jako metody statycznej nie ogranicza możliwości innej
        // interpretacji w przyszłych implementacjach interfejsu Plotter.
        // Więc jeżeli dodamy kolejne kolory, to nie musimy nic zmieniać.

        final String[] names = {"r", "g", "b", "k", "m", "c", "y"};
        final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE,
                Color.BLACK, Color.MAGENTA, Color.CYAN, Color.YELLOW};
        final String code = data.getCode();
        Color color = Color.BLACK;
        for (int i = 0; i < colors.length; i++) {
            if (code.contains(names[i])) {
                color = colors[i];
                break;
            }
        }
        return color;
    }
}
