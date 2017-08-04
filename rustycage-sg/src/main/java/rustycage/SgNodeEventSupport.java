package rustycage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import java.util.List;

import rustycage.event.SgEvent;
import rustycage.event.SgEventListener;
import rustycage.event.TouchEvent;
import rustycage.impl.event.Fireable;
import rustycage.impl.event.ListenerHelper;
import rustycage.util.Preconditions;

/**
 * Created by breh on 3/3/17.
 */
final class SgNodeEventSupport {



    private static class SgEventListenerWrapper<T extends SgEvent> {
        @NonNull
        final SgEventListener listener;
        final boolean isCapturePhase;

        SgEventListenerWrapper(@NonNull SgEventListener listener, boolean isCapturePhase) {
            this.listener = listener;
            this.isCapturePhase = isCapturePhase;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof SgEventListenerWrapper) {
                SgEventListenerWrapper that = (SgEventListenerWrapper) obj;
                return this.listener.equals(that.listener) && this.isCapturePhase == that.isCapturePhase;
            } // else
            return false;
        }

        @Override
        public int hashCode() {
            return listener.hashCode() + (isCapturePhase ? 1234 : 0);
        }

    }

    private static final class TouchEventListenerWrapper extends SgEventListenerWrapper<TouchEvent> {

        @Nullable
        final TouchEvent.TouchType touchType;

        TouchEventListenerWrapper(@Nullable TouchEvent.TouchType touchType, @NonNull SgEventListener listener,
                                  boolean isCapturePhase) {
            super(listener, isCapturePhase);
            this.touchType = touchType;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof TouchEventListenerWrapper) {
                TouchEventListenerWrapper that = (TouchEventListenerWrapper) obj;
                return super.equals(obj) && this.touchType == that.touchType;
            } // else
            return false;
        }

        @Override
        public int hashCode() {
            return super.hashCode() + (touchType != null ? touchType.hashCode() : 0);
        }
    }


    private int captureListenersCount;
    //private final ListenerHelper<TouchEventListenerWrapper, TouchEvent> eventListeners = new ListenerHelper<>();
    private int enterExitListenersCount;
    private boolean readyForEnterEvent = true;

    public boolean hasEnterExitListeners() {
        return enterExitListenersCount > 0;
    }


    public boolean isReadyForEnterEvent() {
        return readyForEnterEvent;
    }

    public boolean isReadyForExitEvent() {
        return !readyForEnterEvent;
    }



    /*

    public boolean deliverTouchEvent(@NonNull MotionEvent motionEvent, float localX, float localY,
                                @NonNull SgNode currentNode, @NonNull SgNode hitNode, final boolean isCapture) {
        boolean consumed = false;
        if (eventListeners.hasListeners()) {
            TouchEvent touchEvent = null;
            // detect enter / exit
            if (isReadyForEnterEvent()) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    // we have our enter event
                    readyForEnterEvent = false;
                    touchEvent = TouchEvent.createTouchEventFromMotionEvent(localX, localY, hitNode, motionEvent);
                }
            }
            if (touchEvent == null) {
                //TouchEvent = new TouchEvent(localX, localY, isCapture,
                //        currentNode, hitNode, motionEvent);
            }
            consumed = eventListeners.fireEvent(new Fireable<TouchEventListenerWrapper, TouchEvent>() {
                @Override
                public boolean fireEvent(@NonNull TouchEventListenerWrapper listener, @NonNull TouchEvent event) {
                    boolean eventConsumed = false;
                    if ( (listener.touchType == null || listener.touchType != null && listener.touchType.equals(event.getTouchType()))
                            && isCapture == listener.isCapturePhase) {
                        //eventConsumed = listener.listener.onEvent(event);
                        if (eventConsumed) {
                            event.consume();
                        }
                    }
                    return eventConsumed;
                }
            }, touchEvent);
        }
        return consumed;
    }*/


}
