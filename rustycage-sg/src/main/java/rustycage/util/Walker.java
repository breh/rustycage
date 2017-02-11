package rustycage.util;

import android.support.annotation.NonNull;

import rustycage.BaseNode;
import rustycage.GroupNode;

/**
 * Created by breh on 2/10/17.
 */

public final class Walker {

    private Walker() {}


    public static void forEachLeaf(@NonNull GroupNode node, @NonNull Action action) {
        Preconditions.assertNotNull(node,"node");
        Preconditions.assertNotNull(action,"action");

        for (int i=node.size()-1; i >= 0; i--) {
            BaseNode childNode = node.get(i);
            if (childNode instanceof GroupNode) {
                forEachLeaf((GroupNode)childNode, action);
            } else {
                action.onNode(childNode);
            }
        }
    }


    public interface Action {
        void onNode(@NonNull BaseNode node);
    }

}
