package rustycage;

import android.support.annotation.NonNull;

/**
 * Created by breh on 1/21/17.
 */

public class SgEllipse extends SgShape {


    private float cx, cy, rx, ry;

    private SgEllipse(float rx, float ry, float cx, float cy) {
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

    @Override
    boolean isPointInHitTarget(@NonNull float[] point) {
        // need to check the ellipse
        float dx = point[0] - cx;
        float dy = point[1] - cy;
        float dx2 = dx*dx;
        float dy2 = dy*dy;
        float cx2 = cx*cx;
        float cy2 = cy*cy;
        float value = dx2 / cx2 + dy2 / cy2;
        return value < 1f;
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




    public static class Builder extends SgShape.Builder<Builder, SgEllipse> {
        private Builder(float cx, float cy, float rx, float ry) {
            super(new SgEllipse(cx,cy,rx,ry));
        }
    }

}

