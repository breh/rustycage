package rustycage;

import android.graphics.Matrix;
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

    private float width, height;

    private Matrix matrix;
    private static final Matrix IDENTIY_MATRIX = new Matrix();

    private boolean dirty;


    BaseNode() {

    }

    public final void translate(float tx, float ty) {
        this.tx = tx;
        this.ty = ty;
        markDirty();
    }

    public final void scale(float s) {
        scale(s,s);
    }

    public final void scale(float sx, float sy) {
        this.sx = sx;
        this.sy = sy;
        markDirty();
    }

    public final void rotate(float r) {
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
            return width / 2f;
        } else {
            return px;
        }
    }

    protected float getActualPivotY() {
        if (Float.isNaN(py)) {
            return height / 2f;
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

    public final float getWidth() {
        return br-bl;
    }
    public final float getHeight() {
        return bb-bt;
    }


    protected final void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        markDirty();
    }

    protected final void setWidth(float width) {
        this.width = width;
        markDirty();
    }

    protected final void setHeight(float height) {
        this.height = height;
        markDirty();
    }

    private void computeMatrix() {
        if (matrix == null) {
            matrix = new Matrix();
        }
        matrix.setTranslate(tx,ty);
    }

    public Matrix getMatrix() {
        if (matrix == null) {
            return IDENTIY_MATRIX;
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

}
