package dns;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * DNS Question.
 */
public class Question implements ProtocolObject {
    public Name name = null;
    public RecordType type = RecordType.A;
    public AddressClass qclass = AddressClass.IN;

    /**
     * Create Question.
     * @param name Domain name.
     * @param type Question type.
     * @param qclass Address class.
     */
    public Question(Name name, RecordType type, AddressClass qclass) {
        this.name = name;
        this.type = type;
        this.qclass = qclass;
    }

    /**
     * Create Question from serialized bytes.
     * @param msgBuf Serialized bytes.
     * @throws Exception
     */
    public Question(ByteBuffer msgBuf) throws Exception {
        deserialize(msgBuf);
    }

    public ByteBuffer serialize() {
        ByteBuffer buf = ByteBuffer.allocate(getSize());
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.put(name.serialize().array());
        buf.putShort((short)type.id);
        buf.putShort((short)qclass.id);
        return buf;
    }

    public void deserialize(ByteBuffer msgBuf) throws Exception {
        name = new Name(msgBuf);
        type = RecordType.fromId(msgBuf.getShort());
        qclass = AddressClass.fromId(msgBuf.getShort());
    }
    
    public int getSize() {
        return BASE_SIZE + name.getSize();
    }

    public String toString() {
        return String.format("NAME=%s, TYPE=%s", name, type);
    }

    private static final int BASE_SIZE = 4;

}
