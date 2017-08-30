package rustycage;

import android.support.annotation.NonNull;

/**
 * A node representing a custom compound node. The main idea is this
 * class provides an encapsulation of a node, so it forms a higher
 * level UI component. This is the only class of the SgNode hierarchy
 * which should be subclassed by the user of this library.
 *
 * Created by breh on 2/17/17.
 */

public abstract class SgCustomNode extends SgNode {

    private SgNode node;

    /**
     * Creates a new instance od this node. Please note the node
     * can be created way later (only when it is required by the scene graph),
     * so not all attributes are available before that.
     *
     * FIXME - needs some work around the lifecycle and perhaps separation of the actual
     *         creator class and the actual node itself ...
     *
     * @return
     */
    protected abstract @NonNull SgNode createNode();

    /**
     * Gets a node instance built by this custom node.
     * @return
     */
    public final SgNode getBuiltNode() {
        if (node == null) {
            node = createNode();
            if (node == null) {
                throw new IllegalStateException("createNode did not return a valid node");
            }
            node.setParent(this);
            invalidate();
        }
        return node;
    }

    @Override
    void onInvalidated() {
        invalidateLocalBounds();
    }

    @Override
    void clearInvalidated() {
        super.clearInvalidated();
        if (node != null) {
            node.clearInvalidated();
        }
    }

    @Override
    protected final void computeLocalBounds(@NonNull float[] bounds) {
        getBuiltNode().computeTransformedBounds(bounds);
    }

    @Override
    final void searchForHitPath(@NonNull SgNodeHitPath nodeHitPath, final float[] touchPoint) {
        getBuiltNode().findHitPath(nodeHitPath, touchPoint);
    }
}
