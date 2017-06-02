package rustycage.event;

import android.support.annotation.NonNull;

/**
 * Created by breh on 2/8/17.
 */

public interface SgEventListener<T extends SgEvent> {

    boolean onEvent(@NonNull T event);

}

