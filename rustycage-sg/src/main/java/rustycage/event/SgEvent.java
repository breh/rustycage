package rustycage.event;

import android.support.annotation.NonNull;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * Created by breh on 6/1/17.
 */

public abstract class SgEvent {

    private boolean isConsumed;
    private final SgNode hitNode;

    protected SgEvent(@NonNull SgNode hitNode) {
        this.hitNode = Preconditions.assertNotNull(hitNode, "hitNode");
    }

    public final SgNode getHitNode() {
        return hitNode;
    }

    public final void consume() {
        isConsumed = true;
    }

    public final boolean isConsumed() {
        return isConsumed;
    }

}
