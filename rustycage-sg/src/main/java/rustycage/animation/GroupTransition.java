package rustycage.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.SgNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by breh on 2/4/17.
 */

public final class GroupTransition extends AbstractTransition<GroupTransition,AnimatorSet> {

    private List<Animator> animators = new ArrayList<>();
    private final boolean parallel;

    protected GroupTransition(@Nullable SgNode node, boolean parallel) {
        super(node);
        this.parallel = parallel;
    }

    public GroupTransition add(Animator... anims) {
        if (anims != null) {
            for (int i=0; i < anims.length; i++) {
                animators.add(anims[i]);
            }
        }
        return getThisTransition();
    }


    public GroupTransition add(@NonNull AbstractTransition<?,?>... transitions) {
        if (transitions != null) {
            for (int i=0; i < transitions.length; i++) {
                Animator animator = transitions[i].build();
                animators.add(animator);
            }
        }
        return getThisTransition();
    }

    @NonNull
    @Override
    protected AnimatorSet build() {
        AnimatorSet animatorSet = new AnimatorSet();
        if (parallel) {
            animatorSet.playTogether(animators);
        } else {
            animatorSet.playSequentially(animators);
        }
        fill(animatorSet);
        return animatorSet;
    }


    public static GroupTransition createParallel(@Nullable SgNode sgNode) {
        return new GroupTransition(sgNode, true);
    }

    public static GroupTransition createParallel() {
        return new GroupTransition(null, true);
    }

    public static GroupTransition createSequential(@Nullable SgNode sgNode) {
        return new GroupTransition(sgNode, false);
    }

    public static GroupTransition createSequential() {
        return new GroupTransition(null, false);
    }

}
