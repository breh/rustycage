package rustycage;

import android.support.annotation.NonNull;

/**
 * Created by breh on 2/10/17.
 */

public final class SgArc extends SgShape {


    private float left, top, right, bottom;
    private float startAngle, sweepAngle;
    private boolean useCenter = true;


    private SgArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
    }

    @Override
    protected void computeLocalBounds(@NonNull float[] bounds) {
        bounds[0] = left;
        bounds[1] = top;
        bounds[2] = right;
        bounds[3] = bottom;
    }


    @Override
    boolean isPointInHitTarget(@NonNull float[] point) {
        // FIXME - need to compute if the point is within arc
        return true;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
        invalidate();
    }


    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
        invalidate();
    }


    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
        invalidate();
    }


    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
        invalidate();
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    public boolean isUseCenter() {
        return useCenter;
    }

    public void setUseCenter(boolean useCenter) {
        this.useCenter = useCenter;
        invalidate();
    }

    public static Builder create(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
        return new Builder(left, top, right, bottom, startAngle, sweepAngle);
    }


    public static class Builder extends SgShape.Builder<SgArc.Builder, SgArc> {
        private Builder(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
            super(new SgArc(left, top, right, bottom, startAngle, sweepAngle));
        }
    }

}

