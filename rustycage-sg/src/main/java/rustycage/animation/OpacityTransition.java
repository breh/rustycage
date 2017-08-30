package rustycage.animation;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * A transition for animating the opacity of given node
 *
 *
 * Created by breh on 2/3/17.
 */
public final class OpacityTransition extends AbstractTransition<OpacityTransition, ObjectAnimator> {

    private float from = Float.NaN;
    private float to = Float.NaN;
    private float by = Float.NaN;

    private static final String PROPERTY_NAME = "opacity";

    private OpacityTransition(@NonNull SgNode targetNode) {
        super(targetNode);
    }

    @NonNull
    public static OpacityTransition create(@NonNull SgNode targetNode) {
        Preconditions.assertNotNull(targetNode,"targetNode");
        return new OpacityTransition(targetNode);
    }

    @NonNull
    public OpacityTransition from(float opacity) {
        this.from = opacity;
        return getThisTransition();
    }

    @NonNull
    public OpacityTransition to(float opacity) {
        this.to = opacity;
        return getThisTransition();
    }

    @NonNull
    public OpacityTransition by(float opacity) {
        this.by = opacity;
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
        float f = hasFrom ? from : getTargetNode().getOpacity();
        float t = hasTo ? to : getTargetNode().getOpacity();
        //Log.d("FT","OpacityTransition: f:"+f+", t:"+t);
        if (hasBy) {
            t = f + by;
            //Log.d("FT","OpacityTransition.hasBy: f:"+f+", t:"+t);
        }
        animator.setFloatValues(f, t);
    }

}
