package dns;

import java.util.HashMap;
import java.util.Map;

/**
 * DNS Address Class
 */
public enum AddressClass {
    IN      (1),
    CS      (2),
    CH      (3),
    AS      (4);
    
    /**
     * DNS ID.
     */
    public final int id;

    private static final Map<Integer, AddressClass> BY_ID = new HashMap<>();
    
    static {
        for (AddressClass o : values()) {
            BY_ID.put(o.id, o);
        }
    }

    private AddressClass(int id) {
        this.id = id;
    }

    /**
     * Get enum from ID.
     * @param id DNS ID.
     * @return Class enum.
     */
    public static AddressClass fromId(int id) {
        return BY_ID.get(id);
    }

    /**
     * Check validity of ID.
     * @param id DNS ID.
     * @return Validity of ID.
     */
    public static boolean idIsValid(int id) {
        return BY_ID.containsKey(id);
    }
}