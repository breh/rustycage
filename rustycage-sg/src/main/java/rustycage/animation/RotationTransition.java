package rustycage.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;

import rustycage.BaseNode;
import rustycage.util.Preconditions;

/**
 * Created by breh on 2/3/17.
 */

public final class RotationTransition extends AbstractTransition<RotationTransition, Animator> {

    private float from = Float.NaN;
    private float to = Float.NaN;
    private float by = Float.NaN;

    private static final String PROPERTY_NAME = "rotation";

    private RotationTransition(@NonNull BaseNode targetNode) {
        super(targetNode);
    }

    public static RotationTransition create(@NonNull BaseNode targetNode) {
        Preconditions.assertNotNull(targetNode,"targetNode");
        return new RotationTransition(targetNode);
    }

    public RotationTransition from(float r) {
        this.from = r;
        return getThisTransition();
    }


    public RotationTransition to(float r) {
        this.to = r;
        return getThisTransition();
    }

    public RotationTransition by(float r) {
        this.by = r;
        return getThisTransition();
    }


    @Override
    protected Animator build() {
        boolean hasFrom = !Float.isNaN(from);
        boolean hasTo = !Float.isNaN(to);
        boolean hasBy = !Float.isNaN(by);
        ObjectAnimator animator = new ObjectAnimator();
        animator.setPropertyName(PROPERTY_NAME);
        float f = hasFrom ? from : getTargetNode().getRotation();
        float t = hasTo ? to : getTargetNode().getRotation();
        if (hasBy) {
            t = f + by;
        }
        animator.setFloatValues(f, t);
        fill(animator);
        return animator;
    }

}
