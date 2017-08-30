package rustycage.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.SgNode;
import rustycage.util.Preconditions;

/**
 * A class representing a transition. A transition is a simple mechanism how to animate the
 * scene graph nodes. Transitions use Anroid's animation API to animate the individual nodes,
 * and the main goal is to have an expressive an very simple to use API (simpler than the
 * Android API)
 *
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

    /**
     * Gets node targetted by this tranition
     * @return
     */
    @Nullable
    protected final SgNode getTargetNode() {
        return targetNode;
    }

    /**
     * Specifies a delay in ms for this transition
     * @param delay
     * @return
     */
    @NonNull
    public final T delay(int delay) {
        this.delay = delay;
        return getThisTransition();
    }

    protected final int getDelay() {
        return delay;
    }

    /**
     * Specifies a duration for this transition (without the delay)
     * @param duration
     * @return
     */
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

    /**
     * Starts "playing" this transition / animation
     * @return
     */
    public final T start() {
        build().start();
        return getThisTransition();
    }

    /**
     * Cancels this transition
     */
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

    /**
     * The actual callback how to update values in the animations
     * @param animator
     */
    protected abstract void updateValues(@NonNull A animator);

    /**
     * Creates an animator object. Needs ot be implemented by individual transitions
     * @return
     */
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


    /**
     * Callback when this transition starts
     * @param listener
     * @return
     */
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

    /**
     * Callback when this transition ends
     * @param listener
     * @return
     */
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

    /**
     * Callback when this transition gets repetated
     * @param listener
     * @return
     */
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


    /**
     * Listener for transition started event
     */
    public interface TransitionStartedListener {

        public void onTransitionStarted(@NonNull AbstractTransition<?,?> transition);

    }

    /**
     * Listener for transition endedr event
     */
    public interface TransitionEndedListener {

        public void onTransitionEnded(@NonNull AbstractTransition<?,?> transition);

    }

    /**
     * Listener for transition repeated event
     */
    public interface TransitionRepeatedListener {

        public void onTransitionRepeated(@NonNull AbstractTransition<?,?> transition);

    }

}
