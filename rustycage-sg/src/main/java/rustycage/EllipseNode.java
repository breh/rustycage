package rustycage;

import android.graphics.drawable.shapes.Shape;
import android.support.annotation.NonNull;

/**
 * Created by breh on 1/21/17.
 */

public class EllipseNode extends ShapeNode {


    private float cx, cy, rx, ry;

    private EllipseNode(float rx, float ry, float cx, float cy) {
        this.rx = rx;
        this.ry = ry;
        this.cx = cx;
        this.cy = cy;
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

    @Override
    protected void computeLocalBounds(@NonNull float[] bounds) {
        bounds[0] = cx - rx;;
        bounds[1] = cy - ry;
        bounds[2] = cx + rx;
        bounds[3] = cy + ry;
    }

    public static Builder createCircle(float r) {
        return new Builder(r, r, 0, 0);
    }

    public static Builder createCircle(float r, float cx, float cy) {
        return new Builder(r, r, cx,cy);
    }

    public static Builder createEllipse(float rx, float ry) {
        return new Builder(rx,ry, 0, 0);
    }

    public static Builder createEllipse(float rx, float ry, float cx, float cy) {
        return new Builder(rx,ry, cx, cy);
    }




    public static class Builder extends ShapeNode.Builder<Builder,EllipseNode> {
        private Builder(float cx, float cy, float rx, float ry) {
            super(new EllipseNode(cx,cy,rx,ry));
        }
    }

}

