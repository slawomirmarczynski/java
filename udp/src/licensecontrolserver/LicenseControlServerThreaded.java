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
package licensecontrolserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Serwer do sprawdzanie ważności licencji.
 *
 * Sprawdzanie ważności licencji odbywa się przez przesłanie do serwera prośby o
 * udzielenie odpowiedzi (zgody na uruchomienie programu).
 *
 * @author Sławomir Marczyński
 */
public class LicenseControlServerThreaded implements Runnable {

    // Uwaga: to uproszczona wersja, ale tym razem otwierany jest wątek roboczy
    // - bo jednocześnie musimy obsługiwać zgłoszenia przez Internet (klientów)
    // oraz sprawdzać czy program serwera ma nadal pracować.
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread workingThread = new Thread(new LicenseControlServerThreaded());
        workingThread.start();
        System.out.print("press enter to stop server ");
        new Scanner(System.in).hasNextLine();
        workingThread.interrupt();
    }

    @Override
    public void run() {

        // Różne rzeczy mogą się zdarzyć, a ponieważ run() będzie w osobnym
        // wątku to konieczne jest załatwienie (w tym osobnym wątku) wszelkich
        // kłopotów w obrębie samego run().

        try {

            // Numer portu i wielkość bufora mają być takie same tu i w programach
            // które będą się pytały o ważność swojej licencji. Chociaż obecnie
            // program serwera i programy będące klientami serwera są po prostu
            // odrębnymi projektami, to niemal równie łatwo byłoby połączyć je
            // w jeden (i to akurat nie jest dobry pomysł) jak i stworzyć
            // współdzieloną klasę "opakowującą" numer portu, długość bufora, sposób
            // kodowania danych - czyli to co musi być takie samo w obu programach.
            //
            int PORT = 80;
            DatagramSocket socket = new DatagramSocket(PORT);
            final int BUFFER_SIZE = 512;
            byte[] buffer = new byte[BUFFER_SIZE];

            // Bardzo prymitywne rozwiązanie - niekończąca się pętla while - nadaje
            // się tylko jako przykład - bo aby zakończyć program trzeba będzie
            // zrobić to "ręcznie" za pomocą systemu - kill/terminate.
            //
            // Ustawienie time-out dla gniazda socket jest konieczne, bo jeżeli
            // jest wartość domyślna (czyli 0) to receive() poniżej będzie
            // czekać bez końca, czyli pętla się zablokuje i nieskuteczne będzie
            // sprawdzanie warunku while, tzn. czy wątek nie jest zatrzymany.
            //
            // Uwaga: gdzieś kiedyś ktoś może poznał metodę stop() służącą do
            // zatrzymywania wątków w Javie; owszem była, ale obecnie nie stosuje
            // się jej, bo jest przestarzała.
            //
            socket.setSoTimeout(100);
            while (!Thread.interrupted()) {

                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                // Adres clientAddress wraz z numerem portu clientPort jest adresem
                // IP pod którym nadawca datagramu (jaki tu odbieramy) czeka na
                // odpowiedź.
                //
                // Uwaga: nie ma gwarancji że to co przyjdzie do nas przez sieć,
                //        czyli po prostu z Internetu, to jest naprawdę to co miało
                //        przyjść... każdy może próbować wysyłać nam różne rzeczy.
                //
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();
                String messageString = new String(buffer, 0, request.getLength(), StandardCharsets.UTF_8);
                System.out.println(clientAddress.getHostName() + ":" + clientPort + " -- " + messageString);

                // Prymitywne i niezbyt skuteczne rozwiązanie: gdy serwer zgadza się
                // na uruchomienie programu (licencja jest ważna) to odsyłamy yes,
                // gdy serwer odmawia uruchomienia to informuje o tym wysyłając no.
                //
                // Oczywiście serwer może decyzję zgodzie uzależniać np. od już
                // uruchomionych instancji programu (np. sprawdzać czy program
                // z danym kluczem/id nie jest już uruchomiony więcej niż założoną
                // liczbę razy).
                //
                String responseString = true ? "yes" : "no";
                byte[] responseBytes = responseString.getBytes(StandardCharsets.UTF_8);
                DatagramPacket response = new DatagramPacket(responseBytes, responseBytes.length,
                        clientAddress, clientPort);
                socket.send(response);
            }
        } catch (IOException ex) {

        }

    }
}
