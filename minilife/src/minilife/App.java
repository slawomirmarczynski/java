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

////////////////////////////////////////////////////////////////////////////////
//
// UWAGA: komentarze po dwóch ukośnikach są - w tym konkretnym projekcie
//        - przeznaczone dla uczących się programowania i omawiają rzeczy
//        zupełnie oczywiste. W "normalnych programach" nie byłoby ich wcale.
//
////////////////////////////////////////////////////////////////////////////////

/**
 * Program pokazujący jak, programując obiektowo i używając koncepcji modelu,
 * widoku i kontrolera (MVC), można zaprogramować "grę w życie" będącą dość
 * prostym automatem komórkowym.
 *
 * Opis gry w życie (life game) można znaleźć takż w Wikipedii:
 * https://pl.wikipedia.org/wiki/Gra_w_%C5%BCycie
 *
 * @author Sławomir Marczyński
 */
public class App {

    // W zasadzie nie potrzebowalibyśmy może klasy App (skrót od Application,
    // moglibyśmy użyć innej nazwy, np. Application, Program, MiniLife), jednak
    // wygodnie jest mieć coś, co reprezentuje aplikację jako taką, całościowo.
    // Takim czymś będzie obiekt klasy App, a jego utworzeniem zajmie się metoda
    // main(). Oczywiście main() musi być publiczne (tak samo App) oraz static.
    //
    /**
     * Program (aplikację) można wywołać bez parametrów, albo z parametrem
     * określającym jak duża ma być plansza.
     *
     * @param args args[0] może określać rozmiar planszy jako liczbę zapisaną
     * jako łańcuch znaków
     */
    public static void main(String[] args) {

        // Najpierw tworzymy nowy obiekt klasy App i to dostarczając jego
        // konstruktorowi parametry wywołania programu (jako tablicę łańcuchów
        // znaków). Potem uruchamiamy (cokolwiek może to znaczyć) ten obiekt
        // app wywołując jego metodę run().
        //
        // Co robi konstruktor? Co robi metoda run? A to zależy - zależy od tego
        // co my chcemy żeby robiły - przecież to my napiszemy odpowiednie
        // kawałki kodu - my określimy jak mają działać.
        //
        // Trik z tworzeniem app w ten sposób pozwala uwolnić się od problemów
        // jakie wynikałyby z programowania wewnątrz metody statycznej (takiej
        // jaką jest funkcja main). Po prostu run() jest już static.
        //
        App app = new App(args);
        app.run();
    }

    // Aplikacja z czegoś tam się składa, tzn. klasa App ma - zdefiniowane jako
    // pola ("zmienne zdefiniowane dla obiektu") komponenty - takie jak poniżej
    // options, model i view. Są one prywatne, bo nie chcemy aby coś nam popsuło
    // zabawę - np. pomyłkowe podmienienie czegoś jakoś. Jak jest private to
    // raczej tego rodzaju przypadków nie będzie.
    //
    // Moglibyśmy np. zadeklarować Model m zamiast Model model, ale choć
    // Model model itp. sposób pisania identyfikatorów wyglądają niezbyt
    // inteligentnie, to w praktyce takie nazwy doskonale się sprawdzają
    // - od razu widać co jest jakiej klasy. Małe i duże litery to nie to samo!
    //
    // Czy ma sens aby zmienne options, model i view były tutaj określone jako
    // final? Ogólnie i teoretycznie final należy stosować możliwie często,
    // jednak bez przesady - nic szczególnego tym razem nie osiągniemy.
    //
    private Options options;
    private Model model;
    private View view;

    /**
     * Konstruktor obiektów jakie są składnikami aplikacji, to jest tych z
     * których aplikacja jest (będzie po wywołaniu konstruktora) złożona.
     *
     * @param args parametry wywołania programu, takie jak w main()
     */
    App(String[] args) {
        options = new Options(args);
        model = new Model(options);
        view = new View(model);
    }

    /**
     * Metoda dokonująca rozruchu działania całej aplikacji.
     */
    private void run() {

        // Model już mamy i ten model jest zupełnie ok, ponieważ konstruktor
        // obiektu model zadziałał i dostarczył kompletny model (więc taki który
        // w pełni jest skonstruowany). Teraz rysujemy co mamy.
        //
        // Moglibyśmy zrobić to inteligentnie, tak aby każda zmiana w modelu
        // automatycznie wywoływała view.paint(). Model musiałby jednak wiedzieć
        // że ma jakiś view i być może więcej niż jeden. Zamiast tego wywołujemy
        // view.paint() po prostu na zewnąrz klasy model - model nic nie wie
        // o tym że jest jakaś obiekt view, klasa View i że coś się dzieje
        // poza nim.
        //
        view.paint();

        // Ups, to bardzo uproszczone - powinniśmy mieć jeszcze (aby było MVC)
        // obiekt klasy Controller (tj. taki który odbiera polecenia wydawane
        // przez użytkownika i modyfikuje model) - ale go nie mamy, bo na razie
        // nie jest potrzebny. A w zasadzie za prawie-kontroler służy właśnie
        // obiekt klasy app.
        //
        // Pętla for poniżej jest paskudna - wbite na sztywno 10 i ogólnie
        // powinna być zastąpiona czymś innym. Ale wystarcza na początek.
        //
        for (int i = 0; i < 10; i++) {
            model.evolve();
            view.paint();
        }

    }

}
