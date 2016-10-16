package rustycage.impl.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import rustycage.ImageNode;

/**
 * Created by breh on 9/26/16.
 */
public class ImageCanvasRenderer extends AbstractCanvasRenderer<ImageNode> {

    @Override
    public void render(@NonNull Canvas canvas, @NonNull ImageNode node) {
        Bitmap bitmap = node.getBitmap();
        if (bitmap != null) {
            canvas.drawBitmap(bitmap,node.getMatrix(),null);
        }
    }
}
