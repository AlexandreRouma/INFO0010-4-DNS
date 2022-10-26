import java.util.HashMap;
import java.util.Map;

public enum ResponseCode {
    SUCCESS                 (0, "Success"),
    ERROR_FORMAT            (1, "Format Error"),
    ERROR_SERVER            (2, "Server Failure"),
    ERROR_NAME              (3, "Name Error"),
    ERROR_NOT_IMPLEMENTED   (4, "Not Implemented"),
    ERROR_REFUSED           (5, "Refused");
    
    public final int id;
    public final String name;
    private static final Map<Integer, ResponseCode> BY_ID = new HashMap<>();
    
    static {
        for (ResponseCode o : values()) {
            BY_ID.put(o.id, o);
        }
    }

    private ResponseCode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ResponseCode fromId(int id) {
        return BY_ID.get(id);
    }

    public static boolean idIsValid(int id) {
        return BY_ID.containsKey(id);
    }
}