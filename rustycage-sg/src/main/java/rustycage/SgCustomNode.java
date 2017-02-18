package rustycage;

import android.support.annotation.NonNull;
import android.util.Log;

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
            markDirty();
        }
        return node;
    }

    @Override
    void onMarkedDirty() {
        markLocalBoundsDirty();
    }

    @Override
    void clearDirty() {
        super.clearDirty();
        if (node != null) {
            node.clearDirty();
        }
    }

    @Override
    protected final void computeLocalBounds(@NonNull float[] bounds) {
        getBuiltNode().computeTransformedBounds(bounds);
    }

    @Override
    final void searchForHitPath(@NonNull NodeHitPath nodeHitPath, final float[] touchPoint) {
        getBuiltNode().findHitPath(nodeHitPath, touchPoint);
    }
}
