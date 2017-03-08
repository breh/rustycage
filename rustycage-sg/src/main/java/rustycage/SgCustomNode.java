package rustycage;

import android.support.annotation.NonNull;

/**
 * Created by breh on 2/17/17.
 */

public abstract class SgCustomNode extends SgNode {

    private SgNode node;

    protected abstract @NonNull SgNode createNode();

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
