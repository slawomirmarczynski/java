/*
 * The MIT License
 *
 * Copyright 2022 Sławomir Marczyński.
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
package sensor;

// Dlaczego importujemy sensory z sensor.sensors?
//
// Java ma niezbyt przemyślane działanie słowa kluczowego protected.
// Jedynym sposobem aby protected działało podobnie jak w C++ jest umieszczenie
// hierarchii klas używających składowych chronionych (czyli protected) osobno,
// w osobnym pakiecie.
//
import sensor.sensors.*;

public class Program implements Runnable {

    // Czym jest sensors? To tablica zawierająca obiekty klasy Sensors,
    // każdy obiekt to jakiś sensor. Być może zamiast tablicy lepiej byłoby
    // użyć listy obiektów (lub innej struktury), ale w tym programie wystarczy
    // tablica. Skąd się bierze ta tablica? Patrząc na linijkę poniżej widzimy
    // że jest wynikiem wywołania createSensors() i na razie nie musimy wiedzieć
    // nic więcej.
    //
    private final Sensor[] sensors = createSensors();


    /**
     * Metoda main ma jeden cel - utworzyć i uruchomić instancję programu,
     * czyli utworzyć obiekt program i wywołać jego metodę run().
     *
     * @param args standardowy parametr funkcji main, tablica z napisami
     *            przekazywanymi przy wywołaniu programu jako argumenty
     *            wywołania programu.
     */
    public static void main(String[] args) {
        Program program = new Program();
        program.run();
    }

    // Metoda run() konieczna dla klasy implementującej interfejs Runnable.
    // Moglibyśmy zamiast run() napisać execute() lub cokolwiek innego, klasa
    // Program nie musi być Runnable. Ale skoro w Javie jest już predefiniowany
    // interfejs Runnable i skoro obiekty Runnable mogą być użyte między innymi
    // do tworzenia wątków, a środowiska IDE same generują szkielet metody run()
    // to jest w tym pewien sens.
    //
    @Override
    public void run() {

        // Włączenie wszystkich sensorów. Ale... dlaczego sensors to sensory?
        // W samej metodzie run() nie ma definicji zmiennej sensors. Definicja
        // sensors jest w klasie Program jako prywatne finalne pole sensors
        // i to od razu zainicjalizowane wywołaniem statycznej createSensors().
        // Dlatego sensors są osobno tworzone dla każdego obiektu klasy Program
        // i dlatego są, po utworzeniu, dostępne wszędzie w klasie Program.
        //
        for (Sensor sensor : sensors) {
            sensor.turnOn();
        }

        delayOneSecond();

        // Zlecenie pomiaru dla wszystkich sensorów. Zwykle realne sensory
        // wielkości fizycznych potrzebują trochę czasu na wykonanie pomiarów.
        // Zwykle nie działają tak szybko jak CPU (nawet takie jak ATMega328)
        // i dlatego lepiej nie czekać na wynik osobno dla każdego, tylko
        // zrobić to po zleceniu pomiarów wszystkim sensorom.
        //
        for (Sensor sensor : sensors) {
            sensor.measurement();
        }

        delayOneSecond();

        // Zakładamy że jedna sekunda czekania wystarczy aby mieć dobre wyniki.
        // W realnym programie czas opóźnienia musielibyśmy starannie wybrać,
        // sprawdzając karty katalogowe sensorów i być może metodą prób
        // i błędów, dodając margines bezpieczeństwa.
        //
        // Skoro mamy dobre wyniki, to możemy odczytać dane z sensorów.
        //
        for (Sensor sensor : sensors) {
            String name = sensor.getName();
            Object value = sensor.getValue();
            System.out.println("Sensor: " + name + " = " + value);
        }

        // Skoro mamy odczytane wartości, to możemy wyłączyć sensory.
        //
        for (Sensor sensor : sensors) {
            sensor.turnOff();
        }
    }

    /**
     * Co najmniej jednosekundowe opóźnienie działania wątku.
     */
    private void delayOneSecond() {
        // @todo: Tu powinno być zagwarantowane opóźnienie trwające 1 sekundę.
        //        Na razie nie ma go, aby nie gmatwać przykładu.
    }

    /**
     * Tworzenie tablicy z sensorami.
     *
     * @return tablica obiektów Sensor zawierająca dokładnie tyle obiektów
     * ile wynosi rozmiar tablicy.
     */
    private Sensor[] createSensors() {
        Sensor t = new TemperatureSensor("temperatura");
        Sensor p = new PressureSensor("ciśnienie");
        Sensor h = new HumiditySensor("wilgotność");

        // Mamy utworzone sensory, tworzymy z nich tablicę.
        //
        return new Sensor[]{t, p, h};
    }
}
