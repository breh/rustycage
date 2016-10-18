package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import rustycage.LineNode;
import rustycage.ResolutionUnit;
import rustycage.impl.AttributesStack;

/**
 * Created by breh on 10/14/16.
 */
public class LineCanvasRenderer extends ShapeCanvasRenderer<LineNode> {

    private static final String TAG = "LineRenderer";

    @Override
    public void renderShape(@NonNull Canvas canvas, @NonNull LineNode node, @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        Paint paint = attributes.get(Paint.class);

        float x1 = node.getX1();
        float y1 = node.getX1();
        float x2 = node.getX2();
        float y2 = node.getY2();

        ResolutionUnit resolutionUnit = attributes.get(ResolutionUnit.class);
        if (resolutionUnit != ResolutionUnit.PX) {
            final int typedValue = resolutionUnit.getTypedValue();
            x1 = TypedValue.applyDimension(typedValue, x1, displayMetrics);
            y1 = TypedValue.applyDimension(typedValue, y1, displayMetrics);
            x2 = TypedValue.applyDimension(typedValue, x2, displayMetrics);
            y2 = TypedValue.applyDimension(typedValue, y2, displayMetrics);
        }

        Log.d(TAG,"rendering line: "+node+" w:"+node.getX2()+", h:"+node.getY2()+", paint: "+paint);
        canvas.drawLine(x1,y1,x2,y2,paint);
    }
}
