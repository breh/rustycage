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
 *
 * A node representing a set of nodes. Nodes are painted in the same order as they
 * are inserted in a group - i.e. the subsequent nodes are painted on tof of the
 * previously painted nodes.
 *
 * Created by breh on 9/9/16.
 */
public class SgGroup extends SgNode implements Iterable<SgNode> {

    private static final String TAG = "SgGroup";

    private final List<SgNode> nodes = new ArrayList<>();
    private List<Attribute<?>> attributes;

    protected SgGroup() {
    }


    /**
     * Finds element by given id in this group (and its subgroups). If there are multiple nodes
     * with the same id, the first found node is returned. The method used depth first search approach.
     * @param id - id of an node - cannot be null
     * @param nodeClass - a desired node class - cannot be null
     * @return found node or null if not found or if the node is not of correct type
     */
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


    /**
     * Adds a new node at the end of this group (so it gets painted on top of the nodes in the group)
     * @param node node to be added - cannot be null
     */
    public final void addNode(@NonNull SgNode node) {
        Preconditions.assertNotNull(node,"node");
        if (node.getParent() != null) {
            throw new IllegalStateException("Node has already parent. Node: "+node+", parent: "+node.getParent());
        }
        node.setParent(this);
        nodes.add(node);
        invalidateLocalBounds();
    }

    /**
     * Removes a specified node from the group.
     * @param node node to be removed - cannot be null
     * @return true of node has been removed, false if not found
     */
    public final boolean removeNode(@NonNull SgNode node) {
        Preconditions.assertNotNull(node,"node");
        boolean removed = nodes.remove(node);
        if (removed) {
            node.setParent(null);
        }
        invalidateLocalBounds();
        return removed;
    }

    /**
     * Adds a new node at a specified index.
     * @param index where the node should be added
     * @param node node to be added - cannot be null
     */
    public final void addNode(int index, @NonNull SgNode node) {
        Preconditions.assertNotNull(node,"node");
        node.setParent(this);
        nodes.add(index,node);
        invalidateLocalBounds();
    }

    /**
     * Moves a specified node to the front (e.g. to the last position in the group)
     * @param node node to be moved - cannot be null
     */
    public final void moveToFront(@NonNull SgNode node) {
        Preconditions.assertNotNull(node,"node");
        int index = nodes.indexOf(node);
        if (index >= 0) {
            nodes.remove(index);
            nodes.add(node);
        }
        invalidate();
    }


