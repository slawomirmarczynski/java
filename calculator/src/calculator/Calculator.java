/*
 * The MIT License
 *
 * Copyright 2016, 2021 Sławomir Marczyński.
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


////////////////////////////////////////////////////////////////////////////////
//
// UWAGA: komentarze po dwóch ukośnikach są - w tym konkretnym projekcie
//        - przeznaczone dla uczących się programowania i omawiają rzeczy
//        zupełnie oczywiste. W "normalnych programach" nie byłoby ich wcale.
//
////////////////////////////////////////////////////////////////////////////////


// Nazwa pakietu zasadniczo powinna być odwróconą nazwą domeny, czyli jeżeli
// mamy zarejestrowaną domenę misie.pl (i adres e-mail Jan.Kowalski@misie.pl)
// to sugerowana nazwa pakietu to pl.misie.calculator. Dlaczego? Aby nasz (ten
// który właśnie tworzymy) pakiet miał nazwę zupełnie różną niż nazwa jakiego-
// kolwiek innego pakietu na świecie. A jeżeli nie mamy własnej (albo firmowej)
// domeny? No cóż, możemy nazwać nasz pakiet w jakiś inny, sensowny sposób.
// Dobrze byłoby jednak unikać nazwy java, javax... i ogólniej nazw jakie są
// używane/zarezerwowane dla standardowych bibliotek Javy.
//
package calculator;

// Importowanie klasy Scanner dedykowanej do usprawnienia czytania tekstów
// będących ciągami znaków. Jest to duże ułatwienie, którego nie było pierwotnie
// w Javie.
//
import java.util.Scanner;

/**
 * Program demonstrujący współdziałanie klas/obiektów w języku Java: oblicznane
 * są wartości wyrażeń takich jak 2+3, -5*+6E+2, 9/0 itp.
 *
 * @author Sławomir Marczyński
 */
public class Calculator {

    /**
     * Informacje wyświetlane na konsoli wprowadzające w sposób użycia programu.
     */

    // W programowaniu obiektowym (OOP) przyjęto nazywać metodami te funkcje
    // i procedury które są definiowane dla określonej klasy i obiektów tej
    // klasy. Miało to sens w C++, w którym to języku programowania potrzebne
    // może być rozróżnienie pomiędzy "zwykłymi funkcjami" i "funkcjami jakie są
    // w klasach". (Osobną sprawą są subtelne różnice w znaczeniu słowa
    // "funkcja" w matematyce i informatyce.)
    //
    // Metoda intro() jest prywatna, bo z założenia będzie wywoływana wewnątrz
    // klasy Calculator i stosujemy zasadę minimalnego uprzywilejowania (PoLP).
    // W uproszczeniu: blokujemy wpływ intro() na to na co nie jest celem i nie
    // jest potrzebne do prawidłowego działania tejże intro().
    //
    // Metoda intro() musi być statyczna jeżeli ma być wywoływana przez
    // statyczną klasę main. To że jest statyczna nie jest niczym złym,
    // ale niesie konsekwencje i jest niekiedy krępujące: nie można używać this,
    // statyczna funkcja może używać tylko statycznych składowych klasy, są
    // problemy związane z wielowątkowością.
    //
    private static void intro() {

        // System.out jest po prostu obiektem reprezentującym strumień systemowy
        // stdout. Strumienie stderr i stdin to odpowiednio System.err oraz
        // System.in. Metoda println po prostu stara się wypisać wszystko co da
        // się jakoś przekształcić na łańcuch znaków, w tym oczywiście same
        // łańcuchy znaków też. Od metody print - czyli println-bez-ln - różni
        // ją to że po wypisaniu dodaje jeszcze znaki oznaczające koniec linii.
        //
        // Dla dociekliwych: czy można to zrobić lepiej/inaczej? Z pewnością.
        // Na przykład zamiast wywoływać czterokrotnie metodę println() można
        // byłoby od razu wypisać cały tekst. Czy znacząco usprawniłoby to
        // działanie programu? Wątpliwe. Zmiana prędkości działania byłaby
        // niezauważalna, rozmiar program też zmieniłby się nieznacznie.
        // Zaplątalibyśmy się w szczegóły które zupełnie nie są istotne dla
        // ogólnej jakości programu.
        //
        System.out.println("Kalkulator");
        System.out.println("Wpisz wyrażenie takie jak 2 + 1 albo 5.5 * 3.7");
        System.out.println("Aby zakończyć nic nie wpisuj i naciśnij enter.");
        System.out.println();
    }


    /**
     * Początek, stąd zaczyna się wykonanie programu.
     *
     * @param args argumenty wywołania programu (nie używane)
     */
    public static void main(String[] args) {

        // Dlaczego main musi być public?
        //
        // Bo musi wywołać się z zewnątrz, musi być widoczna nie tylko wewnątrz
        // pakietu w którym jest zdefiniowana.
        //
        // Dlaczego main musi być static?
        //
        // Z założenia funkcja main jest wywoływana na początku, zanim będziemy
        // mieli obiekty jakiekolwiek obiekty (nie będzie żadnego obiektu klasy
        // Calculator). Chociaż nic nie przeszkadza aby wywoływać main póżniej,
        // ale po prostu zwykle się tego nie robi, bo i po co?
        //
        // Jakie są problemy z tym że main jest static?
        //
        // Jeżeli nie zostaną utworzone obiekty to main może używać tylko tego
        // co jest statyczne (słowo kluczowe static). To niepotrzebnie wymusza
        // abyśmy nadużywali static do wyszystkiego. Zauważmy że metoda intro()
        // jest statyczna właśnie po to aby mogła być wywołana w static main().
        //
        // Ciekawostka: zamiast intro() można użyć calculator.Calculator.intro()
        // - czyli podając nazwę pakietu, nazwę klasy i nazwę statycznej metody.
        //
        intro();

        // Aby wyjść z zaklętego kręgu "statyczne w statycznych" możemy, tak jak
        // poniżej, stworzyć nowy obiekt. Niestatyczne metody tego obiektu mogą
        // być wywołane ze statycznej metody w której ten obiekt jest dostępny.
        //
        // Krótszą forma zapisu tego samego to prostu (new Calculator()).run();
        // ale tak jak jest poniżej też jest ładnie.
        //
        Calculator calculator = new Calculator();
        calculator.run();
    }

