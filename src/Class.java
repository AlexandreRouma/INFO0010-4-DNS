import java.util.HashMap;
import java.util.Map;

public enum Class {
    IN      (1),
    CS      (2),
    CH      (3),
    AS      (4);
    
    public final int id;
    private static final Map<Integer, Class> BY_ID = new HashMap<>();
    
    static {
        for (Class o : values()) {
            BY_ID.put(o.id, o);
        }
    }

    private Class(int id) {
        this.id = id;
    }

    public static Class fromId(int id) {
        return BY_ID.get(id);
    }

    public static boolean idIsValid(int id) {
        return BY_ID.containsKey(id);
    }
}