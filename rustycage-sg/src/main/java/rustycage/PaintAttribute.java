package rustycage;

import android.graphics.Paint;
import android.support.annotation.NonNull;

import rustycage.Attribute;

/**
 * An attribute representing a paint. Can be applied to a group and then all children
 * of the group inherit this attribute (unless they override by their own value).
 *
 * Created by breh on 10/16/16.
 */
public class PaintAttribute extends Attribute<Paint> {

    public PaintAttribute(@NonNull Paint attribute) {
        super(attribute);
    }

    @Override
    public Class<Paint> getAttributeClass() {
        return Paint.class;
    }
}
