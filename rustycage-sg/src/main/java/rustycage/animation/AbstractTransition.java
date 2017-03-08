package rustycage.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * Created by breh on 2/3/17.
 */

public abstract class AbstractTransition<T extends AbstractTransition<T, A>, A extends Animator> {

    private SgNode targetNode;
    private int delay = Integer.MIN_VALUE;
    private int duration = Integer.MIN_VALUE;
    private A animator;


    protected AbstractTransition(@Nullable SgNode targetNode) {
        this.targetNode = targetNode;
    }

    @Nullable
    protected final SgNode getTargetNode() {
        return targetNode;
    }

    @NonNull
    public final T delay(int delay) {
        this.delay = delay;
        return getThisTransition();
    }

    protected final int getDelay() {
        return delay;
    }

    @NonNull
    public final T duration(int duration) {
        this.duration = duration;
        return getThisTransition();
    }

    public final int getDuration() {
        return duration;
    }

    @NonNull
    protected final T getThisTransition() {
        return (T)this;
    }

    public final T start() {
        build().start();
        return getThisTransition();
    }


    public final void cancel() {
        if (animator != null) {
            animator.cancel();
        }
    }

    final A build() {
        if (animator == null) {
            createAnimator();
            fill(animator);
        }
        updateValues(animator);
        return animator;
    }

    protected abstract void updateValues(@NonNull A animator);

    @NonNull
    protected abstract A createAnimator();

    @NonNull
    private final A getAnimator() {
        if (animator == null) {
            animator = createAnimator();
        }
        return animator;
    }


    private void fill(@NonNull A animator) {
        Animator a = getAnimator();
        if (duration >= 0) {
            a.setDuration(duration);
        }
        if (delay >= 0) {
            a.setStartDelay(delay);
        }
        if (targetNode != null) {
            a.setTarget(targetNode);
        }
    }


    @NonNull
    public T onTransitionStarted(final @NonNull TransitionStartedListener listener) {
        Preconditions.assertNotNull(listener,"listener");
        getAnimator().addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onTransitionStarted(AbstractTransition.this);
            }
        });
        return getThisTransition();
    }

    public T onTransitionEnded(final @NonNull TransitionEndedListener listener) {
        Preconditions.assertNotNull(listener,"listener");
        getAnimator().addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onTransitionEnded(AbstractTransition.this);
            }
        });
        return getThisTransition();
    }

    public T onTransitionRepeated(final @NonNull TransitionRepeatedListener listener) {
        Preconditions.assertNotNull(listener,"listener");
        getAnimator().addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onTransitionRepeated(AbstractTransition.this);
            }
        });
        return getThisTransition();
    }


    public interface TransitionStartedListener {

        public void onTransitionStarted(@NonNull AbstractTransition<?,?> transition);

    }

    public interface TransitionEndedListener {

        public void onTransitionEnded(@NonNull AbstractTransition<?,?> transition);

    }

    public interface TransitionRepeatedListener {

        public void onTransitionRepeated(@NonNull AbstractTransition<?,?> transition);

    }

}
