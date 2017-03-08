package rustycage.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * Created by breh on 2/3/17.
 */

public final class TranslationTransition extends AbstractTransition<TranslationTransition, AnimatorSet> {

    private float fromX = Float.NaN;
    private float fromY = Float.NaN;
    private float toX = Float.NaN;
    private float toY = Float.NaN;
    private float byX = Float.NaN;
    private float byY = Float.NaN;

    private static final String X_PROPERTY_NAME = "translationX";
    private static final String Y_PROPERTY_NAME = "translationY";

    private TranslationTransition(@NonNull SgNode targetNode) {
        super(targetNode);
    }

    @NonNull
    public static TranslationTransition create(@NonNull SgNode targetNode) {
        Preconditions.assertNotNull(targetNode,"targetNode");
        return new TranslationTransition(targetNode);
    }

    @NonNull
    public TranslationTransition fromXY(float x, float y) {
        this.fromX = x;
        this.fromY = y;
        return getThisTransition();
    }

    @NonNull
    public TranslationTransition fromX(float x) {
        this.fromX = x;
        return getThisTransition();
    }

    @NonNull
    public TranslationTransition fromY(float y) {
        this.fromY = y;
        return getThisTransition();
    }

    @NonNull
    public TranslationTransition toXY(float x, float y) {
        this.toX = x;
        this.toY = y;
        return getThisTransition();
    }

    @NonNull
    public TranslationTransition toX(float x) {
        this.toX = x;
        return getThisTransition();
    }

    @NonNull
    public TranslationTransition toY(float y) {
        this.toY = y;
        return getThisTransition();
    }

    @NonNull
    public TranslationTransition byXY(float x, float y) {
        this.byX = x;
        this.byY = y;
        return getThisTransition();
    }

    @NonNull
    public TranslationTransition byX(float x) {
        this.byX = x;
        return getThisTransition();
    }

    @NonNull
    public TranslationTransition byY(float y) {
        this.byY = y;
        return getThisTransition();
    }


    @NonNull
    @Override
    protected AnimatorSet createAnimator() {
        return new AnimatorSet();
    }

    @NonNull
    @Override
    protected void updateValues(@NonNull AnimatorSet animator) {
        boolean hasFromX = !Float.isNaN(fromX);
        boolean hasToX = !Float.isNaN(toX);
        boolean hasFromY = !Float.isNaN(fromY);
        boolean hasToY = !Float.isNaN(toY);
        boolean hasByX = !Float.isNaN(byX);
        boolean hasByY = !Float.isNaN(byY);
        ObjectAnimator xAnimator = null;
        if (hasFromX || hasToX || hasByX) {
            xAnimator = new ObjectAnimator();
            xAnimator.setPropertyName(X_PROPERTY_NAME);
            float fx = hasFromX ? fromX : getTargetNode().getTranslationX();
            float tx = hasToX ? toX : getTargetNode().getTranslationX();
            if (hasByX) {
                tx = fx + byX;
            }
            xAnimator.setFloatValues(fx, tx);
            xAnimator.setTarget(getTargetNode());
        }
        ObjectAnimator yAnimator = null;
        if (hasFromY || hasToY || hasByY) {
            yAnimator = new ObjectAnimator();
            yAnimator.setPropertyName(Y_PROPERTY_NAME);
            float fy = hasFromY ? fromY : getTargetNode().getTranslationY();
            float ty = hasToY ? toY : getTargetNode().getTranslationY();
            if (hasByY) {
                ty = fy + byY;
            }
            yAnimator.setFloatValues(fy, ty);
            yAnimator.setTarget(getTargetNode());
        }
        if (xAnimator != null && yAnimator != null) {
            animator.playTogether(xAnimator, yAnimator);
        } else if (xAnimator != null) {
            animator.play(xAnimator);
        } else if (yAnimator != null) {
            animator.play(yAnimator);
        } else {
            throw new IllegalStateException();
        }
    }

}
