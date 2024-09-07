package todo.util;

import java.util.UUID;

public class UUIDUtil {

    private UUIDUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
