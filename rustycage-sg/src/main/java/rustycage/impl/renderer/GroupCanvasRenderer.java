package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import rustycage.SgNode;
import rustycage.SgGroup;
import rustycage.impl.AttributesStack;
import rustycage.impl.FloatStack;

/**
 * Renderer for {@link SgGroup}
 *
 * Created by breh on 9/26/16.
 */
public class GroupCanvasRenderer extends AbstractCanvasRenderer<SgGroup> {

    @Override
    protected void renderNode(@NonNull Canvas canvas, @NonNull SgGroup node, @NonNull FloatStack opacityStack,
                              @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        int size = node.size();
        // setup attributes
        node.pushAttributes(attributes);
        for (int i=0; i < size; i++) {
            SgNode childNode = node.get(i);
            AbstractCanvasRenderer<SgNode> renderer = RendererProvider.getInstance().getRendererForNode(childNode);
            renderer.render(canvas,childNode, opacityStack, attributes, displayMetrics);
        }
        // pop attributes
        node.popAttributes(attributes);
    }
}
