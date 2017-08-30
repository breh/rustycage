package rustycage.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.Touch;
import android.view.MotionEvent;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 *
 * An event representing a touch event
 *
 * Created by breh on 3/2/17.
 */
public class TouchEvent extends SgEvent {

    public enum TouchType {
        DOWN,
        UP,
        MOVE,
        ENTER,
        EXIT,
        OTHER;

        static @NonNull TouchType getTouchTypeFromMotionEvent(@NonNull MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    return DOWN;
                case MotionEvent.ACTION_UP:
                    return UP;
                case MotionEvent.ACTION_MOVE:
                    return MOVE;
                default:
                    return OTHER;
            }
        }

        @Deprecated
        static @NonNull TouchType getTouchTypeFromClass(@NonNull Class<? extends TouchEvent> clazz) {
            if (TouchUpEvent.class.equals(clazz)) {
                return TouchType.UP;
            } else if (TouchDownEvent.class.equals(clazz)) {
                return TouchType.DOWN;
            } else if (TouchMoveEvent.class.equals(clazz)) {
                return TouchType.MOVE;
            } else if (TouchEnterEvent.class.equals(clazz)) {
                return TouchType.ENTER;
            } else if (TouchExitEvent.class.equals(clazz)) {
                return TouchType.EXIT;
            } else {
                return TouchType.OTHER;
            }
        }
    }

    private float localX;
    private float localY;

    private final MotionEvent motionEvent;


    TouchEvent(float localX, float localY, @NonNull SgNode hitNode, @NonNull MotionEvent motionEvent) {
        super(hitNode);
        this.localX = localX;
        this.localY = localY;
        this.motionEvent = motionEvent;
    }


    @Deprecated
    public @NonNull TouchType getTouchType() {
        return TouchType.getTouchTypeFromClass(this.getClass());
    }

    public float getLocalX() {
        return localX;
    }

    public float getLocalY() {
        return localY;
    }

    @NonNull
    TouchEvent updateLocalXY(float localX, float localY) {
        try {
            TouchEvent te = (TouchEvent)this.clone();
            te.localX = localX;
            te.localY = localY;
            return te;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName()+":[ localXY: ").append(localX).append(',').append(localY)
                .append(", touchType: ")
                .append(", hitNode: ").append(getHitNode())
                .append(']').toString();

    }


    @NonNull
    public static TouchEvent createTouchEventFromMotionEvent(float localX, float localY, @NonNull SgNode hitNode, @NonNull MotionEvent motionEvent) {
        Preconditions.assertNotNull(hitNode,"hitNode");
        Preconditions.assertNotNull(motionEvent,"motionEvent");
        TouchType touchType = TouchType.getTouchTypeFromMotionEvent(motionEvent);
        switch (touchType) {
            case DOWN:
                return new TouchDownEvent(localX, localY, hitNode, motionEvent);
            case UP:
                return new TouchUpEvent(localX, localY, hitNode, motionEvent);
            case MOVE:
                return new TouchMoveEvent(localX, localY, hitNode, motionEvent);
            case ENTER:
                return new TouchEnterEvent(localX, localY, hitNode, motionEvent);
            case EXIT:
                return new TouchExitEvent(localX, localY, hitNode, motionEvent);
            default:
                throw new RuntimeException("Unrecognized touch type: "+touchType);
        }
    }
}
