package rustycage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import rustycage.event.SgEventListener;
import rustycage.event.TouchEvent;
import rustycage.impl.event.Fireable;
import rustycage.impl.event.ListenerHelper;
import rustycage.util.Preconditions;

/**
 * Created by breh on 3/3/17.
 */
final class SgNodeEventSupport {


    private static final class TouchEventListenerWrapper {

        @Nullable
        final TouchEvent.TouchType touchType;
        @NonNull
        final SgEventListener listener;
        final boolean isCapturePhase;

        TouchEventListenerWrapper(@Nullable TouchEvent.TouchType touchType, @NonNull SgEventListener listener,
                                  boolean isCapturePhase) {
            this.touchType = touchType;
            this.listener = listener;
            this.isCapturePhase = isCapturePhase;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof TouchEventListenerWrapper) {
                TouchEventListenerWrapper that = (TouchEventListenerWrapper) obj;
                return this.listener.equals(that.listener) && this.touchType == that.touchType
                        && this.isCapturePhase == that.isCapturePhase;
            } // else
            return false;
        }

        @Override
        public int hashCode() {
            return listener.hashCode()
                    + (touchType != null ? touchType.hashCode() : 0)
                    + (isCapturePhase ? 1234 : 0);
        }
    }


    private int captureListenersCount;
    private final ListenerHelper<TouchEventListenerWrapper, TouchEvent> eventListeners = new ListenerHelper<>();
    private int enterExitListenersCount;
    private boolean readyForEnterEvent = true;

    public boolean hasEnterExitListeners() {
        return enterExitListenersCount > 0;
    }

    public boolean hasCaptureListeners() {
        return captureListenersCount > 0;
    }


    public boolean hasBubbleListeners() {
        return (eventListeners.size() - captureListenersCount) > 0;
    }

    public boolean isReadyForEnterEvent() {
        return readyForEnterEvent;
    }

    public boolean isReadyForExitEvent() {
        return !readyForEnterEvent;
    }


    public void addOnTouchListener(@Nullable TouchEvent.TouchType touchType, boolean capturePhase, @NonNull SgEventListener listener) {
        Preconditions.assertNotNull(listener, "listener");
        TouchEventListenerWrapper wrapper = new TouchEventListenerWrapper(touchType, listener, capturePhase);
        eventListeners.addListener(wrapper);
        if (capturePhase) {
            captureListenersCount++;
        }
        if (touchType == null || touchType == TouchEvent.TouchType.ENTER
                || touchType == TouchEvent.TouchType.EXIT) {
            enterExitListenersCount++;
        }
    }

    public boolean removeOnTouchListener(@Nullable TouchEvent.TouchType touchType, boolean capturePhase, @NonNull SgEventListener listener) {
        TouchEventListenerWrapper wrapper = new TouchEventListenerWrapper(touchType, listener, capturePhase);
        boolean result = eventListeners.removeListener(wrapper);
        if (result) {
            if (capturePhase) {
                captureListenersCount--;
            }
            if (touchType == null || touchType == TouchEvent.TouchType.ENTER
                    || touchType == TouchEvent.TouchType.EXIT) {
                enterExitListenersCount--;
            }
        }
        return result;
    }


    public boolean deliverEvent(@NonNull MotionEvent motionEvent, float localX, float localY,
                                @NonNull SgNode currentNode, @NonNull SgNode hitNode, final boolean isCapture) {
        boolean consumed = false;
        if (eventListeners.hasListeners()) {
            TouchEvent touchEvent = null;
            // detect enter / exit
            if (isReadyForEnterEvent()) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    // we have our enter event
                    readyForEnterEvent = false;
                    touchEvent = new TouchEvent(localX, localY, TouchEvent.TouchType.ENTER, isCapture,
                            currentNode, hitNode, motionEvent);
                }
            }
            if (touchEvent == null) {
                touchEvent = new TouchEvent(localX, localY, isCapture,
                        currentNode, hitNode, motionEvent);
            }
            consumed = eventListeners.fireEvent(new Fireable<TouchEventListenerWrapper, TouchEvent>() {
                @Override
                public boolean fireEvent(@NonNull TouchEventListenerWrapper listener, @NonNull TouchEvent event) {
                    boolean eventConsumed = false;
                    if ( (listener.touchType == null || listener.touchType != null && listener.touchType.equals(event.getTouchType()))
                            && isCapture == listener.isCapturePhase) {
                        eventConsumed = listener.listener.onEvent(event);
                        if (eventConsumed) {
                            event.consume();
                        }
                    }
                    return eventConsumed;
                }
            }, touchEvent);
        }
        return consumed;
    }
}
