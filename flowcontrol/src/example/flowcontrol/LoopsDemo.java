package example.flowcontrol;

import java.util.List;

/**
 * Zastosowanie instrukcji tworzących pętle w języku Java.
 * <p>
 * CC-BY-NC-ND 2023 Sławomir Marczyński
 */
public class LoopsDemo {
    public static void main(String[] args) {

        // Pętla for jest trywialna, taka sama jak w C++ i C#.
        // Oczywiście k++ oznacza zwiększenie wartości k o jeden.
        //
        for (int k = 1; k < 5; k++) {
            System.out.println('*');
        }


        // Wartość zmiennej kontrolnej wzrasta o +2 (patrz instrukcja k += 2),
        // a potem dodając do niej +100 przekraczamy próg przy którym pętla
        // ma zostać przerwana. Takie działanie pętli for jest typowe dla
        // C++ i Javy. W Pascalu i Pythonie pętla for działa inaczej: resetuje
        // wartość zmiennej kontrolnej.
        //
        for (int k = 4; k < 10; k += 2) {
            System.out.println(k);
            k = k + 100;
            System.out.println(k);
        }


        // Pętla for może dostarczać kolejnych elementów kolekcji takich jak listy,
        // zbiory, mapy (odwzorowania klucz-wartość) itp. a także zwykłe tablice.

        int[] arrayValues = new int[]{1, 2, 4, 8, -8, -4, -1};
        for (int value : arrayValues) {
            System.out.println(value);
        }


        // Pętla for może mieć więcej niż jedną "zmienną kontrolną".
        //
        int i, j;
        for (i = 0, j = 0; i < 5 || j > -10; i++, j = 2 * j - 1) {
            System.out.println("i = " + i + "  j = " + j);
        }


        //  Aczkolwiek pętla for-all (for z kolekcją elementów) jest bardzo
        //  naturalna, to można używać także indeksów tak jak w przykładzie
        //  poniżej.
        //
        for (int k = 0; k < arrayValues.length; k++) {
            System.out.println(arrayValues[k]);
        }

        // Można też użyć iteratorów - obiektów specjalnie służących
        // do iteracji.
        //
        var listValues = List.of(arrayValues);
        var iterator = listValues.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }


        // Instrukcja break przerywa wykonanie pętli.
        //
        for (int i = 0; i < 10; i++) {
            System.out.println("test pojedynczego break " + i);
            if (i > 5) {
                break;
            }
        }

        // Java nie ma goto, ale ma instrukcję break z etykietą.
        // Można więc przerwać kilka zagnieżdżonych pętli na raz,
        // choć nie jest to szczególnie dobre rozwiązane.
        //
        labelled_for_loop:
        for (int i1 = 0; i1 < 100; i1++) {
            for (int i2 = 0; i2 < 100; i2++) {
                System.out.println("test break z etykietą " + 1000 * i1 + i2);
                if (i1 + i2 > 15) {
                    break labelled_for_loop;
                }
            }
        }

        // Komplementarna do break jest instukcja continue. Nie przerywa ona
        // pętli, a tylko pomija dalsze wykonywanie bieżącej iteracji.
        //
        for (int k = 0; k < 10; k++) {
            if (k > 3 && k < 7) {
                continue;
            }
            System.out.println("testujemy continue");
        }

        // Lepiej jednak to samo zapisać bez continue tak jak poniżej.
        //
        for (int k = 0; k < 10; k++) {
            if (k <= 3 || k >= 7) {
                System.out.println("testujemy continue");
            }
        }


        // Pętla while działa w dość oczywisty sposób, poniżej pokazane jest
        // jak można jej użyć do oszacowania wartości "maszynowego epsilona".
        //
        double epsilon = 1.0;
        while (1.0 + epsilon > 1.0) {
            if (1.0 + epsilon / 2.0 == 1.0) {
                System.out.println("epsilon = " + epsilon);
            }
            epsilon = epsilon / 2.0;
        }

        // Jest instrukcji do-while, ale przydaje się rzadziej niż proste while.
        //
        epsilon = 1.0;
        do {
            epsilon = epsilon / 2.0;
        } while (1.0 + epsilon / 2.0 > 1.0);
        System.out.println("epsilon = " + epsilon);
    }
}
