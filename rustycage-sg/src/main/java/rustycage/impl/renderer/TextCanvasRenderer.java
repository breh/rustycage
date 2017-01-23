package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import rustycage.ResolutionUnit;
import rustycage.TextNode;
import rustycage.impl.AttributesStack;

/**
 * Created by breh on 1/23/17.
 */

public class TextCanvasRenderer extends AbstractCanvasRenderer<TextNode> {

    private static final String TAG = "TextRenderer";

    @Override
    public void render(@NonNull Canvas canvas, @NonNull TextNode node, @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        CharSequence text = node.getText();
        if (text != null) {
            Paint textPaint = node.getTextPaint();
            if (textPaint == null) {
                textPaint = attributes.get(Paint.class);
            }
            if (textPaint != null) {
                //Log.d(TAG,"rendering text: "+node+" text:["+text+"]");
                float x = node.getX();
                float y = node.getY();
                ResolutionUnit resolutionUnit = attributes.get(ResolutionUnit.class);
                if (resolutionUnit != ResolutionUnit.PX) {
                    final int typedValue = resolutionUnit.getTypedValue();
                    x = TypedValue.applyDimension(typedValue, x, displayMetrics);
                    y = TypedValue.applyDimension(typedValue, y, displayMetrics);
                }

                canvas.drawText(text, 0, text.length(), x, y, textPaint);
            }
        }
    }
}
