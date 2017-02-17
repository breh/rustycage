package rustycage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import rustycage.impl.AttributesStack;
import rustycage.util.Preconditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by breh on 9/9/16.
 */
public class SgGroup extends SgNode implements Iterable<SgNode> {

    private static final String TAG = "SgGroup";

    private final List<SgNode> nodes = new ArrayList<>();
    private List<Attribute<?>> attributes;

    protected SgGroup() {
    }


    public final @Nullable <T extends SgNode> T findById(@NonNull String id, @NonNull Class<T> nodeClass) {
        Preconditions.assertNotNull(id, "id");
        Preconditions.assertNotNull(nodeClass, "nodeClass");
        int size = nodes.size();
        for (int i=0; i < size; i++) {
            SgNode node = nodes.get(i);
            if (id.equals(node.getId())) {
                if (nodeClass.isInstance(node)) {
                    return (T)node;
                } else {
                    Log.w(TAG, "Found node with id: "+id+", desired class: "+nodeClass.getSimpleName()+", obtained class: "+node.getClass().getSimpleName());
                    return null;
                }
            }
            if (node instanceof SgGroup) {
                T foundNode = ((SgGroup)node).findById(id, nodeClass);
                if (foundNode != null) {
                    return foundNode;
                }
            }
        } // else
        return null;
    }


    public final void addNode(@NonNull SgNode node) {
        Preconditions.assertNotNull(node,"node");
        if (node.getParent() != null) {
            throw new IllegalStateException("Node has already parent. Node: "+node+", parent: "+node.getParent());
        }
        node.setParent(this);
        nodes.add(node);
        markLocalBoundsDirty();
    }

    public final boolean removeNode(@NonNull SgNode node) {
        Preconditions.assertNotNull(node,"node");
        boolean removed = nodes.remove(node);
        if (removed) {
            node.setParent(null);
        }
        markLocalBoundsDirty();
        return removed;
    }

    public final void addNode(int index, @NonNull SgNode node) {
        Preconditions.assertNotNull(node,"node");
        node.setParent(this);
        nodes.add(index,node);
        markLocalBoundsDirty();
    }

    public final void moveToFront(@NonNull SgNode node) {
        Preconditions.assertNotNull(node,"node");
        int index = nodes.indexOf(node);
        if (index >= 0) {
            nodes.remove(index);
            nodes.add(node);
        }
        markDirty();
    }

    public final @Nullable <T> Attribute<T> setAttribute(@NonNull Attribute<T> attribute) {
        Preconditions.assertGenericTypeNotNull(attribute, "attribute");
        if (attributes == null) {
            attributes = new ArrayList<>();
            attributes.add(attribute);
        } else {
            // find if there is an existing one of given type and replace
            int size = attributes.size();
            Class<T> atributeClazz = attribute.getAttributeClass();
            for (int i=0; i < size; i++) {
                Attribute<?> existingAttribute = attributes.get(i);
                if (existingAttribute.getClass().equals(atributeClazz)) {
                    attributes.set(i,attribute);
                    markDirty();
                    return (Attribute<T>)existingAttribute;
                }
            } // else just add the attribute at the end
            attributes.add(attribute);
        }
        markDirty();
        return null;
    }

    public final boolean removeAttribute(@NonNull Attribute<?> attribute) {
        Preconditions.assertGenericTypeNotNull(attribute, "attribute");
        if (attributes != null) {
            int size = attributes.size();
            for (int i=0; i < size; i++) {
                if (attributes.get(i).equals(attribute)) {
                    attributes.remove(i);
                    markDirty();
                    return true;
                }
            }
        } // else
        markDirty();
        return false;
    }

    public final boolean removeAttribute(Class<?> attributeClazz) {
        Preconditions.assertNotNull(attributeClazz, "attributeClazz");
        if (attributes != null) {
            int size = attributes.size();
            for (int i=0; i < size; i++) {
                if (attributes.get(i).getAttributeClass().equals(attributeClazz)) {
                    markDirty();
                    attributes.remove(i);
                    return true;
                }
            }
        } // else
        markDirty();
        return false;
    }

