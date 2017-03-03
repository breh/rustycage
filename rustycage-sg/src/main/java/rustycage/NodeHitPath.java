package rustycage;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import rustycage.event.TouchEvent;

/**
 * Created by breh on 2/10/17.
 */

final class NodeHitPath {

    private static final int INITIAL_SIZE = 50; // depth of 50

    private float[] hitCoordinates = new float[2*INITIAL_SIZE];
    private List<SgNode> path = new ArrayList<>(INITIAL_SIZE);


    public void pushNode(@NonNull SgNode node, float localX, float localY) {
        int currentIndex = path.size();
        path.add(node);
        int coordinatesIndex = currentIndex*2;
        hitCoordinates = resizeIfNeeded(hitCoordinates, coordinatesIndex);
        hitCoordinates[coordinatesIndex] = localX;
        hitCoordinates[coordinatesIndex+1] = localY;
    }


    public int getDepth() {
        return path.size();
    }

    public @NonNull
    SgNode getNodeAt(int index) {
        return path.get(index);
    }

    public float getLocalX(int index) {
        return hitCoordinates[index*2];
    }

    public float getLocalY(int index) {
        return hitCoordinates[index*2+1];
    }

    public void clear() {
        path.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("NodeHitPath: { depth:").append(path.size()).append(", ");
        for (int i=0; i < path.size(); i++) {
            sb.append("node: ").append(getNodeAt(i))
                    .append(", localXY: [").append(getLocalX(i)).append(", ")
                    .append(getLocalY(i)).append("], ");
        }
        sb.append("}");
        return sb.toString();
    }


    public void deliverEvent(@NonNull MotionEvent originalEvent) {
        // capture
        int size = path.size();
        for (int i=0; i < size; i++) {
            SgNode node = path.get(i);
            if (node.hasCaptureListener()) {
                int coordinatesIndex = i*2;
                float localX = hitCoordinates[coordinatesIndex];
                float localY = hitCoordinates[coordinatesIndex+1];
                TouchEvent deliverEvent = node.getEventSupport().deliverEvent(originalEvent, localX, localY, true);
                if (deliverEvent != null && deliverEvent.isConsumed()) {
                    // we are done
                    return;
                }
            }
        }
        // bubble
        for (int i=size -1; i >= 0; i--) {
            SgNode node = path.get(i);
            if (node.hasBubbleListener()) {
                int coordinatesIndex = i*2;
                float localX = hitCoordinates[coordinatesIndex];
                float localY = hitCoordinates[coordinatesIndex+1];
                TouchEvent deliverEvent = node.getEventSupport().deliverEvent(originalEvent, localX, localY, false);
                if (deliverEvent != null && deliverEvent.isConsumed()) {
                    // we are done
                    return;
                }
            }
        }
    }


    /**
     * Resizes array if needed
     * @param currentArray
     * @param index
     * @return
     */
    private static @NonNull float[] resizeIfNeeded(@NonNull float[] currentArray, int index) {
        if (index >= currentArray.length) {
            float[] newArray = new float[currentArray.length * 2];
            System.arraycopy(currentArray, 0, newArray, 0, currentArray.length);
            return newArray;
        } else {
            return currentArray;
        }
    }


}
