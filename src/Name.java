import java.nio.ByteBuffer;
import java.util.Vector;

public class Name implements EncodableDecodable {
    /**
     * Create name from string.
     * @param name Dot delimited domain name.
     */
    public Name(String name) {
        set(name);
    }

    /**
     * Create name from encoded bytes
     * @param msgBuf Buffer containing the entire message with its position at the name.
     */
    public Name(ByteBuffer msgBuf) throws Exception {
        decode(msgBuf);
    }

    /**
     * Encode name to bytes
     * @return Buffer containing the encoded name
     */
    public ByteBuffer encode() {
        ByteBuffer data = ByteBuffer.allocate(getSize());

        // Encode everything
        for (String s : labels) {
            data.put((byte)s.length());
            data.put(s.getBytes());
        }
        data.put((byte)0);

        return data;
    }

    /**
     * Decode from bytes
     * @param msgBuf Buffer containing the entire message with its position at the name.
     */
    public void decode(ByteBuffer msgBuf) throws Exception {
        decode(msgBuf, false);
    }

    private void decode(ByteBuffer msgBuf, boolean parsingPtr) throws Exception {
        // If not in the middle of parsing a pointer, clear labels
        if (!parsingPtr) { labels.clear(); }

        // Iterate until the entire string is read
        while (msgBuf.hasRemaining()) {
            // If the label is a pointer, decode label being pointed at and return
            try {
                Pointer ptr = new Pointer(msgBuf);
                decode(ByteBuffer.wrap(msgBuf.array()).position(ptr.index), true);
                return;
            }
            catch (NotPointerException e) {}

            // Get label length
            int len = msgBuf.get() & (~Pointer.BYTE_MASK);

            // If null, we're done
            if (len == 0) { return; }

            // TODO: Check that the buffer still has enough data
            
            // Get label string
            labels.add(new String(msgBuf.array(), msgBuf.position(), len));
            msgBuf.position(msgBuf.position() + len);
        }

        // If we exit the loop, it means we didn't get a null termination
        throw new MissingNullTerminationException();
    }

    /**
     * Set domain name.
     * @param name Dot-delimited domain name.
     */
    public void set(String name) {
        String[] parts = name.split("\\.");

        // TODO: Check validity

        // TODO: Cleaner way?
        labels.clear();
        for (String s : parts) { labels.add(s); }
    }

    /**
     * Get domain name
     * @return Dot-delimited domain name.
     */
    public String toString() {
        String str = "";
        for (String s : labels) {
            str += s + ".";
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * Get size of the encoded buffer.
     * @return Size in bytes.
     */
    public int getSize() {
        // Start bytes and terminating null
        int len = labels.size() + 1;

        // Labels
        for (String s : labels) {
            len += s.length();
        }

        return len;
    }

    private Vector<String> labels = new Vector<String>();
}
