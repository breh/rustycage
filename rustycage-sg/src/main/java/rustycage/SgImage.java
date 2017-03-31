package rustycage;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.util.Preconditions;

/**
 * Created by breh on 9/9/16.
 */
public final class SgImage extends SgNode {

    private static final String TAG = "SgImage";

    private Bitmap bitmap;

    private SgImage(@Nullable Bitmap bitmap) {
        setBitmap(bitmap);
    }

    public final void setBitmap(@Nullable Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidateLocalBounds();
    }

    @Override
    protected void computeLocalBounds(@NonNull float[] bounds) {
        bounds[0] = 0;
        bounds[1] = 0;
        bounds[2] = bitmap != null ? bitmap.getWidth() : 0;
        bounds[3] = bitmap != null ? bitmap.getHeight() : 0;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    // Builders

    public static Builder createWithBitmap(@NonNull Bitmap bitmap) {
        return new Builder(bitmap);
    }

    public static Builder createWithResource(@NonNull Resources resources, int id) {
        Preconditions.assertNotNull(resources,"resources");
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id);
        return new Builder(bitmap);
    }



    public static class Builder extends SgNode.Builder<SgImage.Builder, SgImage> {
        private Builder(@Nullable Bitmap bitmap) {
            super(new SgImage(bitmap));
        }
    }

}
