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
 * Plotter i jej implementacje są realizacją wzorca projektowego Strategia.
 * <p>
 * W klasie Plot rysowanie danych może być realizowany w rozmaicie, oczywiście
 * za każdym razem ma powstawać rysunek, ale sposób rysowania może być rozmaity.
 * Dlatego definiujemy abstrakcyjny interfejs Plotter (w Javie zamiast klas
 * abstrakcyjnych warto używać interfejsów jeżeli to możliwe), który określa
 * abstrakcyjną strategię rysowania dostarczając funkcji paint. Konkretną
 * strategię (czyli sposób) rysowania wybieramy poprzez wybór konkretnej
 * implementacji abstrakcyjnej strategii. W ten sposób możemy łatwo rozszerzać
 * możliwości tworząc nowe implementacje interfejsu Plotter i to bez potrzeby
 * modyfikacji kodu starych implementacji tego interfejsu, przez co realizujemy
 * zastadę OCP.
 */
public interface Plotter {

    /**
     * Rysuje dane używając danych osi współrzędnych.
     *
     * @param graphics obiekt Graphics jaki dostaje z nadrzędnego paintComponent.
     * @param xAxis oś odciętych.
     * @param yAxis oś rzędnych.
     * @param data dane (ciąg punktów), jakie mają być wykreślone.
     */
    void paint(Graphics graphics, Axis xAxis, Axis yAxis, DataSet data);
}
