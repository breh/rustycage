package rustycage;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rustycage.event.SgEvent;
import rustycage.event.SgEventListener;
import rustycage.event.TouchEnterEvent;
import rustycage.event.TouchEvent;
import rustycage.event.TouchExitEvent;
import rustycage.impl.event.ListenerHelper;
import rustycage.util.Preconditions;

/**
 * Created by breh on 8/1/17.
 */

class SgNodeEventDeliverySupport {

    private static List<SgNode> eventDeliveryPath = new ArrayList<>();

    private Class<? extends SgEvent> oneListenerEventClass;
    private SgEventListener<?> oneListener;
    private  Map<Class<? extends SgEvent>, ListenerHelper<?,?>>listenerHelperMap;

    private int enterExitListenersCount;
    private boolean readyForEnterEvent = true;


    public static <T extends SgEvent> void deliverEvent(@NonNull T event, @NonNull SgNode targetNode) {
        Preconditions.assertNotNull(event,"event");
        Preconditions.assertNotNull(targetNode,"targetNode");
        // build event path
        eventDeliveryPath.clear();;
        SgNode node = targetNode;
        do {
            eventDeliveryPath.add(node);
            node = node.getParent();
        } while (node != null);
        // capture
        for (int i= eventDeliveryPath.size() - 1; i >= 0; i--) {
            node = eventDeliveryPath.get(i);
            if (node.hasCaptureListener()) {
                node.getCaptureEventDeliverySupport().fireEvent(event, node, true);
                if (event.isConsumed()) {
                    return;
                }
            }

        }
        // bubble
        for (int i=0; i < eventDeliveryPath.size(); i++) {
            node = eventDeliveryPath.get(i);
            if (node.hasBubbleListener()) {
                node.getEventDeliverySupport().fireEvent(event, node, false);
                if (event.isConsumed()) {
                    return;
                }
            }
        }
    }



    public boolean hasEnterExitListeners() {
        return enterExitListenersCount > 0;
    }

    private boolean isReadyForEnterEvent() {
        return readyForEnterEvent;
    }

    private boolean isReadyForExitEvent() {
        return !readyForEnterEvent;
    }

    private void setReadyForEnterEvent(boolean readyForEnterEvent) {
        this.readyForEnterEvent = readyForEnterEvent;

    }

    public static void deliverTouchEvent(@NonNull MotionEvent motionEvent, @NonNull SgNodeHitPath hitPath) {
        Preconditions.assertNotNull(motionEvent,"motionEvent");
        Preconditions.assertNotNull(hitPath,"hitPath");
        // build event path
        int size = hitPath.getSize();
        if (size  > 0) {
            final SgNode hitNode = hitPath.getHitNode();
            for (int i = 0; i < size; i++) {
                SgNode node = hitPath.getNodeAt(i);
                if (node.hasCaptureListener()) {
                    float localX = hitPath.getLocalX(i);
                    float localY = hitPath.getLocalY(i);
                    // enter touch event
                    SgNodeEventDeliverySupport eventDeliverySupport = node.getCaptureEventDeliverySupport();
                    if (eventDeliverySupport.hasEnterExitListeners()) {
                        if (eventDeliverySupport.isReadyForEnterEvent()) {
                            // deliver enter event
                            eventDeliverySupport.setReadyForEnterEvent(false);
                            TouchEvent te = TouchEvent.createTouchEventFromMotionEvent(localX, localY, node, motionEvent);

                        }
                    }
                    // regular move event
                    TouchEvent touchEvent = TouchEvent.createTouchEventFromMotionEvent(localX, localY, hitNode, motionEvent);
                    node.getEventDeliverySupport().fireEvent(touchEvent, node, true);
                    if (touchEvent.isConsumed()) {
                        // we are done
                        return;
                    }
                }
            }
            // bubble
            for (int i = size - 1; i >= 0; i--) {
                SgNode node = hitPath.getNodeAt(i);
                if (node.hasBubbleListener()) {
                    float localX = hitPath.getLocalX(i);
                    float localY = hitPath.getLocalY(i);
                    TouchEvent touchEvent = TouchEvent.createTouchEventFromMotionEvent(localX, localY, hitNode, motionEvent);
                    node.getEventDeliverySupport().fireEvent(touchEvent, node, false);
                    if (touchEvent.isConsumed()) {
                        // we are done
                        return;
                    }
                }
            }
        }
    }


