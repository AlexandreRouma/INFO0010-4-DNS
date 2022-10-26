import java.nio.ByteBuffer;

public interface Decodable {
    public void decode(ByteBuffer buf) throws Exception;
}
