package rustycage;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by breh on 9/9/16.
 */
public abstract class BaseNode {

    private static final String TAG = "BaseNode";

    private float tx,ty;
    private float sx = 1f,sy = 1f;
    private float r;

    private float px = Float.NaN, py = Float.NaN;

    private String id;

    private Matrix matrix;

    private boolean dirty;


    BaseNode() {

    }

    public @Nullable String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public final void setTranslationX(float tx) {
        this.tx = tx;
        markDirty();
        markMatrixDirty();
    }

    public final void setTranslationY(float ty) {
        this.ty = ty;
        markDirty();
        markMatrixDirty();
    }


    public final void setTranslation(float tx, float ty) {
        this.tx = tx;
        this.ty = ty;
        markDirty();
        markMatrixDirty();
    }

    public final void setScale(float s) {
        setScale(s,s);
    }

    public final void setScale(float sx, float sy) {
        this.sx = sx;
        this.sy = sy;
        markDirty();
        markMatrixDirty();
    }

    public final void setRotation(float r) {
        this.r = r;
        markDirty();
        markMatrixDirty();
    }

    public void setPivot(float px, float py) {
        this.px = px;
        this.py = py;
        markDirty();
        markMatrixDirty();
    }

    public void resetPivot() {
        this.px = Float.NaN;
        this.py = Float.NaN;
        markDirty();
        markMatrixDirty();
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
        if (!dirty) {
            dirty = true;
            if (parent != null) {
                if (!parent.isDirty()) {
                    parent.markDirty();
                }
            }
            onMarkedDirty();
        }
    }

    void onMarkedDirty() {
    }


    void clearDirty() {
        if (dirty) {
            dirty = false;
        }
    }


    protected final boolean isDirty() {
        return dirty;
    }

    public abstract float getLeft();

    public abstract float getRight();

    public abstract float getTop();

    public abstract float getBottom();

    // size

    public abstract float getWidth();

    public abstract float getHeight();


    private void markMatrixDirty() {
        matrix = null;
    }

    private Matrix computeMatrix() {
        if (tx == 0 && ty == 0 && r == 0 && sx == 1 && sy == 1) {
            return null; // no matrix needed
        } // else
        Matrix m = new Matrix();
        if (tx != 0 || ty != 0) {
            m.postTranslate(tx, ty);
        }
        return m;
    }

    public Matrix getMatrix() {
        if (matrix == null) {
            matrix = computeMatrix();
        }
        return matrix;
    }



    private BaseNode parent;

    public final @Nullable BaseNode getParent() {
        return parent;
    }

    final void setParent(@Nullable BaseNode parent) {
        this.parent = parent;
        markMatrixDirty();;
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



