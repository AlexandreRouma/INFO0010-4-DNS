import java.nio.ByteBuffer;

public class Text implements EncodableDecodable {
    /**
     * Create text from string.
     * @param txt Text
     */
    public Text(String txt) {
        this.txt = txt;
    }

    /**
     * Create text from encoded bytes
     * @param buf Encoded text
     */
    public Text(ByteBuffer buf) throws Exception {
        decode(buf);
    }

    /**
     * Encode name to bytes
     * @return Buffer containing the encoded name
     */
    public ByteBuffer encode() {
        ByteBuffer data = ByteBuffer.allocate(getSize());
        data.put((byte)txt.length());
        data.put(txt.getBytes());
        return data;
    }

    /**
     * Decode from bytes
     * @param buf Buffer containing the encoded txt record data.
     */
    public void decode(ByteBuffer buf) throws Exception {
        int len = (int)buf.get() & 0xFF;
        if (len > buf.remaining()) {
            throw new Exception("Not enough data in buffer");
        }
        txt = new String(buf.array(), buf.position(), len);
        buf.position(buf.position() + len);
    }

    /**
     * Get domain name
     * @return Dot-delimited domain name.
     */
    public String toString() {
        return txt;
    }

    /**
     * Get size of the encoded buffer.
     * @return Size in bytes.
     */
    public int getSize() {
        return txt.length() + 1;
    }

    public String txt;
}
