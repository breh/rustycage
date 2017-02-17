package rustycage.animation;

import android.animation.Animator;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.SgNode;

/**
 * Created by breh on 2/3/17.
 */

public abstract class AbstractTransition<T extends AbstractTransition<T, A>, A extends Animator> {

    private SgNode targetNode;
    private int delay = Integer.MIN_VALUE;
    private int duration = Integer.MAX_VALUE;
    private A animator;

    protected AbstractTransition(@Nullable SgNode targetNode) {
        this.targetNode = targetNode;
    }

    protected final SgNode getTargetNode() {
        return targetNode;
    }

    public final T delay(int delay) {
        this.delay = delay;
        return getThisTransition();
    }

    protected final int getDelay() {
        return delay;
    }

    public final T duration(int duration) {
        this.duration = duration;
        return getThisTransition();
    }

    public final int getDuration() {
        return duration;
    }

    protected final T getThisTransition() {
        return (T)this;
    }

    public final void start() {
        if (animator == null) {
            animator = build();
        }
        animator.start();
    }

    protected abstract @NonNull A build();

    @CallSuper
    protected void fill(@NonNull A animator) {
        if (duration >= 0) {
            animator.setDuration(duration);
        }
        if (delay >= 0) {
            animator.setStartDelay(delay);
        }
        if (targetNode != null) {
            animator.setTarget(targetNode);
        }
    }

}
