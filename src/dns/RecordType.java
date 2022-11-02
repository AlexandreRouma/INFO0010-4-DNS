package dns;

import java.util.HashMap;
import java.util.Map;

/**
 * DNS Record Type.
 */
public enum RecordType {
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

    private static final Map<Integer, RecordType> BY_ID = new HashMap<>();
    private static final Map<String, RecordType> BY_NAME = new HashMap<>();
    
    static {
        for (RecordType o : values()) {
            BY_ID.put(o.id, o);
            BY_NAME.put(o.name(), o);
        }
    }

    private RecordType(int id) {
        this.id = id;
    }

    /**
     * Get enum from name.
     * @param id Name of enum.
     * @return RecordType enum.
     */
    public static RecordType fromString(String name) {
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
    public static RecordType fromId(int id) {
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