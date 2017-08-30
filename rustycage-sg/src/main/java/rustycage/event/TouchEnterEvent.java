package rustycage.event;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import rustycage.SgNode;

/**
 * TouchEnter event (when a touch point moves to the area occupied by a a node)
 *
 * Created by breh on 8/3/17.
 */
public final class TouchEnterEvent extends TouchEvent {


    TouchEnterEvent(float localX, float localY, @NonNull SgNode hitNode, @NonNull MotionEvent motionEvent) {
        super(localX, localY, hitNode, motionEvent);
    }

}
