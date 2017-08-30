package rustycage.impl.renderer;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import rustycage.SgCustomNode;
import rustycage.SgNode;
import rustycage.impl.AttributesStack;
import rustycage.impl.FloatStack;

/**
 * Renderer for {@link SgCustomNode}
 * Created by breh on 2/17/17.
 */

public class CustomNodeRenderer extends AbstractCanvasRenderer<SgCustomNode> {

    @Override
    protected void renderNode(@NonNull Canvas canvas, @NonNull SgCustomNode node, @NonNull FloatStack opacityStack,
                              @NonNull AttributesStack attributes, @NonNull DisplayMetrics displayMetrics) {
        SgNode innerNode = node.getBuiltNode();
        AbstractCanvasRenderer<SgNode> renderer = RendererProvider.getInstance().getRendererForNode(innerNode);
        renderer.render(canvas,innerNode, opacityStack, attributes, displayMetrics);

    }

}
