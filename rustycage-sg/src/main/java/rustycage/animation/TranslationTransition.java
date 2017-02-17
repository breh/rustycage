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

public final class TranslationTransition extends AbstractTransition<TranslationTransition, Animator> {

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

    public static TranslationTransition create(@NonNull SgNode targetNode) {
        Preconditions.assertNotNull(targetNode,"targetNode");
        return new TranslationTransition(targetNode);
    }

    public TranslationTransition fromXY(float x, float y) {
        this.fromX = x;
        this.fromY = y;
        return getThisTransition();
    }


    public TranslationTransition fromX(float x) {
        this.fromX = x;
        return getThisTransition();
    }

    public TranslationTransition fromY(float y) {
        this.fromY = y;
        return getThisTransition();
    }

    public TranslationTransition toXY(float x, float y) {
        this.toX = x;
        this.toY = y;
        return getThisTransition();
    }

    public TranslationTransition toX(float x) {
        this.toX = x;
        return getThisTransition();
    }

    public TranslationTransition toY(float y) {
        this.toY = y;
        return getThisTransition();
    }


    public TranslationTransition byXY(float x, float y) {
        this.byX = x;
        this.byY = y;
        return getThisTransition();
    }

    public TranslationTransition byX(float x) {
        this.byX = x;
        return getThisTransition();
    }

    public TranslationTransition byY(float y) {
        this.byY = y;
        return getThisTransition();
    }


    @Override
    protected Animator build() {
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
        }
        Animator animator = null;
        if (xAnimator != null && yAnimator != null) {
            AnimatorSet animatorSet  = new AnimatorSet();
            animatorSet.playTogether(xAnimator, yAnimator);
            animator = animatorSet;
        } else if (xAnimator != null) {
            animator = xAnimator;
        } else if (yAnimator != null) {
            animator = yAnimator;
        } else {
            throw new IllegalStateException();
        }
        fill(animator);
        return animator;
    }

}
