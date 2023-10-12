/*
 * The MIT License
 *
 * Copyright 2023 Sławomir Marczyński.
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
 * Demonstracja jak można zaprojektować klasę Program i stworzyć obiekt
 * program jako obiekt będący instancją tejże klasy Program. 
 * Następnie jak w programie można tworzyć inne obiekty innych klas, pokazując
 * to na przykładzie klasy HelloWorld.
 * 
 * Efektem działania programu jest wypisanie "Hello World".
 * Jest to tym razem robione zgodnie z "filozofią" obiektowego programowania:
 * jeżeli masz coś zrobić daj to do zrobienia komuś innemu. Dlatego konkretna
 * praca jest oddelegowana do oddzielnej klasy. Natomiast obiekt klasy Program
 * (przyznaję że nazwa jest mało oryginalna) służy jako miejsce w którym
 * określony jest ogólny schemat działania, bez zagłębiania się w szczegóły.
 * 
 * Jaki zysk? Modyfikując klasę HelloWorld można na przykład zamiast prostego
 * napisu pokazać okno interfejsu graficznego z tym napisem, przeczytać napis
 * przekształcając tekst na mowę, wyświetlić tłumaczenie na inny język itp. itd.
 * Wszystko to bez zmian w kodzie źródłowym klasy Program. Oczywiście można
 * zamiast modyfikacji HelloWorld użyć klasy abstrakcyjnej i dziedziczenia.
 *
 * @author Sławomir Marczyński
 */
public class Program implements Runnable {

    /**
     * Uruchamianie programu.
     *
     * Metoda main musi być statyczna, bo uruchamia się gdy jeszcze nie żadnego
     * obiektu - więc nie można wywoływać nie-statycznych metod. Fakt że to jest
     * metoda statyczna uniemożliwia np. używanie zmiennej this, odwoływanie się
     * do nie-statycznych pól klasy i może stwarzać jeszcze inne trudności.
     * Dlatego main służy zasadniczo tylko do utworzenia obiektu klasy Program i
     * uruchomienia jego metody run.
     *
     * @param args parametry wywołania programu, obecnie nie wykorzystywane, ale
     * w przyszłości mogą się do czegoś przydać, np. do wyboru L&F, itp. itd.
     */
    public static void main(String[] args) {
        Program program = new Program();
        program.run();
    }

    /**
     * Metoda run jest konieczna aby klasa Program była Runnable, tzn. miała
     * interfejs Runnable. W zasadzie wszystko jedno jak ją nazwiemy, mogłaby
     * nazywać się command albo execute, albo jeszcze inaczej. Zysk z Runnable
     * polega na tym, że jest to standard w Javie: obiekty klas implementujących
     * Runnable dają się uruchamiać jako wątki itp.
     */
    @Override
    public void run() {

        // Dlaczego użyliśmy final, dlaczego używamy text? Słowo kluczowe final
        // służy do zadeklarowania że zmienna jest stałą, czyli raz będzie miała
        // nadaną wartość, która się już potem nie może zmienić. Ogólnie chyba
        // powinniśmy używać final wszędzie gdzie się da. Użycie zmiennej text
        // ma swoje uzasadnienie czytelnością kodu i wygodą pracy z IDE.
        //
        final String text = "Hello World";
        HelloWorld helloWorld = new HelloWorld(text);

        // Skoro mamy obiekt helloWorld (uwaga na małe i wielkie litery),
        // to możemy go używać, tj. wywoływać jego metody. Zwróćmy uwagę że tu
        // gdzie jesteśmy nie interesują nas szczegóły. Chcemy widzieć ogólny
        // zarys programu.
        //
        helloWorld.shortInvitation();
        helloWorld.longInvitation();
    }
}
