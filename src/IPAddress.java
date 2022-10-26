import java.nio.ByteBuffer;

public class IPAddress implements EncodableDecodable {
    public IPAddress(ByteBuffer buf) {
        decode(buf);
    }
    
    public ByteBuffer encode() {
        ByteBuffer buf = ByteBuffer.allocate(SIZE);
        buf.putInt(ip);
        return buf;
    }

    public void decode(ByteBuffer buf) {
        ip = buf.getInt();
    }

    public String toString() {
        return String.format("%d.%d.%d.%d", (ip >> 24) & 0xFF, (ip >> 16) & 0xFF, (ip >> 8) & 0xFF, ip & 0xFF);
    }
    
    public int getSize() { return SIZE; }

    public int ip;
    private static final int SIZE = 4;
}
