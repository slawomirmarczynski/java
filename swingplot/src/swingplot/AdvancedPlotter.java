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
import java.awt.geom.Path2D;

/**
 * Jeszcze jedna implementacja interfejsu Plotter. Ciekawa sama w sobie, bo do
 * działania używa prostszych `plotterów`, tak że jeżeli to możliwe deleguje do
 * nich wywołania metody paint. Jest elementem realizacji wzorca Strategia GoF
 * jako klasa konkretnej strategii.
 */
public class AdvancedPlotter implements Plotter {

    final LinePlotter simplePlotter = new LinePlotter();
    final PointPlotter dotPlotter = new PointPlotter();

    /**
     * Rysuje dane według wartości zapisanych w code jako linie ciągłe,
     * przerywane, z punktami lub bez, w zadanym kolorze.
     *
     * @param graphics obiekt Graphics jaki dostaje z nadrzędnego paintComponent.
     * @param xAxis oś odciętych.
     * @param yAxis oś rzędnych.
     * @param data dane (ciąg punktów), jakie mają być wykreślone.
     */
    @Override
    public void paint(Graphics graphics, Axis xAxis, Axis yAxis, DataSet data) {
        String code = data.getCode();
        if (code.contains("--")) {
            paint_dashed(graphics, xAxis, yAxis, data);
        } else if (code.contains("-")) {
            simplePlotter.paint(graphics, xAxis, yAxis, data); // paint solid
        }
        if (code.contains("o")) {
            dotPlotter.paint(graphics, xAxis, yAxis, data);
        }
    }

    /**
     * Metoda wywoływana przez metodę paint wtedy gdy nie da się delegować
     * rysowania do metod klas PointPlotter lub LinePlotter.
     *
     * @param graphics obiekt Graphics jaki dostaje z nadrzędnego paintComponent.
     * @param xAxis oś odciętych.
     * @param yAxis oś rzędnych.
     * @param data dane (ciąg punktów), jakie mają być wykreślone.
     */
    private void paint_dashed(Graphics graphics, Axis xAxis, Axis yAxis, DataSet data) {

        final int n = data.getNumberOfDataPoints();
        if (n > 0) {
            Graphics2D g2d = (Graphics2D) graphics;
            final int strokeWidth = 2;
            Stroke dashed = new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
            g2d.setStroke(dashed);
            g2d.setColor(PointPlotter.getDatasetColor(data));

            Path2D path = new Path2D.Float();
            int x, y;
            x = xAxis.valueToPixel(data.getX(0));
            y = yAxis.valueToPixel(data.getY(0));
            path.moveTo(x, y);
            for (int i = 0; i < n; i++) {
                x = xAxis.valueToPixel(data.getX(i));
                y = yAxis.valueToPixel(data.getY(i));
                path.lineTo(x, y);
            }
            g2d.draw(path);
        }
    }
}
