package rustycage.event;

import android.support.annotation.NonNull;

/**
 * Created by breh on 2/8/17.
 */

public interface TouchEventListener {

    boolean onTouchEvent(@NonNull TouchEvent touchEvent);

}

