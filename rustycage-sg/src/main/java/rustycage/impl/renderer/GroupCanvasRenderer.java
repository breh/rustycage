package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import rustycage.BaseNode;
import rustycage.GroupNode;
import rustycage.impl.AttributesStack;

/**
 * Created by breh on 9/26/16.
 */
public class GroupCanvasRenderer extends AbstractCanvasRenderer<GroupNode> {

    @Override
    protected void renderNode(@NonNull Canvas canvas, @NonNull GroupNode node, @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        int size = node.size();
        // setup attributes
        node.pushAttributes(attributes);
        for (int i=0; i < size; i++) {
            BaseNode childNode = node.get(i);
            AbstractCanvasRenderer<BaseNode> renderer = RendererProvider.getInstance().getRendererForNode(childNode);
            renderer.render(canvas,childNode, attributes, displayMetrics);
        }
        // pop attributes
        node.popAttributes(attributes);
    }
}