    public boolean hasEventListeners() {
        return oneListener != null || listenerHelperMap != null && listenerHelperMap.size() > 0;
    }

    private static boolean isEnterExitEventListener(@NonNull Class<? extends SgEvent> eventClass) {
        return TouchEnterEvent.class.isAssignableFrom(eventClass) || TouchExitEvent.class.isAssignableFrom(eventClass);
    }


    //  generic event listener
    public <T extends SgEvent> void addEventListner(@NonNull Class<T> eventClass, @NonNull SgEventListener<? super T> listener) {
        Preconditions.assertNotNull(eventClass, "eventClass");
        Preconditions.assertNotNull(listener, "listener");
        if (isEnterExitEventListener(eventClass)) {
            enterExitListenersCount++;
        }
        if (oneListener == null && listenerHelperMap == null) {
            // simplest case - just one listener
            oneListenerEventClass = eventClass;
            oneListener = listener;
            return;
        } // else
        if (oneListener != null && listenerHelperMap == null) {
            listenerHelperMap = new HashMap<>();
            ListenerHelper<SgEventListener<? super T>, T> listenerHelper = new ListenerHelper<>();
            listenerHelperMap.put(oneListenerEventClass, listenerHelper);
            listenerHelper.addListener((SgEventListener<? super T>)oneListener);
            oneListenerEventClass = null;
            oneListener = null;
        } // we have a map, we can get a listener for given class
        // we have a map already
        ListenerHelper<SgEventListener<? super T>, T> listenerHelper = (ListenerHelper<SgEventListener<? super T>, T>)listenerHelperMap.get(eventClass);
        if (listenerHelper == null) {
            listenerHelper = new ListenerHelper<>();
            listenerHelperMap.put(eventClass, listenerHelper);
        }
        listenerHelper.addListener((SgEventListener<? super T>)listener);
    }

    public <T extends SgEvent> boolean removeEventListener(@NonNull Class<T> eventClass, @NonNull SgEventListener<? super T> listener) {
        Preconditions.assertNotNull(eventClass, "eventClass");
        Preconditions.assertNotNull(listener, "listener");
        if (oneListener == listener && eventClass.equals(oneListenerEventClass)) {
            oneListener = null;
            oneListenerEventClass = null;
            if (isEnterExitEventListener(eventClass)) {
                enterExitListenersCount--;
            }

            return true;
        } // else
        if (listenerHelperMap != null) {
            ListenerHelper<SgEventListener<? super T>, T> listenerHelper = (ListenerHelper<SgEventListener<? super T>, T>)listenerHelperMap.get(eventClass);
            if (listenerHelper != null) {
                if (listenerHelper.removeListener(listener)) {
                    // we removed a listener
                    if (! listenerHelper.hasListeners()) {
                        // no listeners in helper, so we can remove this helper from the map
                        listenerHelperMap.remove(eventClass);
                        // FIXME should move to one listener in the case it is feasible ...
                        // also remote enter/exit counter
                        if (isEnterExitEventListener(eventClass)) {
                            enterExitListenersCount--;
                        }

                    }
                    return true;
                }
            }
        } // else
        return false;
    }



    private boolean isClassOrSuperClass(@NonNull Class<?> subClass, @NonNull Class<?> superClass) {
        if (subClass.equals(superClass)) {
            return true;
        }
        return superClass.isAssignableFrom(subClass);
    }


    private <T extends SgEvent> void fireEvent(@NonNull T event, @NonNull final SgNode sgNode, final boolean isCapture) {
        Preconditions.assertNotNull(event,"event");
        Class<?> clazz = event.getClass();
        if (oneListener != null) {
            if (isClassOrSuperClass(clazz,oneListenerEventClass)) {
                // we want to deliver this event
                SgEventListener<T> l = (SgEventListener<T>)oneListener;
                l.onEvent(event, sgNode, isCapture);
            }
        }
        // also look in the map
        if (listenerHelperMap != null) {
            // go over all classes all the way to SgEvent and see if there any listener
            do {
                ListenerHelper<SgEventListener<? super T>, T> listenerHelper =  (ListenerHelper<SgEventListener<? super T>, T>)listenerHelperMap.get(clazz);
                if (listenerHelper != null) {
                    listenerHelper.fireSgEvent(event, sgNode, isCapture);
                }
                clazz = clazz.getSuperclass();
            } while (! Object.class.equals(clazz)); // SgEvent derives from Object
        }

    }

}
