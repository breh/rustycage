package rustycage;

import android.support.annotation.NonNull;

/**
 * Created by breh on 9/9/16.
 */
public final class SgLine extends SgShape {


    private float x1, y1, x2,y2;

    private SgLine(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }


    public void setPoints(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        markLocalBoundsDirty();
    }

    public void setX1(float x1) {
        this.x1 = x1;
        markLocalBoundsDirty();
    }

    public void setY1(float y1) {
        this.y1 = y1;
        markLocalBoundsDirty();
    }


    public void setX2(float x2) {
        this.x2 = x2;
        markLocalBoundsDirty();
    }

    public void setY2(float y2) {
        this.y2 = y2;
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

    @Override
    protected void computeLocalBounds(@NonNull float[] bounds) {
        bounds[0] = (x1 < x2) ? x1 : x2;
        bounds[1] = (y1 < y2) ? y1 : y2;
        bounds[2] = (x1 < x2) ? x2 : x1;
        bounds[3] = (y1 < y2) ? y2 : y1;
    }

    // Builders

    public static Builder createWithPoints(float x1, float y1, float x2, float y2) {
        return new Builder(x1,y1,x2,y2);
    }

    public static Builder createWithSize(float x1, float y1, float width, float height) {
        return new Builder(x1,y1,x1+width,y1+height);
    }


    public static class Builder extends SgShape.Builder<Builder, SgLine> {
        private Builder(float x1, float y1, float x2, float y2) {
            super(new SgLine(x1,y1,x2,y2));
        }
    }
}
