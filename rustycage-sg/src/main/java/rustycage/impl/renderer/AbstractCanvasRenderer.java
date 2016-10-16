package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

import rustycage.BaseNode;

/**
 * Created by breh on 9/26/16.
 */
public abstract class AbstractCanvasRenderer<T extends BaseNode>  {

    abstract public void render(@NonNull Canvas canvas, @NonNull T node);

}
