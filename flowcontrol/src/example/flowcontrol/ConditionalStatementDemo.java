package example.flowcontrol;

import java.util.Map;
import java.util.Random;

/**
 * Zastosowanie instrukcji warunkowej i wyrażenia warunkowego w języku Java.
 * <p>
 * Instrukcja warunkowa zapisywana jest przy pomocy słów kluczowych if i else,
 * można je pisać obok siebie jako else if. Zarówno if, jak i else, działają
 * na ujęty w nawiasy klamrowe {} blok instrukcji. Gdy w takim bloku jest tylko
 * jedna instrukcja można nie pisać nawiasów klamrowych. Jednak są korzyści
 * z używania nawiasów zawsze, nawet dla jednej instrukcji: wyraźniej widać
 * zakres działania if, else if i else, łatwiej dopisać coś do już istniejącego
 * kodu.
 * <p>
 * Do tworzenia wyrażeń warunkowych używa się operatora trójargumentowego.
 * Jest też w Javie instrukcja switch, w której używa się słów kluczowych
 * switch, case i break.
 * <p>
 * CC-BY-NC-ND 2023 Sławomir Marczyński
 */
public class ConditionalStatementDemo {

    public static void main(String[] args) {

        // Generowanie a jako przypadkowej liczby z przedziału od 1 do 10.
        //
        Random random = new Random();
        int a = random.nextInt(1, 10);


        // Instrukcji warunkowej zasadniczo nie pisze się w jednej linijce
        // programu, bo chociaż to jest poprawne składniowo, to nie jest
        // - w ocenie programistów używających Javy - eleganckie.
        //
        if (a > 0) print("A jest większe od zera");
        print("sprawdziliśmy to");


        // Trzeba niestety używać operatora && (koniunkcji) do sprawdzania
        // czy wartość mieści się w zakresie. Czyli poniżej sprawdzamy czy
        // zero jest mniejsze niż a i czy a jest mniejsze niż 10.
        //
        // Nawiasy klamrowe można byłoby tym razem pominąć, bo jest tylko
        // jedna instrukcja objęta przez if. Ale, nie tylko moim zdaniem,
        // lepiej zawsze używać nawiasów, bo w ten sposób program jest
        // trochę czytelniejszy i łatwiej dopisać dodatkowe instrukcje wewnątrz
        // nich. Wcięcia w Javie nie mają znaczenia takiego jak w Pythonie,
        // robi się je wyłączne dla poprawienia estetyki kodu źródłowego.
        //
        if (0 < a && a < 10) {
            print("wartość jest większa niż zero i mniejsza niż dziesięć");
        }


        // Czy można pominąć if i napisać tylko else? Tak, można.
        // Czy to jest dobry pomysł? Niekoniecznie.
        //
        if (0 < a && a < 10) {
        } else {
            print("nieprawdą jest że a jest większe niż 0 i mniejsze niż 10");
        }


        // Pokazane poniżej uciekanie if-ów w prawo...
        //
        if (a > 10) {
            print("a jest większe niż dziesięć");
        } else {
            if (a > 9) {
                print("a jest większe niż dziewięć");
            } else {
                if (a > 8) {
                    print("a jest większe niż osiem");
                } else {
                    if (a > 5) {
                        print("a jest większe niż pięć");
                    }
                }
            }
        }

        // ... praktycznie nie występuje, bo zwykle pisze się else if bez
        // nawiasu pomiędzy else i if. Nie ma ani elif, ani elseif jako słów
        // kluczowych, bo byłyby one zupełnie zbędne w Javie. Zauważmy też,
        // że udało się nam zmniejszyć liczbę bloków kodu (mniej nawiasów).
        //
        if (a > 10) {
            print("a jest większe niż dziesięć");
        } else if (a > 9) {
            print("a jest większe niż dziewięć");
        } else if (a > 8) {
            print("a jest większe niż osiem");
        } else if (a > 5) {
            print("a jest większe niż pięć");
        }

        // Jeszcze raz to samo, ale w zupełnie inaczej. Zamiast wielu
        // if tylko jeden, za to osadzony wewnątrz pętli przeglądającej
        // przygotowaną mapę z wartościami do porównywania.
        //
        // UWAGA: Map.of można używać w Java 9 i nowszych.
        //
        Map<Integer, String> levels = Map.of(10, "dziesięć", 9, "dziewięć", 8, "osiem", 5, "pięć");
        for (int value : levels.keySet()) {
            if (a > value) {
                print("a jest większe niż " + levels.get(value));
                break;
            }
        }

        // Owszem, jest instrukcja switch. Uwaga: ponieważ a jest liczbą int
        // to musi być liczbą całkowitą, nie może mieć wartości np. 5.5.
        //
        if (a > 10) {
            print("a jest większe niż dziesięć");
        } else {
            switch (a) {
                case 10:
                    print("a jest większe niż dziewięć");
                    break;
                case 9:
                    print("a jest większe niż osiem");
                    break;
                case 8, 7, 6:
                    print("a jest większe niż pięć");
                    break;
            }
        }


        // Zwykłe porównywanie liczb może niekiedy prowadzić do zadziwiających
        // efektów. Wynikać może to z błędów zaokrągleń w obliczeniach i/lub
        // porównywania z NAN, czyli symbolem nieoznaczonym not-a-number.

        double eps = 1.0E-30;
        if (1.0 + eps == 1.0) {
            print("1.0 + eps to tyle samo co 1.0");
        }

        float nan = Float.NaN;
        if (nan > 0) {
            print("większe");
        } else if (nan < 0) {
            print("mniejsze");
        } else if (nan == 0) {
            print("równe");
        } else {
            print("ani mniejsze, ani większe, ani równe zero");
        }


        // Dla liczb zmiennoprzecinkowych wskazane jest porównania
        // przeprowadzać ostrożnie, uwzględniając możliwość błędów zaokrągleń
        // i skończoną precyzję wyników.

        double value1 = 0.999999999999;
        double value2 = 1.0;
        print("value1 wynosi " + value1);
        print("value2 wynosi " + value2);
        if (value1 == value2) {
            print("value1 jest równe dokładnie value2");
        } else {
            print("value1 nie jest równe dokładnie value2");
        }

        // Prawie poprawne porównywanie liczb zmiennoprzecinkowych można
        // przeprowadzić zakładając względną i bezwzględną tolerancję.
        //
        double relTol = 1.0E-10;
        double absTol = 1.0E-10;
        double difference = Math.abs(value1 - value2);
        double averageValue = (Math.abs(value1) + Math.abs(value2)) / 2.0;
        if (difference < absTol || difference < relTol * averageValue) {
            print("value1 jest niemal równe value2");
        }


        // W Javie zmienne typów prostych (takich jak boolean, int, float,
        // double) są porównywane co do wartości jakie są w nich zapisane.
        // Obiekty, w tym także tablice, są natomiast sprawdzane czy określają
        // jeden i ten sam obiekt. Do porównywania wartości obiektów służy
        // metoda equals.
        //
        int int_1 = 2023;
        int int_2 = 2023;
        Integer obj_1 = 2023;
        Integer obj_2 = 2023;
        var array1 = new int[]{1, 2, 3};
        var array2 = new int[]{1, 2, 3};

        if (int_1 == int_2) {
            print("int_1 i int_2 są takie same");
        } else {
            print("int_1 i int_2 nie są takie same");
        }

        if (obj_1 == obj_2) {
            print("obj_1 i obj_2 są takie same");
        } else {
            print("obj_1 i obj_2 nie są takie same");
        }

        if (int_1 == obj_2) {
            print("int_1 i obj_2 są takie same");
        } else {
            print("int_1 i obj_2 nie są takie same");
        }

        if (obj_1.equals(obj_2)) {
            print("obj_1 i obj_2 mają taką samą wartość");
        } else {
            print("obj_1 i obj_2 mają różne wartości");
        }

        if (obj_2.equals(int_1)) {
            print("int_1 i obj_2 mają taką samą wartość");
        } else {
            print("int_1 i obj_2 mają różne wartości");
        }

        if (array1 == array2) {
            print("array1 i array2 są jedną i tą samą tablicą");
        } else {
            print("array1 i array2 są dwoma różnymi tablicami");
        }

        if (array1.equals(array2)) {
            print("array1 i array2 mają tę samą zawartość");
        } else {
            print("array1 i array2 mają różną zawartość");
        }

        // Może to być niebezpieczne...
        //
        Boolean result = 2 > 1;
        Boolean trueAsObject = true;
        if (result != trueAsObject) {
            print("dwa nie jest większe niż jeden");
        }

        String user_input = "exit";
        String option = "exit";
        if (user_input == option) {
            print("exit");
        } else {
            print("nie wybrano opcji");
        }


        // Zamiast instrukcji warunkowej możemy używać wyrażenia warunkowego.
        //
        // Zwróćmy uwagę że przez zastosowanie słowa kluczowego var odroczyliśmy
        // ustalenie typu zmiennej value1.

        var valueX = (int_1 > int_2) ? int_1 : "napis ćwiczebny";
        print(valueX);

        // W Javie obliczanie, od lewej do prawej, wartości wyrażeń logicznych
        // ogranicza się do tego co niezbędne aby uzyskać wynik (krótka ścieżka).
        //
        // Dlatego w poniższym przykładzie nie będzie sprawdzane czy a < 150,
        // bo już test a < -100 da ustali wynik false. (Pamiętajmy że a jest
        // liczbą losową z zakresu od 1 do 10.)

        boolean short_evaluation = a < -100 && a < 150;

        // Można to wykorzystać do skrytego warunkowego wykonania
        // instrukcji bez pisania if, np.

        var valueY = ((a > 5) && print("a > 5")) || print("a <= 5");

        // zamiast oczywistego (bardziej czytelnego)

        if (a > 5) {
            print("a > 5");
        } else {
            print("a <= 5");
        }


        // Efekt osiągany użyciem if można także osiągnąć przez użycie
        // słowa kluczowego while, chociaż nie jest to intuicyjne
        // i nie powinno być stosowane zamiast zwykłej instrukcji warunkowej:

        while (a > 5) {
            print("a jest większe niż 5");
            break;
        }

        // break można zastąpić przez return albo cokolwiek co przerwie
        // iteracje.


        // Jeszcze bardziej dziwacznym rozwiązaniem może być celowe wywołanie
        // błędu aby uzyskać warunkowe wykonanie kodu:

        try {
            int b = 1 / (a - 5);
        } catch (Exception ex) {
            print("a jest równe 5");
        }

        // Tego rodzaju triki nie są dobrą praktyką programowania.


        // Dobrą praktyką jest natomiast unikanie instrukcji warunkowych
        // tam gdzie są zbędne, przykładowo fragment:

        double y = a > 0 ? Math.cos(a) : Math.cos(-a);

        // można zastąpić przez

        y = Math.cos(Math.abs(a));

        // a najlepiej, ponieważ funkcja cosinus jest parzysta, przez

        y = Math.cos(a);


        // W niektórych przypadkach instrukcję warunkową zastępuje się asercją.

        assert a >= 0;

        // sprawdzi czy a jest większe niż zero. Jeżeli podany warunek NIE JEST
        // spełniony to zostanie zgłoszony wyjątek AssertionError i, jeżeli nie
        // przechwycimy tego wyjątku, program zostanie przerwany.
        //
        // Można użyć asercji z komentarzem:

        assert a >= 0 : "wartość a jest ujemna";

        // Drobiazg (?!) - asercje w Javie (zupełnie odwrotnie niż w Pythonie)
        // są DOMYŚLNIE WYŁĄCZONE. Dlatego nawet wpisanie czegoś tak oczywistego
        // jak poniżej nic nie da. Będzie tak jakby w ogóle asercji nie było.

        assert false;

        // Aby włączyć asercje trzeba uruchomić maszynę Javy (JVM) z opcjonalnym
        // parametrem -enableassertions (można napisać w skrócie -ea).
        //
        // Czy asercje powinny być włączone, czy wyłączone?
        //
        // David Thomas i Andrew Hunt napisali w swojej książce "Pragmatyczny
        // programista" że asercje powinny być zawsze włączone, bo pozwalają
        // wcześnie wykryć zagrożenia. Z pewnością mają rację co do konieczności
        // zawczasu sprawdzania różnych rzeczy. Zauważmy jednak gdy chcemy mieć
        // zawsze włączoną kontrolę, to naturalne jest użycie zwykłych
        // instrukcji warunkowych, a nie asercji. Asercje są pomyślane właśnie
        // jako narzędzie do dodatkowego sprawdzania działania programu podczas
        // debugowania (i być może testowania) programu.
    }

    /**
     * Metoda upraszczająca wywołanie System.out.println(obj).
     *
     * @param obj obiekt który ma być wypisany do strumienia out.
     * @return zawsze true.
     */
    static boolean print(Object obj) {
        System.out.println(obj);

        // Gdyby ta metoda print nie zwracała wartości logicznej,
        // takiej jak true lub false, to nie moglibyśmy używać jej
        // wewnątrz wyrażeń warunkowych.
        //
        return true;
    }
}
