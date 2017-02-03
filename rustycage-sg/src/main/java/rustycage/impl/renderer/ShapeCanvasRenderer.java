package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import rustycage.ResolutionUnit;
import rustycage.ShapeNode;
import rustycage.impl.AttributesStack;
import rustycage.impl.FloatStack;

/**
 * Created by breh on 10/14/16.
 */
public abstract class ShapeCanvasRenderer<S extends ShapeNode> extends AbstractCanvasRenderer<S> {

    protected final void renderNode(@NonNull Canvas canvas, @NonNull S node, @NonNull FloatStack opacityStack,
                                    @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        Paint paint = node.getPaint();
        if (paint == null) {
            paint = attributes.get(Paint.class);
        }
        float opacityValue = opacityStack.peek();
        int existingAlpha = -1;
        if (paint != null) {
            existingAlpha = paint.getAlpha();
        }
        paint = applyOpacityToPaint(paint, opacityValue);
        ResolutionUnit resolutionUnit = attributes.get(ResolutionUnit.class);
        renderShape(canvas, node, paint, resolutionUnit, displayMetrics);
        if (existingAlpha >= 0) {
            paint.setAlpha(existingAlpha);
        }

    }

    protected abstract void renderShape(@NonNull Canvas canvas, @NonNull S node, @Nullable Paint paint,
                                        @Nullable ResolutionUnit resolutionUnit, @NonNull DisplayMetrics displayMetrics);

}
