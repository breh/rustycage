package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class TextCanvasRenderer extends ShapeCanvasRenderer<SgText> {

    private static final String TAG = "TextRenderer";

    @Override
    protected void renderShape(@NonNull Canvas canvas, @NonNull SgText node, @Nullable Paint paint,
                               @Nullable ResolutionUnit resolutionUnit, @NonNull DisplayMetrics displayMetrics) {
        CharSequence text = node.getText();
        if (text != null) {

            float x = node.getX();
            float y = node.getY();
            if (resolutionUnit != ResolutionUnit.PX) {
                final int typedValue = resolutionUnit.getTypedValue();
                x = TypedValue.applyDimension(typedValue, x, displayMetrics);
                y = TypedValue.applyDimension(typedValue, y, displayMetrics);
            }

            canvas.drawText(text, 0, text.length(), x, y, paint);

        }
    }
}
