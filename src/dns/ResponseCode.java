package dns;

import java.util.HashMap;
import java.util.Map;

/**
 * DNS Response Code.
 */
public enum ResponseCode {
    SUCCESS                 (0),
    ERROR_FORMAT            (1),
    ERROR_SERVER            (2),
    ERROR_NAME              (3),
    ERROR_NOT_IMPLEMENTED   (4),
    ERROR_REFUSED           (5);
    
    /**
     * DNS ID.
     */
    public final int id;

    private static final Map<Integer, ResponseCode> BY_ID = new HashMap<>();
    
    static {
        for (ResponseCode o : values()) {
            BY_ID.put(o.id, o);
        }
    }

    private ResponseCode(int id) {
        this.id = id;
    }

    /**
     * Get enum from ID.
     * @param id DNS ID.
     * @return ResponseCode enum.
     */
    public static ResponseCode fromId(int id) {
        return BY_ID.get(id);
    }

    /**
     * Check validity of ID.
     * @param id DNS ID.
     * @return ResponseCode of ID.
     */
    public static boolean idIsValid(int id) {
        return BY_ID.containsKey(id);
    }
}