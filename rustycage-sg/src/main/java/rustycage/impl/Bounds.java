package rustycage.impl;

import android.support.annotation.NonNull;

import rustycage.util.Preconditions;

/**
 * Created by breh on 2/1/17.
 */

public final class Bounds {

    private float left;
    private float right;
    private float top;
    private float bottom;

    public Bounds() {
    }

    public Bounds(float left, float right, float top, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }

    public void set(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void get(@NonNull float[] bounds) {
        Preconditions.assertNotNull(bounds, "bounds");
        if (bounds.length != 4) throw new IllegalArgumentException("Incorrect bounds size: "+bounds.length);
        bounds[0] = this.left;
        bounds[1] = this.top;
        bounds[2] = this.right;
        bounds[3] = this.bottom;
    }

    public void set(@NonNull float[] bounds) {
        Preconditions.assertNotNull(bounds, "bounds");
        if (bounds.length != 4) throw new IllegalArgumentException("Incorrect bounds size: "+bounds.length);
        this.left = bounds[0];
        this.top = bounds[1];
        this.right = bounds[2];
        this.bottom = bounds[3];
    }

    @Override
    public String toString() {
        return new StringBuilder("Bounds[").append(left).append(", ")
                .append(top).append("; ").append(right)
                .append(", ").append(bottom).append(']').toString();
    }
}


