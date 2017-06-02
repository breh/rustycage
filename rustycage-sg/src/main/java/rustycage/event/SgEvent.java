package rustycage.event;

import android.support.annotation.NonNull;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * Created by breh on 6/1/17.
 */

public abstract class SgEvent {

    private final boolean isCapturePhase;
    private boolean isConsumed;
    private final SgNode currentNode;
    private final SgNode hitNode;

    protected SgEvent(boolean isCapturePhase, @NonNull SgNode currentNode, @NonNull SgNode hitNode) {
        this.isCapturePhase = isCapturePhase;
        this.currentNode = Preconditions.assertNotNull(currentNode,"currentNode");
        this.hitNode = Preconditions.assertNotNull(hitNode, "hitNode");
    }


    public final SgNode getCurrentNode() {
        return currentNode;
    }

    public final SgNode getHitNode() {
        return hitNode;
    }

    public final boolean isCapturePhase() {
        return isCapturePhase;
    }

    public final void consume() {
        isConsumed = true;
    }

    public final boolean isConsumed() {
        return isConsumed;
    }

}
