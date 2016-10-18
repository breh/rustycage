package rustycage;

/**
 * Created by breh on 9/9/16.
 */
public final class RectangleNode extends ShapeNode {


    private float x1, y1, x2, y2;

    public RectangleNode(float x1, float y1, float width, float height) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x1 + width;
        this.y2 = x2 + height;
    }

    public RectangleNode(float x2, float y2) {
        this(0,0,x2,y2);
    }

    public void set(float x1, float y1, float width, float height) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x1 + width;
        this.y2 = x2 + height;
        markDirty();
    }

    public void setX1(float x1) {
        this.x1 = x1;
        markDirty();
    }

    public void setY1(float y1) {
        this.y1 = y1;
        markDirty();
    }


    public void setWidth(float width) {
        this.x2 = x1 + width;
        markDirty();
    }

    public void setHeight(float height) {
        this.y2 = x2 + height;
        markDirty();
    }

    public float getWidth() {
        return Math.abs(x2 - x1);
    }

    public float getHeight() {
        return Math.abs(y2 - y1);
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


}
