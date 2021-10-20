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
package calculator;

import java.util.Scanner;

/**
 * Klasa, której obiekty mogą reprezentować wyrażenie rozumiane jako dwie liczby
 * rozdzielone operatorem. Uwaga: jest bardzo uproszczona, aby być w pełni
 * użyteczna powinna być znacznie bardziej skomplikowana.
 *
 * @author Sławomir Marczyński
 */
class Expression {

    // Dlaczego a, b i operand są private final?
    //
    // Private bo są tylko "do użytku wewnętrznego" przez klasę Expression
    // i dlatego celowo zrobiliśmy je niewidocznymi z zewnątrz. Mogłoby się
    // wydawać że to złośliwość wobec programistów piszących inne fragmenty
    // programu... nie mogą zajrzeć do ani do a, ani do b, ani do operand.
    // W rzeczywistości jest to udogodnienie - bo jeżeli w ten sposób
    // określiliśmy a, b i operand jako "szczegóły jakie są nieistotne na
    // zewnąrz klasy" to: uwalniamy innych programistów od myślenia co oznaczają
    // a, b i operand; dajemy sobie szansę na zmianę nazw, albo nawet usunięcie
    // tych zmiennych z programu bez konsekwencji mogących powstać w innych
    // częściach programu.
    //
    // Dlaczego final? Bo wartości te pozostają niezmienne (po ustaleniu ich
    // wartości w konstruktorze) i choć możnaby final pominąć, to final jest
    // przyjętym sposobem zwrócenia uwagi na ten fakt.
    private final double a;
    private final double b;
    private final String operand;

    // Prywatny konstruktor (z parametrami) klasy Expression. Jeżeli nie byłoby
    // żadnego konstruktora to automatycznie jest przyjmowane że istnieje
    // publiczny bezparametrowy konstruktor domyślny. Czyli po pierwsze
    // blokujemy możliwość tworzenia nowych wyrażeń jako new Expression().
    // Po drugie - bo konstruktor jest prywatny - nie będzie możliwości przez
    // przypadek wywołać tego konstruktora.
    //
    // I teraz ktoś dociekliwy mógłby zapytać: w jaki sposób będą tworzone
    // obiekty klasy Expression skoro nie ma żadnego publicznego konstruktora?
    // Rozwiązeniem jest użycie statycznej metody create jako fabryki obiektów.
    //
    // Jeszcze jedna ciekawostka - nie jest złym pomysłem dokumentowanie
    // składowych prywatnych - ale można tego nie robić, bo i tak nie są one
    // częścią interfejsu klasy, czyli i tak nikt "z zewnątrz" ich nie
    // potrzebuje. I dlatego tym razem nie ma komentarza "z gwiazdkami" /** */
    //
    private Expression(double a, String operand, double b) {

        // Mamy póla (dostępnye wewnątrz klasy zmienne) i nazwy parametrów.
        // Czy mogą one być jednakowe? Tak. Ale aby je rozróżnić trzeba dopisać
        // przed nazwami pól słowo this. W ten sposób instrukcją this.a = a;
        // zapisujemy w polu a wartość parametru a, przy tym nie potrzebujemy
        // sztucznie zmieniać nazw.
        //
        this.a = a;
        this.operand = operand;
        this.b = b;
    }

    /**
     * Fabryka obiektów Expression.
     *
     * @param string łańcuch znaków zawierający wyrażenie
     * @return wyrażenie jako obiekt Expression
     */
    static Expression create(String string) {

        // Jeżeli popatrzymy na Calculator.java to znajdziemy tam obiekt
        // klasy Scanner nazywający się scanner. Co absolutnie nam nie
        // przeszkadza aby tu, czyli w innym miejscu programu, utworzyć sobie
        // inny obiekt klasy Scanner. W jednym programie może być wiele obiektów
        // tego samego typu, a każdy z nich zachowuje odrębność i może robić
        // coś innego, choć zgodnie ze specjalizacją klasy. Tym razem obiekt
        // nie nazywa się scanner (choć mógłby), bo raczej trudno coś pomylić
        // w dwóch linijkach. Krótka nazwa jest wystarczająca.

        Scanner sc = new Scanner(string);
        return new Expression(sc.nextDouble(), sc.next(), sc.nextDouble());

        // W zasadzie to już było return, więc formalnie nigdy tu nie będziemy.
        // Ale zastanówmy się co stanie się po return? Zmienna sc jest zmienną
        // lokalną, więc przestanie istnieć. Skoro nie istnieje sc to obiekt
        // który był przechowywany za pomocą sc przestanie być potrzebny.
        // W językach takich jak C++ taki obiekt trzeba usunąć "ręcznie"
        // z pamięci. W Javie takie obiekty są automatycznie przeznaczane do
        // "odśmiecenia", czyli umieszczane w spisie niepotrzebnych nieużytków.
        // Od czasu do czasu aktywowany jest mechanizm tzw. śmieciarki, a wtedy
        // te obiekty które mają być usunięte są usuwane. Dowcip w tym że nie
        // mamy pewności kiedy się to stanie, bo cały proces odśmiecania jest
        // automatyczny i nie potrzebuje (zwykle) dodatkowej obsługi. Tak jest
        // wygodnie. Skutkiem tego jest niemożliwość używania tzw. destruktorów
        // w Javie. W Javie nie ma destruktorów takich jakie są w C++.
    }

    /**
     * Przedstawia wyrażenie, jako łańcuch znaków, w postaci czytelnej dla
     * człowieka.
     *
     * @return łańcuch znaków, taki jak np. "1+2"
     */
    @Override
    public String toString() {
        return "" + a + operand + b;
    }

    /**
     * Zwraca wartość wyrażenia jako liczbę podwójnej precyzji (czyli double).
     * @return
     */
    double value() {

        // Dlaczego na początku wpisujemy do result zero? Moglibyśmy tego nie
        // robić, ale kompilator protestuje wtedy że możliwa jest (z jego punktu
        // widzenia) sytuacja w której zwracana wartość będzie nieokreślona.
        //
        double result = 0;

        switch (operand) {
            case "*":
                result = a * b;
                break;
            case "/":
                result = a / b;
                break;
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;

            // Trochę problematyczne: bo niby dlaczego gdy wyrażenie nie pasuje
            // do wyrażonych oczekiwań nie potraktować tego jako błędu i zgłosić
            // wyjątek? Zamiast tego problem jest wygaszany, a value() zwróci
            // zero jako wynik.
            //
            default:
                break;
        }

        return result;
    }
}
