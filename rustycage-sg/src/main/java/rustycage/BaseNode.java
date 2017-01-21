package rustycage;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by breh on 9/9/16.
 */
public abstract class BaseNode {

    private float tx,ty;
    private float sx = 1f,sy = 1f;
    private float r;

    private float px = Float.NaN, py = Float.NaN;

    private float bl,br,bt,bb;

    private String id;

    private Matrix matrix;
    private static final Matrix IDENTITY_MATRIX = new Matrix();

    private boolean dirty;


    BaseNode() {

    }

    public @Nullable String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public final void setTranslation(float tx, float ty) {
        this.tx = tx;
        this.ty = ty;
        markDirty();
    }

    public final void setScale(float s) {
        setScale(s,s);
    }

    public final void setScale(float sx, float sy) {
        this.sx = sx;
        this.sy = sy;
        markDirty();
    }

    public final void setRotation(float r) {
        this.r = r;
        markDirty();
    }

    public void setPivot(float px, float py) {
        this.px = px;
        this.py = py;
        markDirty();
    }

    public void resetPivot() {
        this.px = Float.NaN;
        this.py = Float.NaN;
        markDirty();
    }

    public float getPivotX() {
        return px;
    }

    public float getPivotY() {
        return py;
    }


    protected float getActualPivotX() {
        if (Float.isNaN(px)) {
            return getWidth() / 2f;
        } else {
            return px;
        }
    }

    protected float getActualPivotY() {
        if (Float.isNaN(py)) {
            return getHeight() / 2f;
        } else {
            return py;
        }
    }

    public final float getTranslationX() {
        return tx;
    }

    public final float getTranslationY() {
        return ty;
    }

    public final float getScaleX() {
        return sx;
    }

    public final float getScaleY() {
        return sy;
    }

    public final float getRotation() {
        return r;
    }


    protected final void markDirty() {
        dirty = true;
    }

    protected final boolean isDirty() {
        return dirty;
    }

    public final float getLeft() {
        return bl;
    }

    public final float getRight() {
        return br;
    }

    public final float getTop() {
        return bt;
    }

    public final float getBottom() {
        return bb;
    }

    public abstract float getWidth();

    public abstract float getHeight();


    /*
    protected final void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        markDirty();
    }*/

    private void computeMatrix() {
        if (matrix == null) {
            matrix = new Matrix();
        }
        matrix.setTranslate(tx,ty);
    }

    public Matrix getMatrix() {
        if (matrix == null) {
            return IDENTITY_MATRIX;
        } else {
            return matrix;
        }
    }



    private GroupNode parent;

    public final @Nullable GroupNode getParent() {
        return parent;
    }

    final void setParent(@Nullable GroupNode parent) {
        this.parent = parent;
        markDirty();
    }


    public static abstract class Builder<B extends Builder<B,N>, N extends BaseNode> {

        private final N node;

        protected Builder(@NonNull N node) {
            this.node = node;
        }

        protected @NonNull N getNode() {
            return node;
        }

        @SuppressWarnings("unchecked")
        protected final B getBuilder() {
            return (B)this;
        }

        public final B txy(float x, float y) {
            getNode().setTranslation(x,y);
            return getBuilder();
        }

        public final B r(float r) {
            getNode().setRotation(r);
            return getBuilder();
        }

        public final B s(float s) {
            getNode().setScale(s);
            return getBuilder();
        }

        public final B id(@NonNull String id) {
            getNode().setId(id);
            return getBuilder();
        }


        public final N build() {
            return node;
        }


    }

}
