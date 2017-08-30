package rustycage.event;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import rustycage.SgNode;

/**
 * TouchUp event
 *
 * Created by breh on 8/3/17.
 */
public final class TouchUpEvent extends TouchEvent {


    TouchUpEvent(float localX, float localY, @NonNull SgNode hitNode, @NonNull MotionEvent motionEvent) {
        super(localX, localY, hitNode, motionEvent);
    }

}
