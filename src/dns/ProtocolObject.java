package dns;

import java.nio.ByteBuffer;

/**
 * DNS Protocol Object.
 */
public interface ProtocolObject {
    /**
     * Serialize object to bytes.
     * @return Serialized bytes.
     */
    ByteBuffer serialize() throws Exception;

    /**
     * Deserialize object from bytes.
     * @param buf Serialized bytes.
     */
    void deserialize(ByteBuffer buf) throws Exception;

    /**
     * Get object size in bytes once serialized.
     * @return Object size in bytes.
     */
    int getSize();
}