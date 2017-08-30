package rustycage;

import android.support.annotation.NonNull;

/**
 * Represents resolution unit as an attribute. By assigning a resolution attribute
 * all nodes in the group "inherit" the value and use the specified units to render.
 *
 * Created by breh on 10/16/16.
 */
public class ResolutionUnitAttribute extends Attribute<ResolutionUnit> {

    public ResolutionUnitAttribute(@NonNull ResolutionUnit resolutionUnit) {
        super(resolutionUnit);
    }

    @Override
    public Class<ResolutionUnit> getAttributeClass() {
        return ResolutionUnit.class;
    }
}
