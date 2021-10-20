# calculator

Program ukazujący ideę programowania obiektowego (OOP).

W programowaniu obiektowym podstawowym budulcem programu są ogólnie obiekty i klasy obiektów.
Zamiast *po prostu pisać* program, jako ciąg linijek z rozkazami do wykonania przez komputer, cel osiągamy dzięki współpracy obiektów.
Efektywne programowania wymagać przy tym będzie aby każdy obiekt miał dobrze określone kompetencje i porządnie zdefiniowane przeznaczenie.

Celem programu calculator jest obliczanie wyrażeń takich jak 2 + 3 (w wyniku powinniśmy dostać 5, ewentualnie 5.0). To mało ambitny cel,
jednakże w istocie zależy nam nie na obliczeniu że dwa plus 3 jest równe 5, ale na pokazaniu jak można rozdzielić zadania do wykonania pomiędzy
różne obiekty różnych klas. W programie są dwie klasy: Calculator i Expression.

## Klasa Calculator

Klasa *Calculator* odpowiada za uruchomienie całego programu. W jej statycznej metodzie *main()* tworzony jest obiekt klasy Calculator, a następnie
wywoływana jest metoda *run()* tego obiektu. Jest to sposób na przejście od metod statycznych do "normalnych" metod, tj. metod nie-statycznych.
W tej metodzie *run()* są, wywołaniami fabryki *Expression.create()*, tworzone obiekty klasy *Expression*. Używamy także jawnie metody *value()*
oraz niejawnie metody *toString()* klasy *Expression*.

## Klasa Expression

Klasa *Expression* nic nie wie o programie jako takim, jedyne co potrafi (prawie dobrze, bo mogłaby lepiej) tworzyć nowe obiekty *Expression*,
które mogą przedstawiać wyrażenie w czytelnej dla człowieka postaci, obliczać i podawać obliczoną wartość wyrażenia. W ten sposób szczegóły (jak
liczyć wartość wyrażenia) są oddzielone od rzeczy bardziej ogólnych (jak uruchomić program). W ten sposób też, jeżeli kiedyś będziemy chcieli
ulepszyć działania na wyrażeniach reprezentowanych przez *Expression*, będziemy mogli skupić się na jednej klasie w programie nie naruszając tego
co robią inne (takie jak klasa Calculator).
