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

public class YAxis extends Axis {

    double offset;
    double length;

    public int valueToPixel(double value) {
        return (int) Math.round(-(value - min) / (max - min) * length + offset);
    }

    @Override
    public void paint(Graphics graphics, int xOffset, int yOffset, int width, int height) {

        this.offset = yOffset;
        this.length = height;

        final FontMetrics metrics = graphics.getFontMetrics();
        final int fontHeight = metrics.getHeight();
        final int ascent = metrics.getAscent();
        final int leading = metrics.getLeading();
        final int labelWidth = metrics.stringWidth(label);
        final String formatString = "%." + decimalDigits + "f";
        double value;

        graphics.drawLine(xOffset, yOffset, xOffset, yOffset - height);

        final int xPivot = fontHeight + leading;
        final int yPivot = yOffset - (height - labelWidth) / 2;
        Graphics2D rotated_graphics = (Graphics2D) graphics.create();
        rotated_graphics.rotate(Math.toRadians(-90.0), xPivot, yPivot);
        rotated_graphics.drawString(label,
                xPivot,
                yPivot);

        value = min + minorStep;
        graphics.setColor(Color.LIGHT_GRAY);
        while (value <= max) {
            int q = valueToPixel(value);
            graphics.drawLine(xOffset, q, xOffset + width, q);
            value += minorStep;
        }
        value = min + majorStep;
        graphics.setColor(Color.GRAY);
        while (value <= max) {
            int q = valueToPixel(value);
            graphics.drawLine(xOffset, q, xOffset + width, q);
            value += majorStep;
        }

        graphics.setColor(Color.BLACK);
        value = min;
        while (value <= max) {
            int q = valueToPixel(value);
            graphics.drawLine(xOffset, q, xOffset - MINOR_TICK_SIZE, q);
            value += minorStep;
        }
        value = min;
        while (value <= max) {
            int q = valueToPixel(value);
            graphics.drawLine(xOffset, q, xOffset - MAJOR_TICK_SIZE, q);
            String tickLabel = String.format(formatString, value);
            int stringWidth = metrics.stringWidth(tickLabel);
            graphics.drawString(tickLabel, xOffset - stringWidth - MAJOR_TICK_SIZE, q + ascent / 2);
            value += majorStep;
        }

    }

}
