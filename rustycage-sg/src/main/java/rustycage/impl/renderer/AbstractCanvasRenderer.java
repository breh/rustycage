package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import rustycage.BaseNode;
import rustycage.impl.AttributesStack;
import rustycage.impl.FloatStack;

/**
 * Created by breh on 9/26/16.
 */
public abstract class AbstractCanvasRenderer<T extends BaseNode>  {

    protected static final Paint OPACITY_PAINT = new Paint();

    public final void render(@NonNull Canvas canvas, @NonNull T node, @NonNull FloatStack opacityStack,
                             @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        // opacity
        float newOpacity = node.getOpacity() * opacityStack.peek();
        if (newOpacity > 0f) { // only render when opacity > 0
            opacityStack.push(newOpacity);

            // matrix
            Matrix m = node.getMatrix();
            if (m.isIdentity()) {
                m = null;
            }
            if (m != null) {
                canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.concat(m);
            }

            renderNode(canvas, node, opacityStack, attributes, displayMetrics);
            if (m != null) {
                canvas.restore();
            }
            opacityStack.pop();
        }
    }

    protected abstract void renderNode(@NonNull Canvas canvas, @NonNull T node, @NonNull FloatStack opacityStack,
                                       @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics);


    protected static final @NonNull Paint applyOpacityToPaint(@Nullable Paint existingPaint, float opacity) {
        int actualAlpha = getActualAlpha(existingPaint, opacity);
        if (existingPaint == null) {
            existingPaint = OPACITY_PAINT;
        }
        existingPaint.setAlpha(actualAlpha);
        return existingPaint;
    }

    protected static final int getActualAlpha(@Nullable Paint existingPaint, float opacity) {
        int actualAlpha = 255;
        if (existingPaint != null) {
            actualAlpha = existingPaint.getAlpha();
        }
        if (opacity < 1f) {
            actualAlpha =  (int) (actualAlpha * opacity);
        }
        return actualAlpha;
    }

}
