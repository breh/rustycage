package rustycage.animation;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.util.Property;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 *
 * A transition for animating any "float" property of given node
 *
 *
 * Created by breh on 3/7/17.
 */
public class FloatPropertyTransition<T extends SgNode> extends AbstractTransition<FloatPropertyTransition<T>, ObjectAnimator> {


    private float from = Float.NaN;
    private float to = Float.NaN;
    private float by = Float.NaN;
    private final String propertyName;
    private final Property<T, Float> property;

    private FloatPropertyTransition(@NonNull T targetNode, @NonNull String propertyName) {
        super(targetNode);
        this.propertyName = propertyName;
        this.property = Property.of((Class<T>)targetNode.getClass(), float.class, propertyName);
    }

    @NonNull
    public static FloatPropertyTransition create(@NonNull SgNode targetNode, @NonNull String propertyName) {
        Preconditions.assertNotNull(targetNode, "targetNode");
        Preconditions.assertNotNull(propertyName, propertyName);
        return new FloatPropertyTransition(targetNode, propertyName);
    }

    @NonNull
    public FloatPropertyTransition from(float value) {
        this.from = value;
        return getThisTransition();
    }

    @NonNull
    public FloatPropertyTransition to(float value) {
        this.to = value;
        return getThisTransition();
    }

    @NonNull
    public FloatPropertyTransition by(float value) {
        this.by = value;
        return getThisTransition();
    }


    @NonNull
    @Override
    protected ObjectAnimator createAnimator() {
        ObjectAnimator animator = new ObjectAnimator();
        animator.setPropertyName(propertyName);
        return animator;
    }

    @Override
    protected void updateValues(@NonNull ObjectAnimator animator) {
        boolean hasFrom = !Float.isNaN(from);
        boolean hasTo = !Float.isNaN(to);
        boolean hasBy = !Float.isNaN(by);
        float f = hasFrom ? from : property.get((T)getTargetNode());
        float t = hasTo ? to : property.get((T)getTargetNode());
        if (hasBy) {
            t = f + by;
        }
        animator.setFloatValues(f, t);
    }

}


