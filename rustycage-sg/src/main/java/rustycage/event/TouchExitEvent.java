package rustycage.event;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import rustycage.SgNode;

/**
 *
 * TouchExit event (when a touch point moves out of the area occupied by a a node)
 *
 * Created by breh on 8/3/17.
 */
public final class TouchExitEvent extends TouchEvent {


    TouchExitEvent(float localX, float localY, @NonNull SgNode hitNode, @NonNull MotionEvent motionEvent) {
        super(localX, localY, hitNode, motionEvent);
    }

}
