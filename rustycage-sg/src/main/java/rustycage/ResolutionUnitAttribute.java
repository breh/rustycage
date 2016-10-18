package rustycage;

import android.support.annotation.NonNull;

/**
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
