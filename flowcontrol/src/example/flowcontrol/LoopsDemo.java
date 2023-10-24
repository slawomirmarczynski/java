package example.flowcontrol;

/**
 * Zastosowanie instrukcji tworzących pętle w języku Java. -- work in progress
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

        // Wartość zmiennej kontrolnej jest powiększana o +2
        // przez k += 2, a następnie o +100 wywołanie k = k + 100.

        for (int k = 4; k < 10; k += 2) {
            System.out.println(k);
            k = k + 100;
            System.out.println(k);
        }
    }
}
