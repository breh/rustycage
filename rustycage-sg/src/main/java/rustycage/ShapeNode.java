package rustycage;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.impl.AttributesStack;

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


    public static abstract class Builder<B extends Builder<B,N>, N extends ShapeNode> extends BaseNode.Builder<B,N> {

        protected Builder(@NonNull N node) {
            super(node);
        }

        public B paint(@NonNull Paint paint) {
            getNode().setPaint(paint);
            return getBuilder();
        }

    }
}
