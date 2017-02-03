package rustycage.impl.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import rustycage.ImageNode;
import rustycage.impl.AttributesStack;
import rustycage.impl.FloatStack;

/**
 * Created by breh on 9/26/16.
 */
public class ImageCanvasRenderer extends AbstractCanvasRenderer<ImageNode> {

    @Override
    protected void renderNode(@NonNull Canvas canvas, @NonNull ImageNode node, @NonNull FloatStack opacityStack,
                              @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        Bitmap bitmap = node.getBitmap();
        if (bitmap != null) {

            Paint paint = attributes.get(Paint.class);
            float opacityValue = opacityStack.peek();
            int originalAlpha = -1;
            if (paint != null) {
                originalAlpha = paint.getAlpha();
            }
            Paint actualPaint = applyOpacityToPaint(paint, opacityValue);
            canvas.drawBitmap(bitmap,node.getMatrix(),actualPaint);

            if (originalAlpha > 0) {
                paint.setAlpha(originalAlpha);
            }

        }
    }
}
