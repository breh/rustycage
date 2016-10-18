package rustycage;

import rustycage.util.Preconditions;

/**
 * Created by breh on 10/16/16.
 */
public abstract class Attribute<T> {

    private final T attribute;

    protected Attribute(T attribute) {
        if (attribute == null) throw new IllegalArgumentException("attribute cannot be null");
        this.attribute = attribute;
    }

    public T getAttribute() {
        return attribute;
    }

    public abstract Class<T> getAttributeClass();

}