    /**
     * Metoda run mogłaby nazywać się inaczej, na przykład execute(), ale akurat
     * nazywa się run(). To w przyszłości może ułatwić przekształcenie klasy
     * Calculator w klasę zgodną z interfejsem Runnnable.
     */
    void run()
    {
        // Tworzymy obiekt klasy Scanner i wrzucamy go do zmiennej mającej nazwę
        // scanner i typ Scanner. Obiekt ten jest wyspecjalizowany w czytaniu
        // danych zapisanych jako łańcuchy znaków. Zmienna scanner jest zmienną
        // lokalną, bo nie potrzebujemy "globalnego scanera" i zupełnie nam
        // wystarcza lokalna (wewnątrz run()) definicja.
        //
        Scanner scanner = new Scanner(System.in);

        // W Javie są pętle z while, z do-while i for ("zwykła" i for-all),
        // są też instrukcje break i continue. Połączenie pętli while(true)
        // i instrukcji warunkowej z break pozwala podejmować decyzję
        // o przerwaniu iteracji w dowolnym miejscu wewnątrz pętli.
        //
        // Można zastąpić pętlę while(true) przez while(loop), gdzie loop jest
        // zmienną typu boolean równą początkowo true i w odpowiednim momencie
        // przyjmującą wartość false, a w ten sposób wyeliminować break.
        //
        while (true) {

            // Dlaczego final?
            //
            // W Javie final oznacza wartości które nie będą zmieniać swoich
            // wartości po zainicjowaniu. Czyli stałe. Sens używania final jest
            // dwojaki: po pierwsze chroni przed przypadkową zmianą, po drugie
            // pokazuje intencje programisty.
            //
            // Dlaczego nie użyć po prostu System.out.print("-->") ?
            //
            // Zdefiniowanie stałej PROMPT daje bardziej samodokumentujący się
            // kod źródłowy: zamiast wypisywać "coś" wypisywany jest PROMPT,
            // czyli coś czego znaczenie jest (być może) od razu zrozumiałe.
            //
            final String PROMPT = "--> ";
            System.out.print(PROMPT);

            // Zamiast szczegółowo analizować co wpisał użytkownik w odpowiedzi
            // po prostu czytamy całą linijkę tekstu. Zauważmy że moglibyśmy
            // w ogóle nie importować klasy Scanner i nie używać bezpośrednio
            // jej metod, ale obudować ją fasadową klasą Input (lub o zbliżonej
            // nazwie) mającą np. metodę Input.getLine(String prompt). Byłoby to
            // szczególnie opłacalne gdyby czytanie całej linii tekstu potrzebne
            // było wielokrotnie, w wielu miejscach w programie.
            //
            String line = scanner.nextLine();
            System.out.println(); // dodatkowa linia odstępu

            // Jeżeli linia jest pusta (należałoby sprawdzić dokładniej co to
            // oznacza, tj. czy np. linia tekstu zawierająca spację jest pusta)
            // to przerwać pętlę, co doprowadzi do zakończenia programu.
            //
            if (line.isEmpty()) {
                break;
            }

            // Tworzymy nowe wyrażenie - i to jest bardzo proste, bo wystarczy
            // tylko wywołać create z odpowiednim parametrem. W programowaniu
            // obiektowym chodzi właśnie o to aby - gdy mamy już obiekty - to
            // używanie obiektów było maksymalnie łatwe.
            //
            Expression expression = Expression.create(line);

            // Mając wyrażenie możemy je wypisać i wypisać jego wartość.
            // Zauważmy że i tym razem jest to bardzo proste, bo wszystko
            // co trudne jest ukryte wewnątrz klasy Expression.
            //
            // Uwaga: w tym miejscu jest finezyjny błąd - Expression.create()
            // działa zgodnie z aktywnymi ustawieniami odnośnie formatu liczb
            // (czyli używa przecinka dziesiętnego lub kropki dziesiętnej
            // zależnie od ustawionego Locale) - natomiast println() zawsze
            // wypisuje liczby z kropką dziesiętną. Czyli jeżeli znakiem ułamka
            // będzie przecinek to zachowane programu będzie niespójne - będzie
            // oczekiwał wpisywanie z przecinkiem, ale pokazywał z kropką.
            //
            // To oczywiście można naprawić używając tu metody printf()
            //
            //   System.out.printf("%s = %f\n", expression, expression.value());
            //
            // i w metodzie toString() zdefiniowanej w klasie Expression.
            //
            System.out.println("" + expression + " = " + expression.value());
        }
    }

}
