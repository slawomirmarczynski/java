# Po co dziedziczenie w OOP?

## Co chcemy zrobić?

Ideą programowania obiektowego jest tworzenie *reusable code*, czyli pisanie
programu tak, aby to co napiszemy miało trwałą przydatność w wielu programach.

Dlatego podstawową zasadą jest nie przepisywanie i modyfikowanie napisanego kodu
klas obiektów, ale tworzenie nowych subklas z użyciem dziedziczenia. Temu też
sprzyja umieszczanie klas w odrębnych plikach.

Przykładowy problemem do rozwiązania jest stworzenie programu odczytującego dane
z czujników temperatury, ciśnienia powietrza i wilgotności. Czyli typowych
czujników stacji pogodowej. Przewidujemy przyszłą rozbudowę o dodatkowe
czujniki, np. siły i kierunku wiatru, opadów deszczu itp. Być może dałoby się
zastosowane rozwiązania użyć także do zupełni innych czujników, takich jakie
mogłyby być w robocie przemysłowym lub dronie.

## Co chcemy teraz stworzyć? 

Jakiś szkielet architektury programu pozwalający względnie łatwo dodawać obsługę
nowych czujników. Taki, że kod napisany dla obsługi czujnika będzie można
wykorzystać łatwo w każdym innym programie.

Pokazane tu rozwiązanie jest więc raczej szkicem niż dopracowanym programem.
Zwłaszcza że nie wyjaśnia jak komunikować się, stosując 
*Java Native Interface* (JNI), z poziomu Javy z hardware osiągalnym np. przez
magistralę I2C. Chodzi nam raczej o ogólne zapoznanie się z podstawowymi
technikami programowania obiektowego.

## Jak chcemy to zrobić?

Program jako taki jest w pliku *Program.java*, w którym jest definicja klasy
*Program*. Statyczna metoda *main* tworzy jeden obiekt klasy *Program* i oddaje
mu sterowanie wywołując metodę *run*. To że klasa *Program* implementuje
interfejs *Runnable* nie ma istotnego znaczenia, ale nie przeszkadza, a być może
kiedyś mogłoby do czegoś się przydać.

Klasa *Program* widzi czujniki jako obiekty abstrakcyjnej klasy *Sensor*.
W ten sposób wszystkie czujniki są obsługiwane w zestandaryzowany sposób.
Nie musimy zastanawiać się nad tym jaki konkretnie czujnik obsługujemy,
klasa *Program* tego nie musi wiedzieć.

Niemniej jednak czujniki są konkretne, więc aby np. stworzyć reprezentację
czujnika temperatury używamy konkretnej (nie-abstrakcyjnej) klasy
*TemperatureSensor*. Analogicznie dla czujników ciśnienia i wilgotności.

Dzięki dziedziczeniu (słowo kluczowe ***extends***) nie tylko oszczędzamy
na konieczności copy-paste tego co już zostało zrobione w superklasie
(inaczej nazywanej klasą bazową), ale również mamy gwarancję zgodności API klas.

Oczywiście w "prawdziwym programie" powinniśmy jeszcze dopracować pewne
szczegóły. Na przykład subklasy mogłyby być przeznaczone do obsługi
konkretnego modelu czujnika konkretnego producenta. Zmiana dostawcy/producenta
czujników polegałby więc nie na zmianie istniejącego kodu, ale na dodaniu
jeszcze jednej klasy i drobnych zmianach w konfiguracji. Moglibyśmy zrobić to
tak, aby dodawanie czujników możliwe było przez wybór typu czujnika z listy
i następnie automatyczne pobranie odpowiedniego pliku z Internetu.
