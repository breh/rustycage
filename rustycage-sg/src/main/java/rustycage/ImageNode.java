package rustycage;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import rustycage.impl.renderer.ImageCanvasRenderer;
import rustycage.impl.renderer.RendererProvider;

/**
 * Created by breh on 9/9/16.
 */
public final class ImageNode extends BaseNode {

    private static final String TAG = "ImageNode";

    private Bitmap bitmap;

    private ImageNode(@Nullable Bitmap bitmap) {
        setBitmap(bitmap);
    }

    public final void setBitmap(@Nullable Bitmap bitmap) {
        this.bitmap = bitmap;
        markLocalBoundsDirty();
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


    public static class Builder extends BaseNode.Builder<ImageNode.Builder,ImageNode> {
        private Builder(@Nullable Bitmap bitmap) {
            super(new ImageNode(bitmap));
        }
    }

}
