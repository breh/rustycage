package rustycage;

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


    public GroupNode() {

    }


    public void addNode(@NonNull BaseNode node) {
        Preconditions.assertNotNull(node,"node");
        node.setParent(this);
        nodes.add(node);
        markDirty();
    }

    public boolean removeNode(@NonNull BaseNode node) {
        Preconditions.assertNotNull(node,"node");
        boolean removed = nodes.remove(node);
        markDirty();
        return removed;
    }

    public void addNode(int index, @NonNull BaseNode node) {
        Preconditions.assertNotNull(node,"node");
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
                    return (Attribute<T>)existingAttribute;
                }
            } // else just add the attribute at the end
            attributes.add(attribute);
        }
        return null;
    }

    public boolean removeAttribute(@NonNull Attribute<?> attribute) {
        Preconditions.assertGenericTypeNotNull(attribute, "attribute");
        if (attributes != null) {
            int size = attributes.size();
            for (int i=0; i < size; i++) {
                if (attributes.get(i).equals(attribute)) {
                    attributes.remove(i);
                    return true;
                }
            }
        } // else
        return false;
    }

    public boolean removeAttribute(Class<?> attributeClazz) {
        Preconditions.assertNotNull(attributeClazz, "attributeClazz");
        if (attributes != null) {
            int size = attributes.size();
            for (int i=0; i < size; i++) {
                if (attributes.get(i).getAttributeClass().equals(attributeClazz)) {
                    attributes.remove(i);
                    return true;
                }
            }
        } // else
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

    @Override
    public float getWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getHeight() {
        throw new UnsupportedOperationException();
    }
}
