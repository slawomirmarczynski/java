/*
 * The MIT License
 *
 * Copyright (c) 2023 Sławomir Marczyński.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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

package swingplot;

import java.awt.*;

/**
 * Axis jest abstrakcją reprezentującą oś liczbową, bez określania jeszcze czy
 * jest to os odciętych, czy oś rzędnych.
 */
public abstract class Axis {

    final static int MAJOR_TICK_SIZE = 10;
    final static int MINOR_TICK_SIZE = 5;

    protected double min = 0.;
    protected double max = 10.;
    protected double majorStep = (max - min) / 4.0;
    protected double minorStep = majorStep / 10.0;
    protected int decimalDigits = 2;

    protected String label = ""; // lepiej dać pusty niż null

    public abstract int valueToPixel(double value);

    public abstract void paint(Graphics graphics, int xOffset, int yOffset, int width, int height);

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMajorStep(double majorStep) {
        this.majorStep = majorStep;
    }

    public void setMinorStep(double minorStep) {
        this.minorStep = minorStep;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
