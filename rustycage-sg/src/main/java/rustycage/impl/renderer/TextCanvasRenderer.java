package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import rustycage.ResolutionUnit;
import rustycage.SgText;
import rustycage.impl.AttributesStack;
import rustycage.impl.FloatStack;

/**
 * Renderer for {@link SgText}
 *
 * Created by breh on 1/23/17.
 */

public class TextCanvasRenderer extends AbstractCanvasRenderer<SgText> {

    private static final String TAG = "TextRenderer";

    @Override
    protected void renderNode(@NonNull Canvas canvas, @NonNull SgText node, @NonNull FloatStack opacityStack,
                              @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
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

                float opacityValue = opacityStack.peek();
                int originalAlpha = textPaint.getAlpha();
                if (opacityValue < 1f) {
                    int actualAlpha = getActualAlpha(textPaint, opacityValue);
                    textPaint.setAlpha(actualAlpha);
                }

                canvas.drawText(text, 0, text.length(), x, y, textPaint);

                textPaint.setAlpha(originalAlpha);
            }
        }
    }
}
