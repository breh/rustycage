package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import rustycage.SgRectangle;
import rustycage.ResolutionUnit;

/**
 * Created by breh on 10/14/16.
 */
public class RectangleCanvasRenderer extends ShapeCanvasRenderer<SgRectangle> {

    private static final String TAG = "LineRenderer";

    @Override
    protected void renderShape(@NonNull Canvas canvas, @NonNull SgRectangle node, @NonNull Paint paint,
                               @Nullable ResolutionUnit resolutionUnit, @NonNull DisplayMetrics displayMetrics) {

        float x1 = node.getX1();
        float y1 = node.getY1();
        float x2 = node.getX2();
        float y2 = node.getY2();

        if (resolutionUnit != ResolutionUnit.PX) {
            final int typedValue = resolutionUnit.getTypedValue();
            x1 = TypedValue.applyDimension(typedValue, x1, displayMetrics);
            y1 = TypedValue.applyDimension(typedValue, y1, displayMetrics);
            x2 = TypedValue.applyDimension(typedValue, x2, displayMetrics);
            y2 = TypedValue.applyDimension(typedValue, y2, displayMetrics);
        }

        //Log.d(TAG,"rendering rectangle: "+node+" xy1:["+x1+","+y1+"], xy2:["+x2+","+y2+"], paint: "+paint);
        canvas.drawRect(x1,y1,x2,y2,paint);
    }
}
