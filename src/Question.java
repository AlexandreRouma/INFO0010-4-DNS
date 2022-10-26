import java.nio.ByteBuffer;

public class Question {
    public Question(ByteBuffer msgBuf) throws Exception {
        decode(msgBuf);
    }

    public Question(Name name, Type type, Class qclass) {
        this.name = name;
        this.type = type;
        this.qclass = qclass;
    }

    public void decode(ByteBuffer msgBuf) throws Exception {
        name = new Name(msgBuf);
        type = Type.fromId(msgBuf.getShort());
        qclass = Class.fromId(msgBuf.getShort());
    }

    public ByteBuffer encode() {
        ByteBuffer buf = ByteBuffer.allocate(getSize());
        buf.put(name.encode().array());
        buf.putShort((short)type.id);
        buf.putShort((short)qclass.id);
        return buf;
    }
    
    public int getSize() {
        return SIZE + name.getSize();
    }

    public Name name = null;
    public Type type = Type.A;
    public Class qclass = Class.IN;

    private static final int SIZE = 4;

}
