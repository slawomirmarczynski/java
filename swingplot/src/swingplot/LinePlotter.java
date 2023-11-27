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

public class LinePlotter implements Plotter {
    @Override
    public void paint(Graphics graphics, Axis xAxis, Axis yAxis, DataSet data) {

        // Ten fragment można napisać rozmaicie, ale chcemy aby był możliwie
        // jak najczytelniejszy. Dlatego nie użyliśmy metody drawPolygon(),
        // ale po prostu łączymy kolejne punkty. Rozwiązanie to jest jednak
        // wadliwe w tym sensie, że gdyby linia była linią przerywaną, to
        // niegwarantowane byłoby prawidłowe utrzymanie długości przerw i/lub
        // kresek w wierzchołkach.
        //
        // Sprawdzamy, czy mamy dostateczną liczbę punktów. Powinien być
        // przynajmniej jeden, choć lepiej aby było ich więcej.
        //
        final int n = data.getNumberOfDataPoints();
        if (n > 0) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(PointPlotter.getDatasetColor(data));
            Path2D path = new Path2D.Float();
            path.moveTo(data.getX(0), data.getY(0));
            for (int i = 0; i < n; i++) {
                int x = xAxis.valueToPixel(data.getX(i));
                int y = yAxis.valueToPixel(data.getY(i));
                path.lineTo(x, y);
            }
            final int strokeWidth = 2;
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.draw(path);
        }
    }
}
