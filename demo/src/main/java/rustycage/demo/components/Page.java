package rustycage.demo.components;

import android.support.annotation.NonNull;

import rustycage.SgCustomNode;
import rustycage.SgGroup;
import rustycage.SgNode;
import rustycage.animation.AbstractTransition;
import rustycage.animation.OpacityTransition;

/**
 * Created by breh on 8/6/17.
 */

public class Page extends SgCustomNode {


    private SgGroup pageGroup = SgGroup.create().build();

    public Page() {

    }


    public void switchToNode(@NonNull SgNode node) {
        OpacityTransition nt = OpacityTransition.create(node).from(0f).to(1f).start();
        node.setOpacity(0f);
        if (pageGroup.size() > 0) {
            // get the current node
            final SgNode oldNode = pageGroup.get(0);
            // fade out
            OpacityTransition ot = OpacityTransition.create(oldNode).to(0f).start().onTransitionEnded(new AbstractTransition.TransitionEndedListener() {
                @Override
                public void onTransitionEnded(@NonNull AbstractTransition<?, ?> transition) {
                    pageGroup.removeNode(oldNode);
                }
            });
        }
        pageGroup.addNode(node);

    }

    @NonNull
    @Override
    protected SgNode createNode() {
        return pageGroup;
    }
}
