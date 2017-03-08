package rustycage.impl.renderer;

import android.support.annotation.NonNull;

import rustycage.RustyCageView;
import rustycage.SgArc;
import rustycage.SgCustomNode;
import rustycage.SgNode;
import rustycage.SgEllipse;
import rustycage.SgGroup;
import rustycage.SgImage;
import rustycage.SgLine;
import rustycage.SgPath;
import rustycage.SgRectangle;
import rustycage.SgText;
import rustycage.util.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by breh on 9/26/16.
 */
public class RendererProvider {

    private final Map<Class<?>, AbstractCanvasRenderer<?>> rendererRegistry = new HashMap<>();

    // FIXME - registation should be done elsewhere
    private RendererProvider() {
        registerRenderer(SgImage.class, new ImageCanvasRenderer());
        registerRenderer(SgGroup.class, new GroupCanvasRenderer());
        registerRenderer(SgLine.class,new LineCanvasRenderer());
        registerRenderer(SgRectangle.class,new RectangleCanvasRenderer());
        registerRenderer(SgEllipse.class, new EllipseCanvasRenderer());
        registerRenderer(SgArc.class, new ArcCanvasRenderer());
        registerRenderer(SgPath.class, new PathCanvasRenderer());
        registerRenderer(SgText.class, new TextCanvasRenderer());
        registerRenderer(SgCustomNode.class, new CustomNodeRenderer());
    }

    private static class Holder {
        static RendererProvider INSTANCE = new RendererProvider();
    }

    public static RendererProvider getInstance() {
        return Holder.INSTANCE;
    }


    <T extends SgNode> void registerRenderer(@NonNull Class<T> nodeClass, @NonNull AbstractCanvasRenderer<T> renderer) {
        Preconditions.assertNotNull(nodeClass,"nodeClass");
        Preconditions.assertNotNull(renderer,"renderer");
        rendererRegistry.put(nodeClass,renderer);
    }

    @SuppressWarnings("unchecked")
    public @NonNull <T extends SgNode> AbstractCanvasRenderer<T> getRendererForNode(@NonNull T node) {
        Preconditions.assertNotNull(node,"node");
        Class<?> nodeClass = node.getClass();
        AbstractCanvasRenderer<?> renderer = rendererRegistry.get(nodeClass);
        if (renderer != null) {
            return (AbstractCanvasRenderer<T>)renderer;
        } else if (node instanceof SgCustomNode) {
            return (AbstractCanvasRenderer<T>)rendererRegistry.get(SgCustomNode.class);
        } else if (node instanceof SgGroup) {
            return (AbstractCanvasRenderer<T>)rendererRegistry.get(SgGroup.class);
        } else {
            throw new IllegalArgumentException("No renderer for node class: "+nodeClass);
        }
    }
}
