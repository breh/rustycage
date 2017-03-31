package rustycage.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * Created by breh on 3/2/17.
 */

public final class TouchEvent {

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
    }

    private final TouchType touchType;
    private float localX;
    private float localY;
    private boolean isCapturePhase;
    private final MotionEvent motionEvent;
    private boolean isConsumed;
    private SgNode currentNode;
    private SgNode hitNode;


    public TouchEvent(float localX, float localY, boolean isCapturePhase,
                      @NonNull SgNode currentNode, @NonNull SgNode hitNode, @NonNull MotionEvent motionEvent) {
        this(localX, localY, null, isCapturePhase, currentNode, hitNode, motionEvent);
    }


    public TouchEvent(float localX, float localY, @Nullable TouchType touchType, boolean isCapturePhase,
                      @NonNull SgNode currentNode, @NonNull SgNode hitNode, @NonNull MotionEvent motionEvent) {
        this.motionEvent = Preconditions.assertNotNull(motionEvent, "motionEvent");
        this.localX = localX;
        this.localY = localY;
        this.isCapturePhase = isCapturePhase;
        this.currentNode = Preconditions.assertNotNull(currentNode,"currentNode");
        this.hitNode = Preconditions.assertNotNull(hitNode, "hitNode");
        this.touchType = touchType != null ? touchType : TouchType.getTouchTypeFromMotionEvent(motionEvent);
    }



    public @NonNull TouchType getTouchType() {
        return touchType;
    }

    public float getLocalX() {
        return localX;
    }

    public float getLocalY() {
        return localY;
    }

    public SgNode getCurrentNode() {
        return currentNode;
    }

    public SgNode getHitNode() {
        return hitNode;
    }

    // FIXME should not be public - use accessor or make it package private
    private void updateEvent(float localX, float localY, boolean isCapturePhase) {
        this.localX = localX;
        this.localY = localY;
        this.isCapturePhase = isCapturePhase;
    }

    public boolean isCapturePhase() {
        return isCapturePhase;
    }


    public void consume() {
        isConsumed = true;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    @Override
    public String toString() {
        return new StringBuilder("TouchEvent:[ localXY: ").append(localX).append(',').append(localY)
                .append(", touchType: ").append(touchType).append(", isCapture: ").append(isCapturePhase)
                .append(", currentNode: ").append(currentNode)
                .append(", hitNode: ").append(hitNode)
                .append(']').toString();

    }
}
