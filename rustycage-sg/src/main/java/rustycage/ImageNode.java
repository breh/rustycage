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
        if (bitmap != null) {
            setSize(bitmap.getWidth(),bitmap.getHeight());
        } else {
            setSize(0,0);
        }
        //
        markDirty();
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

}
