package rustycage.event;

import android.support.annotation.NonNull;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * An abstraction for all events in the scene graph.
 *
 * Created by breh on 6/1/17.
 */
public abstract class SgEvent {

    private boolean isConsumed;
    private final SgNode hitNode; // FIXME - rename this to targetNode

    /**
     * Creates a new event with the given hit node.
     * @param hitNode - a "target" node - cannot be null
     */
    protected SgEvent(@NonNull SgNode hitNode) {
        this.hitNode = Preconditions.assertNotNull(hitNode, "hitNode");
    }

    /**
     * Gets target node
     *
     * FIXME - rename this to getTargetNode
     * @return
     */
    public final SgNode getHitNode() {
        return hitNode;
    }

    /**
     * Consumes this event (so it is no longer propagated in the scenegraph)
     */
    public final void consume() {
        isConsumed = true;
    }

    /**
     * Checks if the event is consumed
     * @return
     */
    public final boolean isConsumed() {
        return isConsumed;
    }

}
