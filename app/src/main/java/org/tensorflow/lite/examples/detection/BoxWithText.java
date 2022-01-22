package org.tensorflow.lite.examples.detection;

import android.graphics.Rect;
import android.graphics.RectF;

public class BoxWithText {
    public String text;
    public Rect rect;

    public BoxWithText(String text, Rect rect) {
        this.text = text;
        this.rect = rect;
    }

}
