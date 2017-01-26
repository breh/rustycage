package rustycage.impl.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import rustycage.ImageNode;
import rustycage.impl.AttributesStack;

/**
 * Created by breh on 9/26/16.
 */
public class ImageCanvasRenderer extends AbstractCanvasRenderer<ImageNode> {

    @Override
    protected void renderNode(@NonNull Canvas canvas, @NonNull ImageNode node, @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        Bitmap bitmap = node.getBitmap();
        if (bitmap != null) {
            canvas.drawBitmap(bitmap,node.getMatrix(),null);
        }
    }
}
