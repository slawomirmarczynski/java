/*
 * The MIT License
 *
 * Copyright 2020 Sławomir Marczyński.
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
package helloworld;

/**
 * Klasa, której obiekty potrafią tylko jedno: wypisywać przygotowany tekst
 * na dwa sposoby. Zauważmy że moglibyśmy ją zmodyfikować (użyć dziedziczenia)
 * tak aby w jakimś sensie dalej wypisywała tekst, ale robiła to w jakiś inny,
 * szczególny sposób. Taka modyfikacja nie powinna wymagać modyfikacji programu,
 * będąc ograniczoną tylko do jednej klasy.
 *
 * @author Sławomir Marczyński
 */
public class HelloWorld {

    // Po co text i dlaczego private? Zmienna text jest widoczna dla wszystkich
    // funkcji wewnątrz obiektu. W ten sposób obiekt pamięta co ma wypisać.
    // Ponieważ jest private, to z nie ma dostępu z zewnątrz, więc wartość text
    // nie zmieni się przez przypadkowe użycie. Zauważmy że brak jest static
    // - a to oznacza że zmienne tekst są tworzone, za każdym razem jedna, gdy
    // są tworzone obiekty klasy HelloWorld. Gdyby było static, to oznaczałoby
    // że text jest zmienną przywiązaną do specyfikacji klasy, współdzieloną
    // przez wszystkie obiekty klasy HelloWorld.
    //
    private String text;

    /**
     * Konstruktor, tworzy nowy obiekt klasy HelloWorld.
     * 
     * @param text tekst jaki ma być użyty w powitaniach.
     */
    public HelloWorld(String text) {
        this.text = text;
    }

    /**
     * Krótka forma. Tylko tekst powitania i nic więcej.
     */
    public void shortInvitation() {
        System.out.println(text);
    }

    /**
     * Długa forma.
     * 
     * Tekst powitania jest wykorzystany kreatywnie.
     */
    public void longInvitation() {

        // Ciekawostka: Java w obecnej wersji wykorzystuje UTF-8; starsze wersje
        // mogą mieć z tym trudności, bo używały UTF-16 jako kodowania kodu
        // źródłowego. Ostatecznie to co zobaczymy zależy też od tego jak radzi
        // sobie z kodami znaków okno konsoli, na które są wyprowadzane wyniki.
        //
        for (int i = 0; i < 3; i++) {
            System.out.print(text + " ♡ ");
        }
        System.out.println();
    }
}
