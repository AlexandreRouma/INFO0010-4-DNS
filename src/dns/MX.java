package dns;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * DNS MX Record.
 */
public class MX implements ProtocolObject {
    public short priority;
    public Name name;

    /**
     * Create MX record.
     * @param priority Record priority.
     * @param name Domain name.
     */
    public MX(short priority, Name name) {
        this.priority = priority;
        this.name = name;
    }

    /**
     * Create MX record from serialized bytes.
     * @param msgBuf Serialized bytes.
     */
    public MX(ByteBuffer msgBuf) throws Exception {
        deserialize(msgBuf);
    }

    public ByteBuffer serialize() {
        ByteBuffer buf = ByteBuffer.allocate(getSize());
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putShort(priority);
        buf.put(name.serialize());
        return buf;
    }

    public void deserialize(ByteBuffer msgBuf) throws Exception {
        priority = msgBuf.getShort();
        name = new Name(msgBuf);
    }

    public int getSize() {
        return SIZE + name.getSize();
    }

    public String toString() {
        return String.format("(PRIORITY=%d, NAME=%s)", priority, name);
    }

    private static final int SIZE = 2;
}
