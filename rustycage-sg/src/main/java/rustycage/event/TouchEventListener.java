package rustycage.event;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

/**
 * Created by breh on 2/8/17.
 */

public interface TouchEventListener {

    boolean onTouchEvent(@NonNull MotionEvent touchEvent, float localX, float localY, boolean isCapturePhase);

}