    /**
     * Attaches an attribute to this node. Attributes are properties shared by this group
     * and applied to all its children recursively.
     * @param attribute attribute to be added - cannot be null.
     * @param <T>
     * @return a previous value of the attribute of null if not any.
     */
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
                    invalidate();
                    return (Attribute<T>)existingAttribute;
                }
            } // else just add the attribute at the end
            attributes.add(attribute);
        }
        invalidate();
        return null;
    }

    /**
     * Removes a specific attribute from the node.
     * @param attribute attribute to be removed - cannot be null
     * @return true if the attribute was removed, false if not found
     */
    public final boolean removeAttribute(@NonNull Attribute<?> attribute) {
        Preconditions.assertGenericTypeNotNull(attribute, "attribute");
        if (attributes != null) {
            int size = attributes.size();
            for (int i=0; i < size; i++) {
                if (attributes.get(i).equals(attribute)) {
                    attributes.remove(i);
                    invalidate();
                    return true;
                }
            }
        } // else
        invalidate();
        return false;
    }

    /**
     * Removes attribute of given class from this node.
     * @param attributeClazz - a class of attribute to be removed - cannot be null
     * @return true if removed, false if not found
     */
    public final boolean removeAttribute(Class<?> attributeClazz) {
        Preconditions.assertNotNull(attributeClazz, "attributeClazz");
        if (attributes != null) {
            int size = attributes.size();
            for (int i=0; i < size; i++) {
                if (attributes.get(i).getAttributeClass().equals(attributeClazz)) {
                    invalidate();
                    attributes.remove(i);
                    return true;
                }
            }
        } // else
        invalidate();
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


    /**
     * Returns a size of this group
     * @return
     */
    public final int size() {
        return nodes.size();
    }

    /**
     * Gets node at a given index.
     * @param index - index. If out of bounds, throws {@link ArrayIndexOutOfBoundsException}
     * @return a node at given index
     */
    public final @NonNull
    SgNode get(int index) {
        return nodes.get(index);
    }

    /**
     * Gets a node at a given index with desired class
     * @param index - index. If out of bounds, throws {@link ArrayIndexOutOfBoundsException}
     * @param nodeClass - a desired class of a node - cannot be null
     * @param <T>
     * @return a found node, or null if the node is not of the desired class
     */
    public final @Nullable <T extends SgNode> T get(int index, @NonNull Class<T> nodeClass) {
        SgNode node = nodes.get(index);
        if (nodeClass.isInstance(node)) {
            return (T)node;
        } else {
            Log.w(TAG, "node at index:  "+index+", desired class: "+nodeClass.getSimpleName()+", obtained class: "+node.getClass().getSimpleName());
            return null;
        }
    }

    /**
     * Returns iterator for all nodes in this group
     * @return
     */
    public @NonNull Iterator<SgNode> iterator() {
        return nodes.iterator();
    }


    /**
     * Returns iteraator for all attributes in this node.
     * @return
     */
    public @Nullable Iterator<Attribute<?>> attributeIterator() {
        if (attributes != null) {
            return attributes.iterator();
        } else {
            return null;
        }
    }

    // bounds

    @Override
    protected void onInvalidated() {
        invalidateLocalBounds();
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
    void clearInvalidated() {
        if (isInvalidated()) {
            super.clearInvalidated();
            int size = nodes.size();
            for (int i=0; i < size; i++) {
                SgNode n = nodes.get(i);
                n.clearInvalidated();
            }
        }
    }


    /**
     * Searches a hit path for a touch event
     * @param nodeHitPath
     * @param touchPoint
     */
    void searchForHitPath(@NonNull SgNodeHitPath nodeHitPath, float[] touchPoint) {
        for (int i= nodes.size() - 1 ; i >= 0; i--) {
            SgNode node = nodes.get(i);
            boolean foundHitPath = node.findHitPath(nodeHitPath, touchPoint);
            if (foundHitPath) {
                // we are done
                break;
            }
        }
    }


    /**
     * Creates a new group builder
     * @return
     */
    public static Builder create() {
        return new Builder();
    }


    /**
     * A group builder class
     */
    public static class Builder extends SgNode.Builder<Builder, SgGroup> {

        private Builder() {
            super(new SgGroup());
        }


        /**
         * Adds a node to this group
         * @param node
         * @return
         */
        public Builder add(@NonNull SgNode node) {
            getNode().addNode(node);
            return getBuilder();
        }

        /**
         * Adds multiple nodes to this group
         * @param nodes
         * @return
         */
        public Builder add(@NonNull SgNode... nodes) {
            for (SgNode node: nodes) {
                getNode().addNode(node);
            }
            return getBuilder();
        }

        /**
         * Adss a node builder to this group
         * @param nodeBuilder
         * @return
         */
        public Builder add(@NonNull SgNode.Builder<?,?> nodeBuilder) {
            getNode().addNode(nodeBuilder.build());
            return getBuilder();
        }

        /**
         * Adds multiple node builders to this group
         * @param nodeBuilders
         * @return
         */
        public Builder add(@NonNull SgNode.Builder<?,?>... nodeBuilders) {
            for (SgNode.Builder<?,?> nodeBuilder: nodeBuilders) {
                getNode().addNode(nodeBuilder.build());
            }
            return getBuilder();
        }

        /**
         * Sets a specific attribute on this group
         * @param attribute
         * @return
         */
        public Builder attribute(@NonNull Attribute<?> attribute) {
            getNode().setAttribute(attribute);
            return getBuilder();
        }

        /**
         * Sets a specific attribute builder on this group
         * @param attributeBuilder
         * @return
         */
        public Builder attribute(@NonNull Attribute.AttributeBuilder<?> attributeBuilder) {
            getNode().setAttribute(attributeBuilder.buildAttribute());
            return getBuilder();
        }

    }

}
