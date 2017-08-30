package rustycage.animation;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * A transition representing rotation
 *
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

    /**
     * Sets from which rotation value this transition animates. Call is optional, in
     * such a case "current" value is used.
     * @param r
     * @return
     */
    @NonNull
    public RotationTransition from(float r) {
        this.from = r;
        return getThisTransition();
    }

    /**
     * Sets to which rotation value this transition animates. The value should be specified either
     * by this call or by "by" call.
     * @param r
     * @return
     */
    @NonNull
    public RotationTransition to(float r) {
        this.to = r;
        return getThisTransition();
    }

    /**
     * Sets by which rotation value this transition animates.
     * @param r
     * @return
     */
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
