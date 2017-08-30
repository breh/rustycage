package rustycage.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * A transition for scaling a given node
 *
 * Created by breh on 2/3/17.
 */

public final class ScaleTransition extends AbstractTransition<ScaleTransition, ObjectAnimator> {

    private float from = Float.NaN;
    private float to = Float.NaN;
    private float by = Float.NaN;

    private static final String PROPERTY_NAME = "scale";

    private ScaleTransition(@NonNull SgNode targetNode) {
        super(targetNode);
    }

    public static ScaleTransition create(@NonNull SgNode targetNode) {
        Preconditions.assertNotNull(targetNode,"targetNode");
        return new ScaleTransition(targetNode);
    }

    @NonNull
    public ScaleTransition from(float s) {
        this.from = s;
        return getThisTransition();
    }

    @NonNull
    public ScaleTransition to(float s) {
        this.to = s;
        return getThisTransition();
    }

    @NonNull
    public ScaleTransition by(float s) {
        this.by = s;
        return getThisTransition();
    }


    @NonNull
    @Override
    protected ObjectAnimator createAnimator() {
        ObjectAnimator animator = new ObjectAnimator();
        animator.setPropertyName(PROPERTY_NAME);
        return animator;
    }

    @Override
    protected void updateValues(@NonNull ObjectAnimator animator) {
        boolean hasFrom = !Float.isNaN(from);
        boolean hasTo = !Float.isNaN(to);
        boolean hasBy = !Float.isNaN(by);

        float f = hasFrom ? from : getTargetNode().getScaleX();
        float t = hasTo ? to : getTargetNode().getScaleX();
        if (hasBy) {
            t = f + by;
        }
        animator.setFloatValues(f, t);
    }

}
