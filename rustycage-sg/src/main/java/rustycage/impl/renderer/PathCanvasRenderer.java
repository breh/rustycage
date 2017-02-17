package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import rustycage.ResolutionUnit;
import rustycage.SgPath;

/**
 * Created by breh on 2/16/17.
 */

public class PathCanvasRenderer extends ShapeCanvasRenderer<SgPath> {

    private static final String TAG = "ArcRenderer";

    @Override
    protected void renderShape(@NonNull Canvas canvas, @NonNull SgPath node, @NonNull Paint paint,
                               @Nullable ResolutionUnit resolutionUnit, @NonNull DisplayMetrics displayMetrics) {

        Path path = node.getPath();

        if (resolutionUnit != ResolutionUnit.PX) {
            Log.w(TAG, "Path supports only pixels at this moment");
            // FIXME - should transform the path based on the dimension ratio
        }

        canvas.drawPath(path, paint);
    }
}
