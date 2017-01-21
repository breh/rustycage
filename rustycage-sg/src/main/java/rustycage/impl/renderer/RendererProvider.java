package rustycage.impl.renderer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.BaseNode;
import rustycage.EllipseNode;
import rustycage.GroupNode;
import rustycage.ImageNode;
import rustycage.LineNode;
import rustycage.RectangleNode;
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
        registerRenderer(ImageNode.class, new ImageCanvasRenderer());
        registerRenderer(GroupNode.class, new GroupCanvasRenderer());
        registerRenderer(LineNode.class,new LineCanvasRenderer());
        registerRenderer(RectangleNode.class,new RectangleCanvasRenderer());
        registerRenderer(EllipseNode.class, new EllipseCanvasRenderer());
    }

    private static class Holder {
        static RendererProvider INSTANCE = new RendererProvider();
    }

    public static RendererProvider getInstance() {
        return Holder.INSTANCE;
    }




    <T extends BaseNode> void registerRenderer(@NonNull Class<T> nodeClass, @NonNull AbstractCanvasRenderer<T> renderer) {
        Preconditions.assertNotNull(nodeClass,"nodeClass");
        Preconditions.assertNotNull(renderer,"renderer");
        rendererRegistry.put(nodeClass,renderer);
    }

    @SuppressWarnings("unchecked")
    public @NonNull <T extends BaseNode> AbstractCanvasRenderer<T> getRendererForNode(@NonNull T node) {
        Preconditions.assertNotNull(node,"node");
        Class<?> nodeClass = node.getClass();
        AbstractCanvasRenderer<?> renderer = rendererRegistry.get(nodeClass);
        if (renderer != null) {
            return (AbstractCanvasRenderer<T>)renderer;
        } else {
            throw new IllegalArgumentException("No renderer for node class: "+nodeClass);
        }
    }
}
