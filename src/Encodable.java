import java.nio.ByteBuffer;

public interface Encodable {
    public ByteBuffer encode() throws Exception;
    public int getSize();
}