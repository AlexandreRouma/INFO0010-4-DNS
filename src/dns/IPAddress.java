package dns;

import java.nio.ByteBuffer;

/**
 * DNS IP Address
 */
public class IPAddress implements ProtocolObject {
    public int ip;

    /**
     * Create IP Address from integer representation.
     * @param buf IP Address in integer representation.
     */
    public IPAddress(int ip) {
        this.ip = ip;
    }

    /**
     * Create IP Address from serialized bytes.
     * @param buf Serialized bytes.
     */
    public IPAddress(ByteBuffer buf) {
        deserialize(buf);
    }
    
    public ByteBuffer serialize() {
        ByteBuffer buf = ByteBuffer.allocate(SIZE);
        buf.putInt(ip);
        return buf;
    }

    public void deserialize(ByteBuffer buf) {
        ip = buf.getInt();
    }

    public int getSize() { return SIZE; }

    public String toString() {
        return String.format("%d.%d.%d.%d", (ip >> 24) & 0xFF, (ip >> 16) & 0xFF, (ip >> 8) & 0xFF, ip & 0xFF);
    }
    
    private static final int SIZE = 4;
}
