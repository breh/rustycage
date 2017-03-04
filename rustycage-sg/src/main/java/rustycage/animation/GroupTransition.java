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

    private List<AbstractTransition<?,?>> children = new ArrayList<>();
    private final boolean parallel;
    private boolean needAddingChildren = true;

    protected GroupTransition(@Nullable SgNode node, boolean parallel) {
        super(node);
        this.parallel = parallel;
    }

    public GroupTransition add(@NonNull AbstractTransition<?,?>... transitions) {
        if (transitions != null) {
            for (int i=0; i < transitions.length; i++) {
                children.add(transitions[i]);
            }
        }
        return getThisTransition();
    }


    @NonNull
    @Override
    protected AnimatorSet createAnimator() {
        return new AnimatorSet();
    }

    @Override
    protected void updateValues(@NonNull AnimatorSet animatorSet) {
        int size = children.size();
        if (needAddingChildren) {
            List<Animator> animators = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                animators.add(children.get(i).build());
            }
            if (parallel) {
                animatorSet.playTogether(animators);
            } else {
                animatorSet.playSequentially(animators);
            }
            needAddingChildren = false;
        } else {
            // just rebuild the children
            for (int i = 0; i < size; i++) {
                children.get(i).build();
            }

        }
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
