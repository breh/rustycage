package rustycage;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.util.Preconditions;

/**
 * A node representing bitmap image. Bounds of the node
 * are specified by the width and height of the image
 *
 * Created by breh on 9/9/16.
 */
public final class SgImage extends SgNode {

    private static final String TAG = "SgImage";

    private Bitmap bitmap;

    private SgImage(@Nullable Bitmap bitmap) {
        setBitmap(bitmap);
    }

    /**
     * Sets bitmap to this node.
     *
     * @param bitmap
     */
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

    /**
     * Gets bitmap of this node
     *
     * @return
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    // Builders

    /**
     * Creates a node with given bitmap
     *
     * @param bitmap
     * @return
     */
    public static Builder createWithBitmap(@NonNull Bitmap bitmap) {
        return new Builder(bitmap);
    }

    /**
     * Creates a node with given bitmap as a resource
     *
     * @param resources
     * @param id
     * @return
     */
    public static Builder createWithResource(@NonNull Resources resources, int id) {
        Preconditions.assertNotNull(resources, "resources");
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id);
        return new Builder(bitmap);
    }

    public static Builder createEmpty() {
        return new Builder(null);
    }


    public static class Builder extends SgNode.Builder<SgImage.Builder, SgImage> {
        private Builder(@Nullable Bitmap bitmap) {
            super(new SgImage(bitmap));
        }
    }

}
