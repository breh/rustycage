package rustycage;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rustycage.impl.AttributesStack;
import rustycage.util.Preconditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by breh on 9/9/16.
 */
public class GroupNode extends BaseNode implements Iterable<BaseNode> {

    private final List<BaseNode> nodes = new ArrayList<>();
    private List<Attribute<?>> attributes;

    // bounds
    private final float[] bounds = new float[4];

    protected GroupNode() {
    }


    public void addNode(@NonNull BaseNode node) {
        Preconditions.assertNotNull(node,"node");
        if (node.getParent() != null) {
            throw new IllegalStateException("Node has already parent. Node: "+node+", parent: "+node.getParent());
        }
        node.setParent(this);
        nodes.add(node);
        markDirty();
    }

    public boolean removeNode(@NonNull BaseNode node) {
        Preconditions.assertNotNull(node,"node");
        boolean removed = nodes.remove(node);
        if (removed) {
            node.setParent(null);
        }
        markDirty();
        return removed;
    }

    public void addNode(int index, @NonNull BaseNode node) {
        Preconditions.assertNotNull(node,"node");
        node.setParent(this);
        nodes.add(index,node);
        markDirty();
    }

    public @Nullable <T> Attribute<T> setAttribute(@NonNull Attribute<T> attribute) {
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

    public boolean removeAttribute(@NonNull Attribute<?> attribute) {
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

    public boolean removeAttribute(Class<?> attributeClazz) {
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
    public void pushAttributes(@NonNull AttributesStack attributesStack) {
        if (attributes != null) {
            int size = attributes.size();
            for (int i = 0; i < size; i++) {
                Attribute attr = attributes.get(i);
                attributesStack.push(attr.getAttributeClass(),attr.getAttribute());
            }
        }
    }

    // FIXME - should not be public
    public void popAttributes(@NonNull AttributesStack attributesStack) {
        if (attributes != null) {
            int size = attributes.size();
            for (int i = 0; i < size; i++) {
                Attribute attr = attributes.get(i);
                attributesStack.pop(attr.getAttributeClass());
            }
        }
    }


    public int size() {
        return nodes.size();
    }

    public BaseNode get(int index) {
        return nodes.get(index);
    }

    public @NonNull Iterator<BaseNode> iterator() {
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
    public float getWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void onMarkedDirty() {
        bounds[0] = Float.NaN;
    }

    private void recomputeBoundsWhenDirty() {
        if (Float.NaN == bounds[0]) {
            // need to recompute bounds
            int size = nodes.size();
            for (int i=0; i < size; i++) {
                BaseNode child = nodes.get(i);
                float cbl = child.getLeft();
                float cbr = child.getRight();
                float cbt = child.getRight();
                float cbb = child.getRight();
                Matrix m = getMatrix();
                if (m != null) {
                    // need to transform the bounds
                    bo
                }
            }
        }
    }

    @Override
    public float getLeft() {
        recomputeBoundsWhenDirty();
        return bounds[0];
    }

    @Override
    public float getRight() {
        recomputeBoundsWhenDirty();
        return bounds[2];
    }

    @Override
    public float getTop() {
        recomputeBoundsWhenDirty();
        return bounds[1];
    }

    @Override
    public float getBottom() {
        recomputeBoundsWhenDirty();
        return bounds[3];
    }

    @Override
    void clearDirty() {
        if (isDirty()) {
            super.clearDirty();
            int size = nodes.size();
            for (int i=0; i < size; i++) {
                BaseNode n = nodes.get(i);
                n.clearDirty();
            }
        }
    }

    public static Builder create() {
        return new Builder();
    }


    public static class Builder extends BaseNode.Builder<Builder,GroupNode> {

        private Builder() {
            super(new GroupNode());
        }


        public Builder add(@NonNull BaseNode node) {
            getNode().addNode(node);
            return getBuilder();
        }

        public Builder add(@NonNull BaseNode... nodes) {
            for (BaseNode node: nodes) {
                getNode().addNode(node);
            }
            return getBuilder();
        }

        public Builder add(@NonNull BaseNode.Builder<?,?> nodeBuilder) {
            getNode().addNode(nodeBuilder.build());
            return getBuilder();
        }

        public Builder add(@NonNull BaseNode.Builder<?,?>... nodeBuilders) {
            for (BaseNode.Builder<?,?> nodeBuilder: nodeBuilders) {
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
