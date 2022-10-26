import java.nio.ByteBuffer;

public class ResourceRecord implements Encodable, Decodable {
    public ResourceRecord(ByteBuffer data) throws Exception {
        decode(data);
    }

    public ByteBuffer encode() throws Exception {
        ByteBuffer buf = ByteBuffer.allocate(getSize());
        buf.put(name.encode());
        buf.putShort((short)type.id);
        buf.putShort((short)rclass.id);
        buf.putInt(ttl);
        buf.putShort((short)record.getSize());
        buf.put(record.encode());
        return buf;
    }

    public void decode(ByteBuffer msgBuf) throws Exception {
        // Parse record header
        name = new Name(msgBuf);
        type = Type.fromId(msgBuf.getShort());
        rclass = Class.fromId(msgBuf.getShort());
        ttl = msgBuf.getInt();
        int rdLength = msgBuf.getShort();
        
        // Decode data
        switch (type) {
            case A:
                record = new IPAddress(msgBuf);
                break;
            case CNAME:
            case NS:
                record = new Name(msgBuf);
                break;
            case TXT:
                record = new Text(msgBuf);
                break;
            default:
                //record = msgBuf.slice();
                msgBuf.position(msgBuf.position() + rdLength);
                break;
        }
    }
    
    public String toString() {
        return String.format("(TYPE=%s, TTL=%d, DATA=\"%s\")", type.name(), ttl, record);
    }

    public int getSize() {
        return SIZE + record.getSize();
    }

    public Name name;
    public Type type;
    public Class rclass;
    public int ttl;
    public EncodableDecodable record = null;

    private static final int SIZE = 10;
}
