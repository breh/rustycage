package rustycage;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.impl.renderer.ImageCanvasRenderer;
import rustycage.impl.renderer.RendererProvider;

/**
 * Created by breh on 9/9/16.
 */
public final class ImageNode extends BaseNode {

    private Bitmap bitmap;

    public ImageNode(@Nullable Bitmap bitmap) {
        setBitmap(bitmap);
    }

    public final void setBitmap(@Nullable Bitmap bitmap) {
        this.bitmap = bitmap;
        markDirty();
        markLocalBoundsDirty();
    }

    @Override
    protected void computeLocalBounds(@NonNull float[] bounds) {
        bounds[0] = 0;
        bounds[1] = 0;
        bounds[2] = bitmap != null ? bitmap.getWidth() : 0;
        bounds[2] = bitmap != null ? bitmap.getHeight() : 0;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
