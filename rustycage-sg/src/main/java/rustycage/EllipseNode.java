package rustycage;

import android.graphics.drawable.shapes.Shape;

/**
 * Created by breh on 1/21/17.
 */

public class EllipseNode extends ShapeNode {


    private float cx, cy, rx, ry;

    private EllipseNode(float cx, float cy, float rx, float ry) {
        this.cx = cx;
        this.cy = cy;
        this.rx = rx;
        this.ry = ry;
    }

    @Override
    public float getWidth() {
        return rx * 2;
    }

    @Override
    public float getHeight() {
        return ry * 2;
    }

    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public float getRx() {
        return rx;
    }

    public float getRy() {
        return ry;
    }


    public static Builder createCircle(float cx, float cy, float r) {
        return new Builder(cx,cy,r,r);
    }

    public static Builder createEllipse(float cx, float cy, float rx, float ry) {
        return new Builder(cx,cy,rx,ry);
    }


    public static class Builder extends ShapeNode.Builder<Builder,EllipseNode> {
        private Builder(float cx, float cy, float rx, float ry) {
            super(new EllipseNode(cx,cy,rx,ry));
        }
    }

}

