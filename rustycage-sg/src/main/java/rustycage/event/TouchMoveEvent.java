package rustycage.event;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import rustycage.SgNode;

/**
 * Created by breh on 8/3/17.
 */

public final class TouchMoveEvent extends TouchEvent {


    TouchMoveEvent(float localX, float localY, @NonNull SgNode hitNode, @NonNull MotionEvent motionEvent) {
        super(localX, localY, hitNode, motionEvent);
    }

}
