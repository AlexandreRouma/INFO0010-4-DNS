import java.nio.ByteBuffer;

public class MX implements EncodableDecodable {
    public MX(short priority, Name name) {
        this.priority = priority;
        this.name = name;
    }

    public MX(ByteBuffer msgBuf) throws Exception {
        decode(msgBuf);
    }

    public ByteBuffer encode() {
        ByteBuffer buf = ByteBuffer.allocate(getSize());
        buf.putShort(priority);
        buf.put(name.encode());
        return buf;
    }

    public void decode(ByteBuffer msgBuf) throws Exception {
        priority = msgBuf.getShort();
        name = new Name(msgBuf);
    }

    public int getSize() {
        return SIZE + name.getSize();
    }

    public String toString() {
        return String.format("(PRIORITY=%d, NAME=%s)", priority, name);
    }

    public short priority;
    public Name name;
    private static final int SIZE = 2;
}
