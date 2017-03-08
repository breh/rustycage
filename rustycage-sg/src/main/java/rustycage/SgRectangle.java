package rustycage;

import android.support.annotation.NonNull;

/**
 * Created by breh on 9/9/16.
 */
public final class SgRectangle extends SgShape {


    private float x1, y1, x2, y2;
    private float rx, ry;

    private SgRectangle(float x1, float y1, float x2, float y2, float rx, float ry) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.rx = rx;
        this.ry = ry;
    }


    public void setPoints(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        markLocalBoundsDirty();
    }


    public void setInnerRadii(float rx, float ry) {
        this.rx = rx;
        this.ry = ry;
        markDirty();
    }

    public void setX1(float x1) {
        this.x1 = x1;
        markLocalBoundsDirty();
    }

    public void setY1(float y1) {
        this.y1 = y1;
        markLocalBoundsDirty();
    }


    public float getX1() {
        return x1;
    }

    public float getY1() {
        return y1;
    }

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }

    public float getRx() {
        return rx;
    }

    public float getRy() {
        return ry;
    }


    public void setWidth(float width) {
        this.x2 = x1 + width;
        markLocalBoundsDirty();
    }

    public void setHeight(float height) {
        this.y2 = x2 + height;
        markLocalBoundsDirty();
    }

    @Override
    protected void computeLocalBounds(@NonNull float[] bounds) {
        bounds[0] = (x1 < x2) ? x1 : x2;
        bounds[1] = (y1 < y2) ? y1 : y2;
        bounds[2] = (x1 < x2) ? x2 : x1;
        bounds[3] = (y1 < y2) ? y2 : y1;
    }

    // builders

    public static Builder createWithPoints(float x1, float y1, float x2, float y2) {
        return new Builder(x1, y1, x2, y2, 0, 0);
    }

    public static Builder createWithPoints(float x1, float y1, float x2, float y2, float rx, float ry) {
        return new Builder(x1, y1, x2, y2, rx, ry);
    }

    public static Builder createWithSize(float x1, float y1, float width, float height) {
        return createWithSize(x1, y1, width, height, 0, 0);
    }

    public static Builder createWithSize(float x1, float y1, float width, float height, float rx, float ry) {
        return new Builder(x1, y1, x1+width, y1+height, rx, ry);
    }


    public static Builder createWithSize(float width, float height) {
        return new Builder(0, 0, width, height, 0, 0);
    }


    public static class Builder extends SgShape.Builder<Builder, SgRectangle> {
        private Builder(float x1, float y1, float x2, float y2, float rx, float ry) {
            super(new SgRectangle(x1,y1,x2,y2, rx, ry));
        }
    }


}
