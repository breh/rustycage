package rustycage;

/**
 *
 * An abstract class representing an attribute for the scene graph. An attribute
 * is immutable value which can be applied to a group. All children of that group "inherit" the
 * attribute value unless they override the value by their own attribute
 *
 * Created by breh on 10/16/16.
 */
public abstract class Attribute<T> {

    private final T attribute;

    /**
     * Constructs a new attribute with given value
     * @param attribute
     */
    protected Attribute(T attribute) {
        if (attribute == null) throw new IllegalArgumentException("attribute cannot be null");
        this.attribute = attribute;
    }

    /**
     * Gets attribute value
     * @return
     */
    public T getAttribute() {
        return attribute;
    }

    /**
     * Gets attribute class
     * @return
     */
    public abstract Class<T> getAttributeClass();


    /**
     * A helper for building attributes
     * @param <T>
     */
    public interface AttributeBuilder<T extends Attribute<?>> {

        public T buildAttribute();

    }

}
