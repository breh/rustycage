package rustycage;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.util.PaintBuilder;

/**
 * A node representing a geometric shape. A shape node can have "paint" assigned,
 * which allows specifying paint attributes such as foreground and background colors, etc ..
 *
 *
 * Created by breh on 9/9/16.
 */
public abstract class SgShape extends SgNode {

    private Paint paint;

    /**
     * Sets paint to this shape
     *
     * FIXME - shoudl switch to "immutable" SgPaint ...
     * @param paint
     */
    public void setPaint(@Nullable Paint paint) {
        this.paint = paint;
        invalidate();
    }

    /**
     * Gets paint of this shape
     * @return
     */
    public Paint getPaint() {
        return paint;
    }


    /**
     * Abstract shape builder
     * @param <B>
     * @param <N>
     */
    public static abstract class Builder<B extends Builder<B,N>, N extends SgShape> extends SgNode.Builder<B,N> {

        protected Builder(@NonNull N node) {
            super(node);
        }

        /**
         * Sets paint on this shape
         * @param paint
         * @return
         */
        public B paint(@NonNull Paint paint) {
            getNode().setPaint(paint);
            return getBuilder();
        }

        /**
         * Sets paint builder on this shape
         * @param paintBuilder
         * @return
         */
        public B paint(@NonNull PaintBuilder paintBuilder) {
            getNode().setPaint(paintBuilder.build());
            return getBuilder();
        }

    }
}
