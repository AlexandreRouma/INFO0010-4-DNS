package dns;

import java.util.HashMap;
import java.util.Map;

/**
 * DNS Message Opcode.
 */
public enum Opcode {
    QUERY   (0),
    IQUERY  (1),
    STATUS  (2);
    
    /**
     * DNS ID.
     */
    public final int id;

    private static final Map<Integer, Opcode> BY_ID = new HashMap<>();
    
    static {
        for (Opcode o : values()) {
            BY_ID.put(o.id, o);
        }
    }

    private Opcode(int id) {
        this.id = id;
    }

    /**
     * Get enum from ID.
     * @param id DNS ID.
     * @return Opcode enum.
     */
    public static Opcode fromId(int id) {
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