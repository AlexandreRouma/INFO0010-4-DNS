package dns;

import dns.exceptions.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * DNS Text Segment.
 */
public class Text implements ProtocolObject {
    public String txt;

    /**
     * Create text.
     * @param txt Text.
     */
    public Text(String txt) {
        this.txt = txt;
    }

    /**
     * Create text from serialized bytes.
     * @param buf Serialized bytes.
     */
    public Text(ByteBuffer buf) throws Exception {
        deserialize(buf);
    }

    public ByteBuffer serialize() {
        ByteBuffer data = ByteBuffer.allocate(getSize());
        data.order(ByteOrder.BIG_ENDIAN);
        data.put((byte)txt.length());
        data.put(txt.getBytes());
        return data;
    }

    public void deserialize(ByteBuffer buf) throws Exception {
        int len = (int)buf.get() & 0xFF;
        if (len > buf.remaining()) {
            throw new InvalidMessageException("Not enough data in buffer");
        }
        txt = new String(buf.array(), buf.position(), len);
        buf.position(buf.position() + len);
    }

    public int getSize() {
        return BASE_SIZE + txt.length();
    }

    void setText(String txt) {
        this.txt = txt;
    }

    public String toString() {
        return txt;
    }

    private static final int BASE_SIZE = 1;
}
