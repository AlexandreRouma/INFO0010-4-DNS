package dns;

import dns.exceptions.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * DNS Name Pointer.
 */
public class Pointer implements ProtocolObject {
    public static final short SIGNATURE = (short)0xC000;
    public static final byte BYTE_SIGNATURE = (byte)0xC0;
    public static final short MASK = (short)0xC000;
    public static final byte BYTE_MASK = (byte)0xC0;

    public short index;

    /**
     * Create pointer.
     * @param index Byte index of the name being pointed to.
     */
    public Pointer(short index) {
        this.index = index;
    }

    /**
     * Create pointer from serialized bytes.
     * @param msgBuf Serialized bytes.
     * @throws Exception
     */
    public Pointer(ByteBuffer msgBuf) throws Exception {
        deserialize(msgBuf);
    }

    public ByteBuffer serialize() {
        ByteBuffer buf = ByteBuffer.allocate(SIZE);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putShort((short)(index | SIGNATURE));
        return buf;
    }

    public void deserialize(ByteBuffer msgBuf) throws Exception {
        if (!isPointer(msgBuf)) {
            throw new NotPointerException();
        }

        // Check validity
        int initPos = msgBuf.position();
        short val = (short)(msgBuf.getShort() & (~MASK));
        
        // Check for circular or forward depdence
        if (val >= initPos) {
            throw new InvalidMessageException("Possible circular pointer dependency detected.");
        }

        // Save index
        index = val;
    }

    public int getSize() {
        return SIZE;
    }

    public static boolean isPointer(ByteBuffer buf) {
        return (buf.get(buf.position()) & BYTE_MASK) == BYTE_SIGNATURE;
    }

    private static final int SIZE = 2;
}
