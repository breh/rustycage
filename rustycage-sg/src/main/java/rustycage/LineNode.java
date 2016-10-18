package rustycage;

/**
 * Created by breh on 9/9/16.
 */
public final class LineNode extends ShapeNode {


    private float x1, y1, x2,y2;

    public LineNode(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public LineNode(float x2, float y2) {
        this(0,0,x2,y2);
    }

    public void setPoints(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
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


    public void setX2(float x2) {
        this.x2 = x2;
        markDirty();
    }

    public void setY2(float y2) {
        this.y2 = y2;
        markDirty();
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
    public float getWidth() {
        return Math.abs(x2 - x1);
    }

    @Override
    public float getHeight() {
        return Math.abs(y2 - y1);
    }
}
