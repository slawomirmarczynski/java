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

public class XAxis extends Axis {

    double offset;
    double length;

    public int valueToPixel(double value) {
        return (int) Math.round((value - min) / (max - min) * length + offset);
    }

    @Override
    public void paint(Graphics graphics, int xOffset, int yOffset, int width, int height) {

        this.offset = xOffset;
        this.length = width;

        final FontMetrics metrics = graphics.getFontMetrics();
        final int fontHeight = metrics.getHeight();
        final int ascent = metrics.getAscent();
        final int leading = metrics.getLeading();
        final int labelWidth = metrics.stringWidth(label);
        final String formatString = "%." + decimalDigits + "f";
        double value;

        value = min + minorStep;
        graphics.setColor(Color.LIGHT_GRAY);
        while (value <= max) {
            int p = valueToPixel(value);
            graphics.drawLine(p, yOffset, p, yOffset - height);
            value += minorStep;
        }
        graphics.setColor(Color.GRAY);
        value = min + majorStep;
        while (value <= max) {
            int p = valueToPixel(value);
            graphics.drawLine(p, yOffset, p, yOffset - height);
            value += majorStep;
        }

        graphics.setColor(Color.BLACK);
        value = min;
        while (value <= max) {
            int p = valueToPixel(value);
            graphics.drawLine(p, yOffset, p, yOffset + MINOR_TICK_SIZE);
            value += minorStep;
        }
        value = min;
        while (value <= max) {
            int p = valueToPixel(value);
            graphics.drawLine(p, yOffset, p, yOffset + MAJOR_TICK_SIZE);
            String tickLabel = String.format(formatString, value);
            int stringWidth = metrics.stringWidth(tickLabel);
            graphics.drawString(tickLabel, p - stringWidth / 2, yOffset + MAJOR_TICK_SIZE + ascent);
            value += majorStep;
        }

        graphics.drawLine(xOffset, yOffset, xOffset + width, yOffset);
        graphics.drawString(label,
                xOffset + (width - labelWidth) / 2,
                yOffset + fontHeight + ascent + leading + MAJOR_TICK_SIZE);
    }
}
