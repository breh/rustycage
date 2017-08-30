package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import rustycage.SgNode;
import rustycage.impl.AttributesStack;
import rustycage.impl.FloatStack;

/**
 * Abstract renderer for the scene graph nodes. This render uses
 * {@link Canvas} to paint the scene graph. It should be possible to switch
 * to other rendering environments if desired in the future
 *
 * Created by breh on 9/26/16.
 */
public abstract class AbstractCanvasRenderer<T extends SgNode>  {

    protected static final Paint OPACITY_PAINT = new Paint();

    protected static final Matrix IDENTITY_MATRIX = new Matrix();

    public final void render(@NonNull Canvas canvas, @NonNull T node, @NonNull FloatStack opacityStack,
                             @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        // opacity
        float newOpacity = node.getOpacity() * opacityStack.peek();
        if (newOpacity > 0f) { // only render when opacity > 0
            opacityStack.push(newOpacity);

            // matrix
            Matrix m = node.getMatrix();
            if (m!= null && m.isIdentity()) {
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

    /**
     * Abstract method used to render a node
     * @param canvas - canvas where to render
     * @param node - which node to render
     * @param opacityStack - opacity stack for current opacity values
     * @param attributeStack - attribute stack for othe attributes
     * @param displayMetrics - display matrics
     */
    protected abstract void renderNode(@NonNull Canvas canvas, @NonNull T node, @NonNull FloatStack opacityStack,
                                       @NonNull AttributesStack attributeStack, @NonNull DisplayMetrics displayMetrics);


    /**
     * Helper for applying specified opacity to given paint as alpha
     * @param existingPaint
     * @param opacity
     * @return
     */
    protected static final @NonNull Paint applyOpacityToPaint(@Nullable Paint existingPaint, float opacity) {
        int actualAlpha = getActualAlpha(existingPaint, opacity);
        if (existingPaint == null) {
            existingPaint = OPACITY_PAINT;
        }
        existingPaint.setAlpha(actualAlpha);
        return existingPaint;
    }

    /**
     * Gets the alpha value based on current paint and given opacity
     * @param existingPaint
     * @param opacity
     * @return
     */
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
