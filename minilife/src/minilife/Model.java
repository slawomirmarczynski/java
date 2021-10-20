/*
 * The MIT License
 *
 * Copyright 2021 Sławomir Marczyński.
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
package minilife;

/**
 * Klasa będąca modelem "gry w życie" ze zmienionymi regułami.
 *
 * Nasz program nieco różni się od "prawdziwej gry w życie" - zasady są inne:
 * mamy siatkę o pewnej liczbie wierszy i pewnej liczbie kolumn; jeżeli komórka
 * w i-tym wierszu i w j-tej kolumnie sąsiaduje z jakąkolwiek zajętą komórką to
 * sama w następnym cyklu jest zajęta; ale jeżeli komórka jest otoczona przez
 * zajęte komórki to jest opróżniana (przestaje być zajęta). Oczywiście nic nie
 * stoi na przeszkodzie aby te reguły zastąpić orginalnymi regułami gry life
 * (patrz Wikipedia), jednak nie jest to najważniejsze. Najważniejsze jest
 * zrozumieć jak można podzielić program na współpracujące ze sobą klasy i jak
 * te klasy ze sobą współpracują.
 *
 * @author Sławomir Marczyński
 */
class Model {

    // Po co są width i height, skoro moglibyśmy przechowywać całe options
    // jakie dostaje konstruktor? Mając model możemy po prostu napisać
    // model.width, co jest znacznie prostsze niż model.getWidth() albo
    // model.options.width. Mamy też gwarancję, że nawet jeżeli opcje się
    // gdzieś jakoś zmieną, to te width i height są pod kontrolą obiektu model.
    // Zwłaszcza że słowo kluczowe final zabezpiecza je przed zmianami wartości.
    //
    final int width;
    final int height;
    private boolean[][] board;
    private boolean[][] updated1;
    private boolean[][] updated2;

    Model(Options options) {

        this.width = options.width;
        this.height = options.height;

        board = new boolean[height][width];

        // Teraz ciekawostka - tablica board jest wypełniona wartościami false,
        // bo jej elementy są typu boolean. Nie jest tak jednak gdybyśmy mieli
        // tablicę elementów Boolean (dużą literą), bo wtedy domyślne jest null.
        //
        // Dlatego tym razem niepotrzebne jest inicjalizowanie elementów takie
        // jak poniżej w komentarzu:
        //
        //        for (int row = 0; row < height; row++) {
        //            for (int column = 0; column < width; column++) {
        //                board[row][column] = false;
        //            }
        //        }

        /*
         * Ustawienie początkowe musi mieć jakieś elementy o wartości true,
         * inaczej plansza będzie cały czas pusta (ex nihilo nihili).
         */
        board[5][5] = true;
    }

    private boolean isValidCell(int row, int column) {
        return row >= 0 && row < height && column >= 0 && column < width;
    }

    /**
     * Sprawdza czy komórka będzie pusta.
     *
     * @param row
     * @param column
     * @return true jeżeli komórka będzie pusta i dostępna, false jeżeli coś w
     * niej będzie, także false jeżeli współrzędne (row, column) określają
     * lokację poza planszą
     */
    private boolean isEmptyCell(int row, int column) {
        if (isValidCell(row, column)) {
            return !board[row][column]; // ! oznacza zaprzeczenie
        }
        return false;
    }

    /**
     * Sprawdza czy komórka jest pusta.
     *
     * @param row
     * @param column
     * @return false jeżeli komórka jest pusta i dostępna, true jeżeli coś w
     * niej jest, także false jeżeli współrzędne (row, column) określają lokację
     * poza planszą
     */
    boolean isFilledCell(int row, int column) {
        if (isValidCell(row, column)) {
            return board[row][column];
        }
        return false;
    }

    /**
     * Jeden krok w ewolucji automatu komórkowego.
     */
    void evolve() {

        /*
         * W tablicy updated, którą tworzymy jako tak samo dużą jak board,
         * będziemy gromadzili nowy stan planszy bez naruszania tablicy board.
         * Po aktualizacji - zastąpimy tablicę board tablicą update.
         *
         * UWAGA, nie zadziała tu prawdłowo klonowanie tablicy
         *
         *      boolean[][] updated = board.clone();
         *
         * ponieważ clone() jest płytką kopią i robi niezupełnie to co mogłoby
         * się nam wydawać.
         */
        boolean[][] updated = new boolean[height][width];

        /*
         * Po pierwsze, najpierw wrzucamy do komórki to co było
         */
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                updated[row][column] = board[row][column];
            }
        }

        /*
         * Po drugie, jeżeli którakolwiek z sąsiednich komórek na
         * planszy jest zajęta, to komórka też będzie zajęta...
         */
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {

                // Nie musielibyśmy definiować a, b itd. - ale znakomicie
                // ułatwią one nam pracę - patrz instrukcje if poniżej.
                //
                boolean a = isFilledCell(row - 1, column);
                boolean b = isFilledCell(row + 1, column);
                boolean c = isFilledCell(row, column - 1);
                boolean d = isFilledCell(row, column + 1);

                if (a || b || c || d) {
                    updated[row][column] = true;
                }
            }
        }

        /*
         * ... chyba że (po trzecie) żadna z sąsiednich komórek nie jest
         * pusta.
         */
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {

                // Nie musielibyśmy definiować ea, eb itd. - ale znakomicie
                // ułatwią one nam pracę - patrz instrukcje if poniżej.
                //
                boolean a = isEmptyCell(row - 1, column);
                boolean b = isEmptyCell(row + 1, column);
                boolean c = isEmptyCell(row, column - 1);
                boolean d = isEmptyCell(row, column + 1);

                if (!a && !b && !c && !d) {
                    updated[row][column] = false;
                }

            }
        }

        board = updated;
    }

}
