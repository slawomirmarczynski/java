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
 * Różne parametry są dostarczane do programu jako łańcuchy znaków, ale to klasa
 * Options obsługuje dostarczanie ich jako odpowiednich wartości, także i wtedy
 * gdy są to wartości domyślne.
 *
 * @author Sławomir Marczyński
 */
class Options {

    // Jeżeli pola (fields) takie jak poniżej width i height są final, to można
    // bezpiecznie udostępniać je nawet jako publiczne - najgorszą rzeczą jaka
    // może się zdarzyć to to że ktoś przeczyta ich zawartość. Zmodyfikować ich
    // się nie da, bo są final. Więc nikt niczego nie popsuje.
    //
    // Przy takim podejściu aby zmienić np. szerokość (width) konieczne będzie
    // po prostu utworzenie od nowa nowego obiektu klasy Options. Nic trudnego.
    //
    final int width;   /* szerokość planszy */
    final int height;  /* wysokość planszy */

    /**
     * Konstruktor tworzący opcje (obiekt klasy Options) z dostarczonych mu
     * parametrów wywołania programu.
     *
     * @param args
     */
    Options(String[] args) {

        // Tymczasowe, prowizoryczne rozwiązanie to np.:
        //
        //      width = 10;
        //      height = 10;
        //
        // i coś takiego zadziała. Poniżej jest jednak znacznie lepsza wersja
        // - prawie taka jaka mogłaby być docelowo.
        // DEFAULT_WIDTH i DEFAULT_HEIGHT są wyłącznie po to aby kod źródłowy
        // był bardziej czytelny, samodokumentujący się.
        //
        final int DEFAULT_WIDTH = 20;
        final int DEFAULT_HEIGHT = 20;

        // Teraz mamy trochę gimnastyki, bo musi się nam udać już za pierwszym
        // razem podstawić odpowiednie wartości do width i height obiektu (są
        // final, więc w konstruktorze raz możemy wpisać coś do nich, ale tylko
        // jeden jedyny raz). Eleganckim rozwiązaniem jest użycie dodatkowych
        // zmiennych lokalnych proposedWidth i proposedHeight tak jak poniżej.
        //
        // Najpierw wpisujemy do nich wartości domyślne (moglibyśmy od razu 10,
        // ale wtedy kod nie byłby samodokumentujący się, tzn. nie widać byłoby
        // co oznacza te "magiczne" 10). W ten sposób nieważne co będzie potem,
        // ale coś już mamy w proposedWidth i proposedHeight.
        //
        int proposedWidth = DEFAULT_WIDTH;
        int proposedHeight = DEFAULT_HEIGHT;

        // Można byłoby nie dawać instrukcji if wierząc iż try-catch rozwiąże
        // wszystkie problemy w jednolity sposób (sięgnięcie po nieistniejący
        // element tablicy args też wygeneruje wyjątek). Ale brak danych w args
        // prawdopodobnie będzie bardzo częsty i trudno go uznawać za sytuację
        // wyjątkową, więc spychanie obsługi tego do konstrukcji try-catch jest
        // trochę nie w stylu Javy.
        //
        if (args.length > 0) {
            try {

                // Łamiemy zasadę DRY (nie powtarzaj się) po to aby łatwiej było
                // wprowadzać ewentualne modyfikacje: każda (z dwóch poniżej)
                // linijka bierze jakiś argument i wyciąga sobie z niego co
                // trzeba, a to że akurat width i height robią to samo... cóż
                // to w przyszłych wersjach może się zmienić, np. width może
                // wyciągać dane z args[0], a height z args[1].
                //
                // Zauważmy, że być może lepiej byłoby utworzyć niezależne
                // bloki try-catch dla każdego parametru - to że nam się raz
                // nie udało nie musi oznaczać że (gdy height będzie z args[1])
                // drugi raz też się nie uda.
                //
                proposedWidth = Integer.parseUnsignedInt(args[0]);
                proposedHeight = Integer.parseUnsignedInt(args[0]);

            } catch (NumberFormatException exception) {

                // Różnego rodzaju "dobre pomysły", których realizację odkładamy
                // na później, oznacza się zwykle znacznikiem @todo tak jak
                // to jest poniżej pokazane.
                //
                // @todo: zamiast uciszać wyjątek lepiej byłoby jakoś
                //        poinformować użytkownika że podał złe parametry, np:
                //
                // System.err.println("Złe parametry startowe programu.");
            }
        }

        // Tu przepisujemy proposedWidth i proposedHeight do "globalnych
        // w klasie" width i height. Możemy to zrobić, choć tylko raz, bo final.
        //
        width = proposedWidth;
        height = proposedHeight;
    }

    // Jeżeli ktoś doczytał do tego miejsca, to być może zauważył, że realnie
    // kod źródłowy (pomijając komentarze) jest krótki.
    //
    // I tak powinno być - klasa obiektów nie powinna być bardzo skomplikowana
    // - nie chcemy dużo pisać, progam ma działać dzięki przemyślanej
    // architekturze, dobrze rozdzielonym kompetencjom klas i współpracy
    // wzajemnej obiektów.
}
