package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import rustycage.SgEllipse;
import rustycage.ResolutionUnit;

/**
 * Created by breh on 1/21/17.
 */

public class EllipseCanvasRenderer extends ShapeCanvasRenderer<SgEllipse> {



    private static final String TAG = "EllipseRenderer";

    @Override
    protected void renderShape(@NonNull Canvas canvas, @NonNull SgEllipse node, @Nullable Paint paint,
                               @Nullable ResolutionUnit resolutionUnit, @NonNull DisplayMetrics displayMetrics) {
        float cx = node.getCx();
        float cy = node.getCy();
        float rx = node.getRx();
        float ry = node.getRy();

        if (resolutionUnit != ResolutionUnit.PX) {
            final int typedValue = resolutionUnit.getTypedValue();
            cx = TypedValue.applyDimension(typedValue, cx, displayMetrics);
            cy = TypedValue.applyDimension(typedValue, cy, displayMetrics);
            rx = TypedValue.applyDimension(typedValue, rx, displayMetrics);
            ry = TypedValue.applyDimension(typedValue, ry, displayMetrics);
        }

        //Log.d(TAG,"rendering ellipse: "+node+" cxy:["+cx+","+cy+"], rxy:["+rx+","+ry+"], paint: "+paint);
        // FIXME - might want to draw circle in the case rx/ry are equal - could be faster !!!
        canvas.drawOval(cx-rx, cy-ry, cx+rx, cy+ry, paint);
    }
}
