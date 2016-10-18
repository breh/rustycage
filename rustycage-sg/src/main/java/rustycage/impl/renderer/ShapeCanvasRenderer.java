package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import rustycage.ShapeNode;
import rustycage.impl.AttributesStack;

/**
 * Created by breh on 10/14/16.
 */
public abstract class ShapeCanvasRenderer<S extends ShapeNode> extends AbstractCanvasRenderer<S> {

    public final void render(@NonNull Canvas canvas, @NonNull S node, @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        Paint paint = node.getPaint();
        if (paint != null) {
            attributes.push(Paint.class,paint);
        }
        renderShape(canvas, node, attributes, displayMetrics);
        if (paint != null) {
            attributes.pop(Paint.class);
        }
    }

    abstract public void renderShape(@NonNull Canvas canvas, @NonNull S node, @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics);

}
