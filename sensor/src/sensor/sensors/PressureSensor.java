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
 * Sensor ciśnienia powietrza. Ok, to tylko przykład. 
 * "Prawdziwy program" mógłby w tym miejscu użyć w nazwie klasy czegoś
 * identyfikującego konkretny podzespół elektroniczny, a nawet wersję tegoż.
 */
public class PressureSensor extends Sensor {

    public PressureSensor(String name) {
        super(name);
    }

    @Override
    public void measurement() {
        
        // To jest tylko demo i dlatego metoda measurement() nie robi tego
        // co powinna robić, tylko wstawia "przypadkową wartość" (stałą, więc
        // za każdym razem tę samą) abyśmy mogli zobaczyć efekty działania
        // programu.
        //
        value = 1020.5;
    }
}
