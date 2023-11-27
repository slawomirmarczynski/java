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

/**
 * Klasa reprezentująca zestaw danych, jakie mają być pokazywane na wykresie.
 * <p>
 * Każdy zestaw składa się z ciągu odciętych i ciągu rzędnych oraz określenia
 * koloru w jakim te dane mają być wykreślane. Oczywiście jest to tylko przykład
 * i dlatego brak w nim możliwości, które mogłyby być konieczne np. dla osób
 * mających trudności z rozpoznawaniem kolorów.
 */
public class DataSet {

    // Dlaczego private? Dlaczego nie final? Dlaczego nie static?
    //
    // Private, bo nie chcemy umożliwiać bezpośredniego dostępu do tych danych.
    // Nie-final, bo być może w przyszłości będziemy zmieniali te wartości.
    // Dlaczego nie static? Bo chcemy móc wstawiać więcej niż jeden wykres.
    //
    private final int numberOfDataPoints;
    private final double[] x;
    private final double[] y;
    private final String code;

    /**
     * Tworzy obiekt klasy DataSet na podstawie otrzymanych danych. Powstający
     * obiekt dostaje kopię danych, tak więc na zewnątrz nie wypływają na to
     * co będzie wykreślał program.
     *
     * @param x    tablica liczb zmiennoprzecinkowych.
     * @param y    tablica liczb zmiennoprzecinkowych, tak samo duża jak x.
     * @param code
     */
    public DataSet(double[] x, double[] y, String code) {
        this.numberOfDataPoints = Math.min(x.length, y.length);
        this.x = x.clone(); // tablice są mutable, klonowanie defensywne
        this.y = y.clone(); // tablice są mutable, klonowanie defensywne
        this.code = code; // łańcuchy znaków są immutable, klonowanie zbędne
    }

    /**
     * Zwraca liczbę punktów danych.
     *
     * @return liczba punktów danych.
     */
    public int getNumberOfDataPoints() {
        return numberOfDataPoints;
    }

    /**
     * Zwraca wartość odciętej.
     *
     * @param index numer wierzchołka łamanej, zaczynając od zera.
     * @return wartość odciętej
     */
    public double getX(int index) {
        return x[index];
    }

    /**
     * Zwraca wartość rzędnej.
     *
     * @param index numer wierzchołka łamanej, zaczynając od zera.
     * @return wartość rzędnej.
     */
    public double getY(int index) {
        return y[index];
    }

    /**
     * Zwraca kod, zapisany w łańcuch znaków, określający w jaki sposób należy
     * wykreślić linię.
     *
     * @return kod, np. "r--o" oznacza czerwoną linię kreskowaną z zaznaczonymi
     *         jako okręgi punktami; kod ten jest zbliżony do tego którego używa
     *         się w programie Matlab i w bibliotece matplotlib w Pythonie.
     */
    public String getCode() {
        return code;
    }
}
