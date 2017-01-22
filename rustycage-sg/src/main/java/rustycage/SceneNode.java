package rustycage;

import android.support.annotation.NonNull;

/**
 * Created by breh on 1/21/17.
 */

class SceneNode extends BaseNode {


    private BaseNode sceneDelegate;
    private RustyCageView view;

    public SceneNode(@NonNull BaseNode sceneDelegate, @NonNull RustyCageView view) {
        this.sceneDelegate = sceneDelegate;
        this.view = view;
    }

    @Override
    void onMarkedDirty() {

    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }
}
