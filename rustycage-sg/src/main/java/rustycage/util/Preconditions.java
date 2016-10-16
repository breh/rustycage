package rustycage.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by breh on 9/9/16.
 */
public class Preconditions {

    private Preconditions() {}

    public static <T> T assertNotNull(@NonNull T value, @Nullable String name) {
        if (value == null) {
            throw new IllegalArgumentException("argument "+(name != null ? name : "")+" cannot be null");
        }
        return value;
    }

    public static void assertGenericTypeNotNull(@NonNull Object value, @Nullable String name) {
        if (value == null) {
            throw new IllegalArgumentException("argument "+(name != null ? name : "")+" cannot be null");
        }
    }

}
