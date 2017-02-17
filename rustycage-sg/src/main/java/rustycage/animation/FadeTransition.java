package rustycage.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * Created by breh on 2/3/17.
 */

public final class FadeTransition extends AbstractTransition<FadeTransition, Animator> {

    private float from = Float.NaN;
    private float to = Float.NaN;
    private float by = Float.NaN;

    private static final String PROPERTY_NAME = "opacity";

    private FadeTransition(@NonNull SgNode targetNode) {
        super(targetNode);
    }

    public static FadeTransition create(@NonNull SgNode targetNode) {
        Preconditions.assertNotNull(targetNode,"targetNode");
        return new FadeTransition(targetNode);
    }

    public FadeTransition from(float opacity) {
        this.from = opacity;
        return getThisTransition();
    }


    public FadeTransition to(float opacity) {
        this.to = opacity;
        return getThisTransition();
    }

    public FadeTransition by(float opacity) {
        this.by = opacity;
        return getThisTransition();
    }


    @Override
    protected Animator build() {
        boolean hasFrom = !Float.isNaN(from);
        boolean hasTo = !Float.isNaN(to);
        boolean hasBy = !Float.isNaN(by);
        ObjectAnimator animator = new ObjectAnimator();
        animator.setPropertyName(PROPERTY_NAME);
        float f = hasFrom ? from : getTargetNode().getOpacity();
        float t = hasTo ? to : getTargetNode().getOpacity();
        if (hasBy) {
            t = f + by;
        }
        animator.setFloatValues(f, t);
        fill(animator);
        return animator;
    }

}
