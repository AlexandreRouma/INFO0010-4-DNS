import java.nio.ByteBuffer;

public class Pointer {
    public Pointer(ByteBuffer msgBuf) throws Exception {
        decode(msgBuf);
    }

    public Pointer(short index) {
        this.index = index;
    }

    public ByteBuffer encode() {
        ByteBuffer buf = ByteBuffer.allocate(SIZE);
        buf.putShort((short)(index | SIGNATURE));
        return buf;
    }

    public void decode(ByteBuffer msgBuf) throws Exception {
        if (!isPointer(msgBuf)) {
            throw new NotPointerException();
        }

        // Check validity
        int initPos = msgBuf.position();
        short val = (short)(msgBuf.getShort() & (~MASK));
        
        // Check for circular or forward depdence
        if (val >= initPos) {
            throw new InvalidPointerDependencyException();
        }

        // Save index
        index = val;
    }

    public static boolean isPointer(ByteBuffer buf) {
        return (buf.getShort(buf.position()) & MASK) == SIGNATURE;
    }

    public static final int SIZE = 2;
    public static final short SIGNATURE = (short)0xC000;
    public static final short MASK = (short)0xC000;
    public static final byte BYTE_MASK = (byte)0xC0;
    public short index;
}
