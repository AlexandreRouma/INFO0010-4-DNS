package dns;

import java.nio.ByteBuffer;

/**
 * DNS Question.
 */
public class Question implements ProtocolObject {
    public Name name = null;
    public Type type = Type.A;
    public Class qclass = Class.IN;

    /**
     * Create Question.
     * @param name Domain name.
     * @param type Question type.
     * @param qclass Address class.
     */
    public Question(Name name, Type type, Class qclass) {
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
        buf.put(name.serialize().array());
        buf.putShort((short)type.id);
        buf.putShort((short)qclass.id);
        return buf;
    }

    public void deserialize(ByteBuffer msgBuf) throws Exception {
        name = new Name(msgBuf);
        type = Type.fromId(msgBuf.getShort());
        qclass = Class.fromId(msgBuf.getShort());
    }
    
    public int getSize() {
        return BASE_SIZE + name.getSize();
    }

    private static final int BASE_SIZE = 4;

}
