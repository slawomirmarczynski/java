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
package sensor.sensors;

/**
 * Klasa Sensor jest abstrakcją reprezentującą "jakiś sensor", bez ograniczania
 * się do konkretnego modelu czy producenta. Ma to dwie korzyści: określa API
 * i daje polimorfizm. Po prostu nie musimy (gdy to jest niepotrzebne) wiedzieć
 * z jakimi konkretnymi sensorami pracuje nasz program, wystarczy że są one
 * zgodne z założeniami jakie tu właśnie sformułujemy. Podobny rezultat można
 * osiągnąć stosując nie-abstrakcyjną klasę jako bazę, ale użycie abstrakcyjnej
 * jest wygodniejsze. W języku Java niekiedy korzystne jest zamiast klasy użyć
 * interfejsu (słowo kluczowe interface) - nie ma dziedziczenia wielobazowego.
 */
public abstract class Sensor {

    // Dlaczego name jest prywatne, a value jest protected?
    //
    // Zmienna name jest nazwą sensora i jest ustalana w konstruktorze (patrz
    // poniżej). Nie ma - taki jest nasz zamysł - żadnej możliwości aby ją
    // później zmienić i dlatego jest final. Wykorzystywana będzie jedynie
    // w metodzie getName(), która sama też jest final, więc nie będzie można
    // jej zmodyfikować. Nie ma więc sensu aby subklasy, takie jak na przykład
    // TemperatureSensor, miały dostęp do name. To typowe rozwiązanie, API
    // ma "gettera" (po polsku to akcesor), ale nie najbardziej eleganckie.
    //
    // Można po prostu zadeklarować name jako public final, a także pozbyć się
    // getName(). Dostęp do nazwy będzie nadal możliwy, po prostu będzie ona
    // publiczna. I tak samo nie będzie można zmienić nazwy, bo będzie final,
    // co uczyni ją read-only, czyli tylko do odczytu. Z drugiej strony
    // programiści używający Javy są tak przyzwyczajeni do "getterów", że być
    // może łatwiej będzie im używać getName() niż po prostu name.
    //
    // Zmienna value jest chroniona i jest typu Object, czyli może reprezentować
    // cokolwiek co jest obiektem. Zakładamy że ma ona przechowywać albo wartość
    // ostatniego aktualnie pomiaru, albo null gdy nic nie jest zmierzone.
    // Zakładamy też że każda subklasa klasy Sensor, każda na swój sposób, musi
    // zadbać aby wartość zmiennej value była aktualizowana. A to oznacza
    // że każda subklasa musi mieć dostęp do value, więc value nie może być 
    // prywatna. Z drugiej strony value nie powinno być modyfikowane przez
    // cokolwiek co nie jest sensorem, więc nie może być public. Odpada też
    // "domyślny dostęp" (taki jaki w Javie jest), bo oznacza to że value byłoby
    // niedostępne dla subklas klasy Sensor definiowanych poza pakietem sensors.
    // Dlatego właśnie jest protected. Pech w tym że protected daje dostęp
    // do value wszystkim klasom wewnątrz pakietu sensor.sensors. Dlatego
    // w pakiecie sensor.sensors są tylko obiekty będące sensorami i nic więcej.
    // To nadal nie jest idealne rozwiązanie, bo możliwe jest że ktoś stworzy
    // pakiet o takiej samej nazwie, tj. sensor.sensors, i w ten sposób zepsuje
    // nasze rozwiązanie. Można próbować temu zapobiec, ale to pociąga za sobą
    // dalszą komplikację programu.
    //
    private final String name;
    protected Object value = null;

    /**
     * Konstruktor klasy abstrakcyjnej, nie służy do samodzielnego tworzenia
     * obiektów klasy Sensor (bo ta jest abstrakcyjna), lecz do wywołania gdy
     * konstruktor subklasy będzie tworzył obiekt konkretny.
     * 
     * @param name nazwa czujnika.
     */
    Sensor(String name) {
        this.name = name;
    }

    /**
     * Włączenie czujnika.
     * 
     * Koncepcja jest taka, że czujnik może (choć nie musi) być wyłączany
     * i włączany, np. dla oszczędzania energii. Jak to konkretnie będzie
     * określi się w subklasach. Dlaczego więc turnOn() nie jest metodą
     * abstrakcyjną? Ułatwiamy sobie życie w przypadku gdyby czujnik był
     * na stałe włączony, bez możliwości programowego włączania/wyłączania:
     * zamiast pisać odpowiednie implementacje turnOn() i turnOff() możemy
     * wtedy po prostu nic nie robić, bo "puste" ich wersje będą domyślne.
     */
    public void turnOn() {
    }

    /**
     * Wyłączenie czujnika, patrz też turnOn().
     */
    public void turnOff() {
    }

    /**
     * Pomiar tego co ma mierzyć czujnik. Ta metoda jest najbardziej tajemnicza.
     * 
     * Po prostu absolutnie nie wiemy, w tej chwili, co takiego ma w niej być.
     * Wiemy tylko co ma robić - zapoczątkować proces pomiaru - pomiaru który
     * po pewnym czasie się skończy i wtedy zmienna value powinna przyjąć 
     * wartość różną od null. Dlaczego tak? Większość czujników itp. pracuje
     * znacznie wolniej niż CPU, są problemy z synchronizacją i oczekiwaniem
     * na zakończenie pomiarów. Z drugiej strony korzystne może być wymuszenie
     * równoległej pracy. Tysiąc czujników potrzebujących setnej sekundy
     * na pomiar każdy to opóźnienie 10 sekund gdy będziemy używali ich kolejno,
     * ale teoretycznie tylko jedna setna sekundy gdy będą pracowały równolegle.
     * 
     * Problemem jest także i to w jaki sposób "dogadamy się" z czujnikami.
     * Java nie pozwala na łatwy bezpośredni dostęp do hardware, Wirtualna
     * Maszyna Javy (JVM) skutecznie na to nie pozwala. Można to jednak obejść
     * poprzez Java Native Interface (JNI) i podobne techniki. Tu nie będziemy
     * tego tematu rozwijać, po prostu zakładamy że będzie możliwe wydawanie
     * poleceń i otrzymywanie danych z czujników. W praktyce istnieją biblioteki
     * pozwalające na tego typu operacje np. dla Raspberry Pi.
     */
    public abstract void measurement();

    /**
     * Metoda zwracająca nazwę czujnika.
     * 
     * Można byłoby wprost użyć name (zadeklarowane jako final), ale wtedy
     * programiści używający (sub)klasy Sensor mogliby nie skojarzyć że jest
     * to coś co udostępnia nazwę. Mogliby myśleć że w name trzeba wpisać
     * nazwę czy jakoś tak. Dlatego chyba lepiej zostawić getName().
     * 
     * @return nazwa czujnika jako łańcuch znaków.
     */
    public final String getName() {
        return name;
    }

    /**
     * Metoda zwracająca wynik pomiaru lub null gdy nie ma (jeszcze) wyniku.
     * 
     * Dlaczego nie jest final? Mogłaby być. Ale nie chcemy zablokować sobie
     * możliwości całkowitego nadpisania jej "nową lepszą wersją" realizującą
     * zupełnie inny pomysł (np. obliczającą średnią z kilku ostatnich pomiarów,
     * dostarczającej wartości maksymalnej, ...)
     * 
     * @return wynik pomiaru jako Object, czyli jakiś obiekt (można ustalić
     * jaki operatorem instanceof).
     */
    public Object getValue() {
        return value;
    }
}
