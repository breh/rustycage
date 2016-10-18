package rustycage;

import android.util.TypedValue;

/**
 * Created by breh on 10/16/16.
 */
public enum ResolutionUnit {

    PX(TypedValue.COMPLEX_UNIT_PX),
    DP(TypedValue.COMPLEX_UNIT_DIP),
    SP(TypedValue.COMPLEX_UNIT_SP),
    PT(TypedValue.COMPLEX_UNIT_PT),
    MM(TypedValue.COMPLEX_UNIT_MM),
    IN(TypedValue.COMPLEX_UNIT_IN);


    private int typedValue;
    private ResolutionUnit(int typedValue) {
        this.typedValue = typedValue;
    }

    public int getTypedValue() {
        return typedValue;
    }
}
