package rustycage;

import android.support.annotation.NonNull;

/**
 * Created by breh on 2/10/17.
 */

public final class ArcNode extends ShapeNode {


    private float left, top, right, bottom;
    private float startAngle, sweepAngle;
    private boolean useCenter = true;


    private ArcNode(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
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



    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
        markDirty();
    }


    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
        markDirty();
    }


    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
        markDirty();
    }


    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
        markDirty();
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
        markDirty();
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        markDirty();
    }

    public boolean isUseCenter() {
        return useCenter;
    }

    public void setUseCenter(boolean useCenter) {
        this.useCenter = useCenter;
        markDirty();
    }

    public static Builder create(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
        return new Builder(left, top, right, bottom, startAngle, sweepAngle);
    }


    public static class Builder extends ShapeNode.Builder<ArcNode.Builder,ArcNode> {
        private Builder(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
            super(new ArcNode(left, top, right, bottom, startAngle, sweepAngle));
        }
    }

}

