package rustycage.impl.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import rustycage.util.Preconditions;

/**
 * Created by breh on 3/2/17.
 */

public final class ListenerHelper<T,E> {

    private @Nullable T listener;
    private @Nullable List<T> listeners;

    public void addListener(@NonNull T listener) {
        Preconditions.assertNotNull(listener,"listener");
        if (this.listener == null) {
            // everything is empty
            this.listener = listener;
        } else {
            if (listeners == null) {
                listeners = new ArrayList<>();
            }
            listeners.add(listener);
        }
    }

    /**
     * Number of listeners in the helper
     * @return
     */
    public int size() {
        int result = listener != null ? 1 : 0;
        if (listeners != null) {
            result += listeners.size();
        }
        return result;
    }

    public boolean hasListeners() {
        return listener != null || listeners.size() > 0;
    }


    public boolean removeListener(@NonNull T listener) {
        Preconditions.assertNotNull(listener,"listener");
        boolean result = false;
        if (listener.equals(this.listener)) {
            this.listener = null;
            result = true;
            if (listeners != null) {
                this.listener = listeners.remove(0);
            }
        } else if (listeners != null) {
            result = listeners.remove(listener);
        }
        if (listeners != null && listeners.size() == 0) {
            listeners = null;
        }
        return result;
    }



    public boolean fireEvent(@NonNull Fireable<T,E> fireable, @NonNull E event) {
        Preconditions.assertNotNull(fireable, "fireable");
        Preconditions.assertNotNull(event, "event");
        // single listener first
        if (listener != null) {
            boolean isConsumed = fireable.fireEvent(listener, event);
            if (isConsumed) {
                return true; // we are done
            }
        }
        // multiple listeners next
        if (listeners != null) {
            int size = listeners.size();
            for (int i=0; i < size; i++) {
                boolean isConsumed  = fireable.fireEvent(listeners.get(i), event);
                if (isConsumed) {
                    return true;
                }
            }
        } // else not consumed
        return false;
    }

}
