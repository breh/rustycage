package rustycage;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import rustycage.impl.Bounds;

/**
 * Created by breh on 9/9/16.
 */
public abstract class BaseNode {

    private static final String TAG = "BaseNode";

    // FIXME - all these fields need memory optimization

    private float tx,ty;
    private float sx = 1f,sy = 1f;
    private float r;

    private float px = Float.NaN, py = Float.NaN;

    private String id;

    private Matrix matrix;
    private static final Matrix IDENTITY_MATRIX = new Matrix();

    private float[] localBounds = new float[4];
    private float[] transformedBounds;


    private boolean dirty = true;
    private boolean localBoundsDirty = true;
    private boolean transformedBoundsDirty = true;

    private float opacity = 1f;

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
        markTransformedBoundsDirty();
    }

    public final void setTranslationY(float ty) {
        this.ty = ty;
        markTransformedBoundsDirty();
    }


    public final void setTranslation(float tx, float ty) {
        this.tx = tx;
        this.ty = ty;
        markTransformedBoundsDirty();
    }

    public final void setScale(float s) {
        setScale(s,s);
    }

    public final void setScale(float sx, float sy) {
        this.sx = sx;
        this.sy = sy;
        markTransformedBoundsDirty();
    }

    public final void setRotation(float r) {
        this.r = r;
        markTransformedBoundsDirty();
    }

    public void setPivot(float px, float py) {
        this.px = px;
        this.py = py;
        markTransformedBoundsDirty();
    }

    public void resetPivot() {
        this.px = Float.NaN;
        this.py = Float.NaN;
        markTransformedBoundsDirty();
    }

    public float getPivotX() {
        return px;
    }

    public float getPivotY() {
        return py;
    }


    protected float getActualPivotX() {
        if (Float.isNaN(px)) {
            return (getRight() + getLeft()) / 2f;
        } else {
            return px;
        }
    }

    protected float getActualPivotY() {
        if (Float.isNaN(py)) {
            return (getBottom() + getTop()) / 2f;
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


    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        if (opacity > 1f) opacity = 1f;
        if (opacity < 0f) opacity = 0f;
        this.opacity = opacity;
        markDirty();
    }


    /**
     * Marks item dirty. Typically not used by a library user, only when things like ShapeNode paint
     * object is updated / animated (there is no listener which would allow to listen on
     * changes to the paint object)
     */
    public final void markDirty() {
        if (!dirty) {
            dirty = true;
            if (parent != null) {
                parent.markDirty();
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

    public final float getLeft() {
        refreshLocalBoundsIfNeeded();
        return localBounds[0];
    }

    public final float getRight() {
        refreshLocalBoundsIfNeeded();
        return localBounds[2];
    }

    public final float getTop() {
        refreshLocalBoundsIfNeeded();
        return localBounds[1];
    }

    public final float getBottom() {
        refreshLocalBoundsIfNeeded();
        return localBounds[3];
    }

    public final @NonNull Bounds getLocalBounds() {
        return getLocalBounds(null);
    }


    public final @NonNull Bounds getLocalBounds(@Nullable Bounds bounds) {
        if (bounds == null) {
            bounds = new Bounds();
        }
        refreshLocalBoundsIfNeeded();
        bounds.set(localBounds);
        return bounds;
    }


    protected final void markLocalBoundsDirty() {
        localBoundsDirty = true;
        markTransformedBoundsDirty();
        markDirty();
    }


    private void refreshLocalBoundsIfNeeded() {
        if (localBoundsDirty) {
            computeLocalBounds(localBounds);
            localBoundsDirty = false;
        }
    }

    protected abstract void computeLocalBounds(@NonNull float[] bounds);


    public final float getTransformedLeft() {
        refreshTransformedBoundsIfNeeded();
        if (transformedBounds != null) {
            return transformedBounds[0];
        } else {
            return getLeft();
        }
    }

    public final float getTransformedRight() {
        refreshTransformedBoundsIfNeeded();
        if (transformedBounds != null) {
            return transformedBounds[2];
        } else {
            return getRight();
        }
    }

    public final float getTransformedTop() {
        refreshTransformedBoundsIfNeeded();
        if (transformedBounds != null) {
            return transformedBounds[1];
        } else {
            return getTop();
        }
    }

    public final float getTransformedBottom() {
        refreshTransformedBoundsIfNeeded();
        if (transformedBounds != null) {
            return transformedBounds[3];
        } else {
            return getBottom();
        }
    }


    protected final void markTransformedBoundsDirty() {
        matrix = null;
        transformedBoundsDirty = true;
        markDirty();
    }


    public final @NonNull Bounds getTransformedBounds() {
        return getTransformedBounds(null);
    }

    public final @NonNull Bounds getTransformedBounds(@Nullable Bounds bounds) {
        if (bounds == null) {
            bounds = new Bounds();
        }
        refreshTransformedBoundsIfNeeded();
        if (transformedBounds != null) {
            bounds.set(transformedBounds);
        } else {
            bounds.set(localBounds);
        }
        return bounds;
    }


    private void refreshTransformedBoundsIfNeeded() {
        if (transformedBoundsDirty) {
            transformedBounds = computeTransformedBounds(localBounds, getMatrix(), transformedBounds);
            transformedBoundsDirty = false;
        }
    }


    private  static float[] computeTransformedBounds(@NonNull float[] localBounds, @NonNull Matrix matrix, @Nullable float[] transformedBounds) {
        if (! matrix.isIdentity()) {
            if (transformedBounds == null) {
                transformedBounds = new float[4];
            }
            matrix.mapPoints(transformedBounds, localBounds);
        } else {
            if (transformedBounds != null) {
                transformedBounds = null;
            }
        }
        return transformedBounds;
    }


    // size

    public  final float getWidth() {
        return Math.abs(getRight() - getLeft());
    }

    public final float getHeight() {
        return Math.abs(getBottom() - getTop());
    }



    protected final boolean isMatrixDirty() {
        return matrix == null;
    }

    private Matrix computeMatrix() {
        if (tx == 0 && ty == 0 && r == 0 && sx == 1 && sy == 1) {
            return IDENTITY_MATRIX;
        } // else
        Matrix m = new Matrix();
        if (sx != 1 || sy != 1) {
            m.postScale(sx, sy, getActualPivotX(), getActualPivotY());
        }
        if (r != 0) {
            m.postRotate(r, getActualPivotX(), getActualPivotY());
        }
        if (tx != 0 || ty != 0) {
            m.postTranslate(tx, ty);
        }
        return m;
    }

    public final Matrix getMatrix() {
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
        markTransformedBoundsDirty();;
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

        public final B opacity(float opacity) {
            getNode().setOpacity(opacity);
            return getBuilder();
        }


        public final N build() {
            return node;
        }


    }

}



