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

import java.awt.Color;

/**
 * Klasa reprezentująca zestaw danych, jakie mają być pokazywane na wykresie.
 *
 * Każdy zestaw składa się z ciągu odciętych i ciągu rzędnych oraz określenia
 * koloru w jakim te dane mają być wykreślane. Oczywiście jest to tylko przykład
 * i dlatego brak w nim możliwości które mogłyby być konieczne np. dla osób
 * mających trudności z rozpoznawaniem kolorów.
 */
public class DataSet {

    // Dlaczego private? Dlaczego nie final? Dlaczego nie static?
    //
    // Private, bo nie chcemy aby możliwy był bezpośredni dostęp do tych danych.
    // Nie-final, bo być może w przyszłości będziemy umożliwiali zmianę tych
    // wartości. Dlaczego nie static? Bo chcemy móc wstawiać więcej niż jeden
    // wykres.
    //
    private int numberOfDataPoints;
    private double[] x;
    private double[] y;
    private Color color;

    /**
     * Tworzy obiekt klasy DataSet na podstawie otrzymanych danych. Powstający
     * obiekt dostaje kopię danych, tak więc na zewnątrz nie wypływają na to
     * co będzie wykreślał program.
     *
     * @param x tablica liczb zmiennoprzecinkowych.
     * @param y tablica liczb zmiennoprzecinkowych, tak samo duża jak x.
     * @param color kolor jakim ma być kreślona linia, np. COLOR.BLACK.
     */
    public DataSet(double[] x, double[] y, Color color) {

        // Sprawdzamy czy wszystkie parametry są różne od null, jeżeli nie to
        // zgłaszamy problemy wywołując wyjątek.
        //
        if (x == null || y == null || color == null) {
            throw new NullPointerException();
        }

        // Sprawdzamy, czy długości tabel są jednakowe i większe od zera.
        //
        if (x.length == y.length && x.length > 0) {
            this.numberOfDataPoints = x.length;
            this.x = x.clone();
            this.y = y.clone();
            this.color = color;
        } else {
            throw new IllegalArgumentException();
        }
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
     * @param i numer wierzchołka łamanej, zaczynając od zera.
     * @return wartość odciętej
     */
    public double getX(int i) {
        return x[i];
    }

    /**
     * Zwraca wartość rzędnej.
     *
     * @param i numer wierzchołka łamanej, zaczynając od zera.
     * @return wartość rzędnej.
     */
    public double getY(int i) {
        return y[i];
    }

    /**
     * Zwraca wartość koloru, jakim powinna być rysowana łamana.
     *
     * @return kolor.
     */
    public Color getColor() {
        return color;
    }
}
