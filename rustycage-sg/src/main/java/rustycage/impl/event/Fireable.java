package rustycage.impl.event;

import android.support.annotation.NonNull;

/**
 * Created by breh on 3/2/17.
 */

public interface Fireable<T,E> {

    /**
     * Fires the event on given listener. If method returns false
     * no futther listeners are called (the event is consumed)
     * @param listener
     * @param event
     * @return
     */
    boolean fireEvent(@NonNull T listener, @NonNull E event);
}
