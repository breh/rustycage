package rustycage.event;

import android.support.annotation.NonNull;

import rustycage.SgNode;

/**
 * Created by breh on 2/8/17.
 */

public interface SgEventListener<T extends SgEvent> {

    void onEvent(@NonNull T event, @NonNull SgNode currentNode, boolean isCapturePhase);

}

