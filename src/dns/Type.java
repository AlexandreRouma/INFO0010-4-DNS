package dns;

import java.util.HashMap;
import java.util.Map;

/**
 * DNS Record Type.
 */
public enum Type {
    A       (1),
    NS      (2),
    MD      (3),
    MF      (4),
    CNAME   (5),
    SOA     (6),
    MB      (7),
    MG      (8),
    MR      (9),
    NULL    (10),
    WKS     (11),
    PTR     (12),
    HINFO   (13),
    MINFO   (14),
    MX      (15),
    TXT     (16);
    
    /**
     * DNS ID.
     */
    public final int id;

    private static final Map<Integer, Type> BY_ID = new HashMap<>();
    private static final Map<String, Type> BY_NAME = new HashMap<>();
    
    static {
        for (Type o : values()) {
            BY_ID.put(o.id, o);
            BY_NAME.put(o.name(), o);
        }
    }

    private Type(int id) {
        this.id = id;
    }

    /**
     * Get enum from n ame.
     * @param id Name of enum.
     * @return Type enum.
     */
    public static Type fromString(String name) {
        return BY_NAME.get(name);
    }

    /**
     * Check validity of name.
     * @param name Name of enum.
     * @return Validity of name.
     */
    public static boolean nameIsValid(String name) {
        return BY_NAME.containsKey(name);
    }

    /**
     * Get enum from ID.
     * @param id DNS ID.
     * @return Class enum.
     */
    public static Type fromId(int id) {
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