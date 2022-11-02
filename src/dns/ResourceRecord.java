package dns;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * DNS Resource Record.
 */
public class ResourceRecord implements ProtocolObject {
    public Name name;
    public RecordType type;
    public AddressClass rclass;
    public int ttl;
    public ProtocolObject record;

    /**
     * Create Resourece Record.
     * @param name Domain Name
     * @param type Record type.
     * @param rclass Address class.
     * @param ttl Time to live.
     * @param record Record object.
     */
    public ResourceRecord(Name name, RecordType type, AddressClass rclass, int ttl, ProtocolObject record) {
        this.name = name;
        this.type = type;
        this.rclass = rclass;
        this.ttl = ttl;
        this.record = record;
    }

    /**
     * Create Resource Record from serialized bytes.
     * @param msgBuf Serialized bytes.
     * @throws Exception
     */
    public ResourceRecord(ByteBuffer msgBuf) throws Exception {
        deserialize(msgBuf);
    }

    public ByteBuffer serialize() throws Exception {
        ByteBuffer buf = ByteBuffer.allocate(getSize());
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.put(name.serialize());
        buf.putShort((short)type.id);
        buf.putShort((short)rclass.id);
        buf.putInt(ttl);
        buf.putShort((short)record.getSize());
        buf.put(record.serialize());
        return buf;
    }

    public void deserialize(ByteBuffer msgBuf) throws Exception {
        // Parse record header
        name = new Name(msgBuf);
        type = RecordType.fromId(msgBuf.getShort());
        rclass = AddressClass.fromId(msgBuf.getShort());
        ttl = msgBuf.getInt();
        int rdLength = msgBuf.getShort();
        
        // Decode data
        switch (type) {
            case A:
                record = new IPAddress(msgBuf);
                break;
            case CNAME:
            case NS:
            case PTR:
                record = new Name(msgBuf);
                break;
            case MX:
                record = new MX(msgBuf);
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
    
    public int getSize() {
        return BASE_SIZE + record.getSize();
    }

    public String toString() {
        return String.format("(TYPE=%s, TTL=%d, DATA=\"%s\")", type, ttl, record);
    }

    private static final int BASE_SIZE = 10;
}
