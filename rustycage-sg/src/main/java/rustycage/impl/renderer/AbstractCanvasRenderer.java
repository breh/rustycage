package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import rustycage.Attribute;
import rustycage.BaseNode;
import rustycage.impl.AttributesStack;

/**
 * Created by breh on 9/26/16.
 */
public abstract class AbstractCanvasRenderer<T extends BaseNode>  {

    abstract public void render(@NonNull Canvas canvas, @NonNull T node, @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics);

}
