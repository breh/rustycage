package rustycage.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * Created by breh on 2/3/17.
 */

public final class RotationTransition extends AbstractTransition<RotationTransition, ObjectAnimator> {

    private float from = Float.NaN;
    private float to = Float.NaN;
    private float by = Float.NaN;

    private static final String PROPERTY_NAME = "rotation";

    private RotationTransition(@NonNull SgNode targetNode) {
        super(targetNode);
    }

    @NonNull
    public static RotationTransition create(@NonNull SgNode targetNode) {
        Preconditions.assertNotNull(targetNode,"targetNode");
        return new RotationTransition(targetNode);
    }

    @NonNull
    public RotationTransition from(float r) {
        this.from = r;
        return getThisTransition();
    }

    @NonNull
    public RotationTransition to(float r) {
        this.to = r;
        return getThisTransition();
    }

    @NonNull
    public RotationTransition by(float r) {
        this.by = r;
        return getThisTransition();
    }


    @NonNull
    @Override
    protected ObjectAnimator createAnimator() {
        ObjectAnimator animator = new ObjectAnimator();
        animator.setPropertyName(PROPERTY_NAME);
        return  animator;
    }

    @Override
    protected void updateValues(@NonNull ObjectAnimator animator) {
        boolean hasFrom = !Float.isNaN(from);
        boolean hasTo = !Float.isNaN(to);
        boolean hasBy = !Float.isNaN(by);

        float f = hasFrom ? from : getTargetNode().getRotation();
        float t = hasTo ? to : getTargetNode().getRotation();
        if (hasBy) {
            t = f + by;
        }
        animator.setFloatValues(f, t);
    }

}
