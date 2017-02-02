package rustycage;

import android.support.annotation.NonNull;

/**
 * Created by breh on 9/9/16.
 */
public final class RectangleNode extends ShapeNode {


    private float x1, y1, x2, y2;

    private RectangleNode(float x1, float y1, float x2, float y2) {
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
        markDirty();
        markLocalBoundsDirty();
    }

    public void setX1(float x1) {
        this.x1 = x1;
        markDirty();
        markLocalBoundsDirty();
    }

    public void setY1(float y1) {
        this.y1 = y1;
        markDirty();
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


    public void setWidth(float width) {
        this.x2 = x1 + width;
        markDirty();
        markLocalBoundsDirty();
    }

    public void setHeight(float height) {
        this.y2 = x2 + height;
        markDirty();
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
        return new Builder(x1,y1,x2,y2);
    }

    public static Builder createWithSize(float x1, float y1, float width, float height) {
        return new Builder(x1,y1,x1+width,y1+height);
    }

    public static Builder createWithSize(float width, float height) {
        return new Builder(0,0,width,height);
    }


    public static class Builder extends ShapeNode.Builder<Builder,RectangleNode> {
        private Builder(float x1, float y1, float x2, float y2) {
            super(new RectangleNode(x1,y1,x2,y2));
        }
    }


}
