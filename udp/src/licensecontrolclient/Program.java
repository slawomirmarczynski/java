/*
 * The MIT License
 *
 * Copyright 2022 PI_stanowisko_1.
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
package licensecontrolclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * Program informujący że został uruchomiony.
 *
 * @author Sławomir Marczyński
 */
 public class Program {

    /**
     * Sprawdzanie ważności licencji.
     *
     * Sprawdzanie ważności licencji odbywa się przez przesłanie do serwera
     * prośby o udzielenie odpowiedzi (zgody na uruchomienie programu).
     * Następnie metoda checkLicense czeka na odpowiedź z serwera, są trzy
     * możliwości: tak, nie albo brak odpowiedzi (timeout).
     *
     * @return true jeżeli serwer licencji odpowiedział pozytywnie.
     */
    static boolean checkLicense() {

        // Różne rzeczy mogą się nie udać, np. może nie być możliwe połączenie
        // z serwerem (bo serwer może nie być uruchomiony), może to lub tamto...
        // Dlatego przygotowujemy się na wyjątki - try-catch załatwi sprawę.
        try {
            // Protokół UDP przesyła bajty a nie znaki, więc aby przesłać
            // łańcuch znaków trzeba go najpierw zamienić na bajty, ewentualnie
            // wybierając odpowiednie kodowanie znaków.
            //
            // Osobną sprawą jest czy powinniśmy używać łańcuchów znaków,
            // czy też od razu tablic bajtów?

            String messageString = "Uruchomiono program";
            byte[] messageBytes = messageString.getBytes(StandardCharsets.UTF_8);

            // To tylko program przykładowy, więc adres serwera jest po prostu
            // ustalony (i to ewentualnie może tak pozostać) oraz jako serwer
            // jest użyty komputer na którym uruchamiany jest program.
            //
            // Jako numer portu używamy 80, co jest trikiem umożliwiającym
            // obejście większości firewalli. Nie jest to dobre dla docelowego
            // rozwiązania, ale do prób powinno się nadać.
            //
            // Domyślnie timeout jest równy 0, zmieniamy na 10 sekund.
            final String LICENSE_SERVER_NAME = "localhost";
            final int PORT = 80;
            final int TIMEOUT = 10000; // 10 tysięcy milisekund
            InetAddress licenseServerAddress = InetAddress.getByName(LICENSE_SERVER_NAME);
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);

            // Wysyłanie zapytania do serwera, a w zasadzie wysyłanie zapytania
            // zaadresowanego do serwera "w Internet" (serwer nie musi działać).
            //
            // W zasadzie nie jest to operacja blokująca, program spędzi w send
            // tylko tyle czasu ile to niezbędne.
            DatagramPacket request = new DatagramPacket(messageBytes, messageBytes.length, licenseServerAddress, PORT);
            socket.send(request);

            // Odbieranie odpowiedzi jest operacją blokuącą, nie zakończy się
            // dopóki odpowiedź nie przyjdzie.
            final int BUFFER_SIZE = 512;
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            String responseString = new String(buffer, 0, response.getLength(), StandardCharsets.UTF_8);

            // Jeżeli odpowiedź jest pozytywna zwracana jest wartość true.
            // Porównywanie przez quals jest prawidłowe, bo chcemy porównywać
            // zawartość
            final String goodAnswer = "yes";
            if (responseString.equals(goodAnswer)) {
                return true;
            }

        } catch (IOException ex) {
            // Nic nie rób, po prostu jeżeli jest błąd to nie ma zgody na start.
        }
        return false;
    }

    public static void main(String[] args) {

        if (checkLicense() == true) {
            System.out.println("jest zgoda, licencja jest potwierdzona");
        } else {
            System.out.println("brak zgody, licencja nie jest potwierdzona");
        }
    }

}
