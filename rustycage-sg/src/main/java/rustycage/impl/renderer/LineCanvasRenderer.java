package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.Log;

import rustycage.LineNode;

/**
 * Created by breh on 10/14/16.
 */
public class LineCanvasRenderer extends ShapeCanvasRenderer<LineNode> {

    private static final String TAG = "LineRenderer";

    @Override
    public void render(@NonNull Canvas canvas, @NonNull LineNode node) {
        Log.d(TAG,"rendering line: "+node+" w:"+node.getX2()+", h:"+node.getY2()+", paint: "+node.getPaint());
        canvas.drawLine(node.getX1(),node.getY1(),node.getX2(),node.getY2(),node.getPaint());
    }
}
