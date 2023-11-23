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

/**
 * Program jest klasą będącą przykładowym środowiskiem do demonstracji
 * możliwości Java Swing do samodzielnego tworzenia wykresów XY.
 * <p>
 * Klasa Program ma tylko jedną metodę. Zadaniem tej metody jest utworzyć
 * interfejs GUI Swing i w powstałym oknie graficznym osadzić obiekt klasy
 * Plot przekazując mu dane do wykreślenia.
 *
 * Dla lepszego efektu warto uruchamiać z parametrem -Dsun.java2d.uiScale=2.0
 * maszyny wirtualnej. Spowoduje to przeskalowanie GUI korzystne dla dużego DPI.
 */
public class Program {
    public static void main(String[] args) {

        // Dane do wykreślania są po prostu zwykłymi tablicami z liczbami
        // zmiennoprzecinkowymi. To bardzo proste rozwiązanie.

        final double[] x = {1, 2, 3, 4, 5, 8, 9};
        final double[] y = {2, 3, 3, 1, 5, 9, 5};
        final double[] z = {1, 5, 2, 9, 2, 1, 100};

        // Jeżeli usuniemy EventQueue.invokeLater to istnieje ryzyko, że program
        // czasami będzie działać, a czasami nie. Wystąpią dziwne błędy trudne
        // do zdiagnozowania. Bo invokeLater jest konieczne, gdyż uruchamiamy
        // program w domyślnym wątku, który nie jest wątkiem EDT, a na obiektach
        // Swing należy operować wyłącznie z wątku EDT.
        //
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Program do rysowania wykresów");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(640, 480);

            // Tworzenie nowego wykresu jest tak proste jak to tylko możliwe.
            // Po prostu zamiast obiektu JButton czy JLabel tworzymy nowy obiekt
            // Plot. Następnie, mając już obiekt plot, robimy różne rzeczy.
            //
            Plot plot = new Plot();
            plot.setTitle("Wykres");
            plot.setXAxisLabel("odcięte");
            plot.setYAxisLabel("rzędne");
            plot.addDataSet(x, y, Color.RED);
            plot.addDataSet(x, z, Color.BLUE);
            frame.add(plot);

            frame.setVisible(true);
        });
    }
}