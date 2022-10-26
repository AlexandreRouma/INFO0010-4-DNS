import java.util.HashMap;
import java.util.Map;

public enum Opcode {
    QUERY   (0),
    IQUERY  (1),
    STATUS  (2);
    
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

    public static Opcode fromId(int id) {
        return BY_ID.get(id);
    }

    public static boolean idIsValid(int id) {
        return BY_ID.containsKey(id);
    }
}