    // FIXME - should not be public
    public final void pushAttributes(@NonNull AttributesStack attributesStack) {
        if (attributes != null) {
            int size = attributes.size();
            for (int i = 0; i < size; i++) {
                Attribute attr = attributes.get(i);
                attributesStack.push(attr.getAttributeClass(),attr.getAttribute());
            }
        }
    }

    // FIXME - should not be public
    public final void popAttributes(@NonNull AttributesStack attributesStack) {
        if (attributes != null) {
            int size = attributes.size();
            for (int i = 0; i < size; i++) {
                Attribute attr = attributes.get(i);
                attributesStack.pop(attr.getAttributeClass());
            }
        }
    }


    public final int size() {
        return nodes.size();
    }

    public final @NonNull
    SgNode get(int index) {
        return nodes.get(index);
    }

    public final @Nullable <T extends SgNode> T get(int index, @NonNull Class<T> nodeClass) {
        SgNode node = nodes.get(index);
        if (nodeClass.isInstance(node)) {
            return (T)node;
        } else {
            Log.w(TAG, "node at index:  "+index+", desired class: "+nodeClass.getSimpleName()+", obtained class: "+node.getClass().getSimpleName());
            return null;
        }
    }

    public @NonNull Iterator<SgNode> iterator() {
        return nodes.iterator();
    }

    public @Nullable Iterator<Attribute<?>> attributeIterator() {
        if (attributes != null) {
            return attributes.iterator();
        } else {
            return null;
        }
    }

    // bounds

    @Override
    protected void onMarkedDirty() {
        markLocalBoundsDirty();
    }

    @Override
    protected void computeLocalBounds(@NonNull float[] bounds) {
        int size = nodes.size();
        float left = Float.MAX_VALUE;
        float top = Float.MAX_VALUE;
        float right = Float.MIN_VALUE;
        float bottom = Float.MIN_VALUE;
        for (int i=0; i < size; i++) {
            SgNode n = nodes.get(i);
            float nodeLeft = n.getTransformedBoundsLeft();
            float nodeTop = n.getTransformedBoundsTop();
            float nodeRight = n.getTransformedBoundsRight();
            float nodeBottom = n.getTransformedBoundsBottom();
            if (nodeLeft < left) {
                left = nodeLeft;
            }
            if (nodeTop < top) {
                top = nodeTop;
            }
            if (nodeRight > right) {
                right = nodeRight;
            }
            if (nodeBottom > bottom) {
                bottom = nodeBottom;
            }
        }
        bounds[0] = left;
        bounds[1] = top;
        bounds[2] = right;
        bounds[3] = bottom;
    }


    @Override
    void clearDirty() {
        if (isDirty()) {
            super.clearDirty();
            int size = nodes.size();
            for (int i=0; i < size; i++) {
                SgNode n = nodes.get(i);
                n.clearDirty();
            }
        }
    }




    void searchForHitPath(@NonNull NodeHitPath nodeHitPath, float[] touchPoint) {
        for (int i= nodes.size() - 1 ; i >= 0; i--) {
            SgNode node = nodes.get(i);
            boolean foundHitPath = node.findHitPath(nodeHitPath, touchPoint);
            if (foundHitPath) {
                // we are done
                break;
            }
        }
    }


    public static Builder create() {
        return new Builder();
    }


    public static class Builder extends SgNode.Builder<Builder, SgGroup> {

        private Builder() {
            super(new SgGroup());
        }


        public Builder add(@NonNull SgNode node) {
            getNode().addNode(node);
            return getBuilder();
        }

        public Builder add(@NonNull SgNode... nodes) {
            for (SgNode node: nodes) {
                getNode().addNode(node);
            }
            return getBuilder();
        }

        public Builder add(@NonNull SgNode.Builder<?,?> nodeBuilder) {
            getNode().addNode(nodeBuilder.build());
            return getBuilder();
        }

        public Builder add(@NonNull SgNode.Builder<?,?>... nodeBuilders) {
            for (SgNode.Builder<?,?> nodeBuilder: nodeBuilders) {
                getNode().addNode(nodeBuilder.build());
            }
            return getBuilder();
        }

        public Builder attribute(@NonNull Attribute<?> attribute) {
            getNode().setAttribute(attribute);
            return getBuilder();
        }

    }

}
