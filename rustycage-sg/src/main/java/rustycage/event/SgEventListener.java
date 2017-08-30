package rustycage.event;

import android.support.annotation.NonNull;

import rustycage.SgNode;

/**
 * A base interface representing a listener for a given node
 *
 * Created by breh on 2/8/17.
 */
public interface SgEventListener<T extends SgEvent> {

    /**
     * Called when event happends on give node.
     * @param event an event
     * @param currentNode - a current node where the event is delivered
     * @param isCapturePhase - true in the case this is a capture phase, false in the case it is a
     *                       bubble phase
     */
    void onEvent(@NonNull T event, @NonNull SgNode currentNode, boolean isCapturePhase);

}

