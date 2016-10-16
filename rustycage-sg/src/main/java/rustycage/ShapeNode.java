package rustycage;

import android.graphics.Paint;
import android.support.annotation.Nullable;

/**
 * Created by breh on 9/9/16.
 */
public abstract class ShapeNode extends BaseNode {

    private Paint paint;

    public void setPaint(@Nullable Paint paint) {
        this.paint = paint;
        markDirty();
    }

    public Paint getPaint() {
        return paint;
    }
}
