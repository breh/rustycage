package rustycage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import rustycage.event.TouchEvent;
import rustycage.event.TouchEventListener;
import rustycage.impl.event.Fireable;
import rustycage.impl.event.ListenerHelper;
import rustycage.util.Preconditions;

/**
 * Created by breh on 3/3/17.
 */




class SgNodeEventSupport {


    private static final class TouchEventListenerWrapper {

        @Nullable
        final TouchEvent.TouchType touchType;
        @NonNull
        final TouchEventListener listener;
        final boolean isCapturePhase;

        TouchEventListenerWrapper(@Nullable TouchEvent.TouchType touchType, @NonNull TouchEventListener listener,
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

    public boolean hasCaptureListeners() {
        return captureListenersCount > 0;
    }


    public boolean hasBubbleListeners() {
        return (eventListeners.size() - captureListenersCount) > 0;
    }


    public void addOnTouchListener(@NonNull TouchEventListener listener, @Nullable TouchEvent.TouchType touchType, boolean capturePhase) {
        Preconditions.assertNotNull(listener, "listener");
        TouchEventListenerWrapper wrapper = new TouchEventListenerWrapper(touchType, listener, capturePhase);
        eventListeners.addListener(wrapper);
        if (capturePhase) {
            captureListenersCount++;
        }
    }

    public boolean removeOnTouchListener(@NonNull TouchEventListener listener, @Nullable TouchEvent.TouchType touchType, boolean capturePhase) {
        TouchEventListenerWrapper wrapper = new TouchEventListenerWrapper(touchType, listener, capturePhase);
        boolean result = eventListeners.removeListener(wrapper);
        if (result && capturePhase) {
            captureListenersCount--;
        }
        return result;
    }


    public boolean deliverEvent(@NonNull MotionEvent motionEvent, float localX, float localY, final boolean isCapture) {
        boolean consumed = false;
        if (eventListeners.hasListeners()) {
            TouchEvent touchEvent = new TouchEvent(localX, localY, isCapture, motionEvent);
            consumed = eventListeners.fireEvent(new Fireable<TouchEventListenerWrapper, TouchEvent>() {
                @Override
                public boolean fireEvent(@NonNull TouchEventListenerWrapper listener, @NonNull TouchEvent event) {
                    boolean eventConsumed = false;
                    if (listener.touchType != null && listener.touchType.equals(event.getTouchType()) && isCapture == listener.isCapturePhase) {
                        eventConsumed = listener.listener.onTouchEvent(event);
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
