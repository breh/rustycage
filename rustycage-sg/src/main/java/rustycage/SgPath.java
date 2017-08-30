package rustycage;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.NonNull;

/**
 *
 * A node representing a path
 *
 * Created by breh on 2/16/17.
 */
public final class SgPath extends SgShape {

    private static final String TAG = "SgPath";

    private Path path;
    private Region region;

    private SgPath(@NonNull Path path) {
        this.path = path;
    }

    @Override
    void onInvalidated() {
        region = null;
    }

    @Override
    protected void computeLocalBounds(@NonNull float[] bounds) {
        RectF rectF = new RectF();
        path.computeBounds(rectF,true);
        bounds[0] = rectF.left;
        bounds[1] = rectF.top;
        bounds[2] = rectF.right;
        bounds[3] = rectF.bottom;
    }

    @Override
    boolean isPointInHitTarget(@NonNull float[] point) {
        // FIXME - region uses int, not enought resolution in some cases
        if (region == null) {
            region = new Region();
            Region clip = new Region((int)getLocalBoundsLeft(), (int)getLocalBoundsTop(), (int)getLocalBoundsRight(), (int)getLocalBoundsBottom());
            region.setPath(path,clip);
        }
        boolean isInside = region.contains((int)point[0], (int)point[1]);
        /*
        if (isInside) {
            Log.d(TAG,"is inside !!!: " + point[0] + "," + point[1]+" : "+this);
        } else {
            Log.d(TAG,"is outside: " + point[0] + "," + point[1]+" : "+this);
        }*/
        return isInside;

    }

    // FIXME path should not be exposed
    public @NonNull Path getPath() {
        return path;
    }

    public static Builder create() {
        return new Builder();
    }


    public static class Builder extends SgShape.Builder<SgPath.Builder, SgPath> {

        private Builder() {
            super(new SgPath(new Path()));
        }

        public Builder arcTo(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
            getNode().path.arcTo(left, top, right, bottom, startAngle, sweepAngle, false);
            return getBuilder();
        }


        public Builder cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
            getNode().path.cubicTo(x1, y1, x2, y2, x3, y3);
            return getBuilder();
        }

        public Builder lineTo(float x, float y) {
            getNode().path.lineTo(x, y);
            return getBuilder();
        }

        public Builder moveTo(float x, float y) {
            getNode().path.moveTo(x, y);
            return getBuilder();
        }

        public Builder quadTo(float x1, float y1, float x2, float y2) {
            getNode().path.quadTo(x1, y1, x2, y2);
            return getBuilder();
        }

        public Builder rCubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
            getNode().path.rCubicTo(x1, y1, x2, y2, x3, y3);
            return getBuilder();
        }

        public Builder rLineTo(float x, float y) {
            getNode().path.rLineTo(x, y);
            return getBuilder();
        }

        public Builder rMoveTo(float x, float y) {
            getNode().path.rMoveTo(x, y);
            return getBuilder();
        }

        public Builder rQuadTo(float x1, float y1, float x2, float y2) {
            getNode().path.rQuadTo(x1, y1, x2, y2);
            return getBuilder();
        }

        public Builder fillType(@NonNull Path.FillType fillType) {
            getNode().path.setFillType(fillType);
            return getBuilder();
        }

        public Builder close() {
            getNode().path.close();
            return getBuilder();
        }

        @Override
        public SgPath build() {
            getNode().invalidate();
            return super.build();
        }
    }
}
