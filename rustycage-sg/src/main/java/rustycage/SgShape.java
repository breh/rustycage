package rustycage;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.util.PaintBuilder;

/**
 * Created by breh on 9/9/16.
 */
public abstract class SgShape extends SgNode {

    private Paint paint;

    public void setPaint(@Nullable Paint paint) {
        this.paint = paint;
        invalidate();
    }

    public Paint getPaint() {
        return paint;
    }

    // FIXME - should not be public
    /*
    public void pushAttributes(@NonNull AttributesStack attributesStack) {
        if (paint != null) {
            attributesStack.push(Paint.class,paint);
        }
    }*/

    // FIXME - should not be public
    /*public void popAttributes(@NonNull AttributesStack attributesStack) {
        if (paint != null) {
            attributesStack.pop(Paint.class);
        }
    }*/


    public static abstract class Builder<B extends Builder<B,N>, N extends SgShape> extends SgNode.Builder<B,N> {

        protected Builder(@NonNull N node) {
            super(node);
        }

        public B paint(@NonNull Paint paint) {
            getNode().setPaint(paint);
            return getBuilder();
        }

        public B paint(@NonNull PaintBuilder paintBuilder) {
            getNode().setPaint(paintBuilder.build());
            return getBuilder();
        }

    }
}
