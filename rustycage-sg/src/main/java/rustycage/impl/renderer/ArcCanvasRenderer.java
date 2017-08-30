package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import rustycage.SgArc;
import rustycage.ResolutionUnit;

/**
 * Renderer for {@link SgArc} node
 * Created by breh on 10/14/16.
 */
public class ArcCanvasRenderer extends ShapeCanvasRenderer<SgArc> {

    private static final String TAG = "ArcRenderer";

    @Override
    protected void renderShape(@NonNull Canvas canvas, @NonNull SgArc node, @NonNull Paint paint,
                               @Nullable ResolutionUnit resolutionUnit, @NonNull DisplayMetrics displayMetrics) {

        float left = node.getLeft();
        float right = node.getRight();
        float top = node.getTop();
        float bottom = node.getBottom();
        float startAngle = node.getStartAngle();
        float sweepAngle=  node.getSweepAngle();
        boolean useCenter = node.isUseCenter();

        if (resolutionUnit != ResolutionUnit.PX) {
            final int typedValue = resolutionUnit.getTypedValue();
            left = TypedValue.applyDimension(typedValue, left, displayMetrics);
            right = TypedValue.applyDimension(typedValue, right, displayMetrics);
            top = TypedValue.applyDimension(typedValue, top, displayMetrics);
            bottom = TypedValue.applyDimension(typedValue, bottom, displayMetrics);
        }

        canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, useCenter, paint);
    }
}
