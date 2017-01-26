package rustycage;

import android.graphics.Bitmap;
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
    }

    @Override
    public float getWidth() {
        return bitmap != null ? bitmap.getWidth() : 0;
    }

    @Override
    public float getHeight() {
        return bitmap != null ? bitmap.getHeight() : 0;
    }

    @Override
    public float getLeft() {
        return 0;
    }

    @Override
    public float getRight() {
        return getWidth();
    }

    @Override
    public float getTop() {
        return 0;
    }

    @Override
    public float getBottom() {
        return getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
