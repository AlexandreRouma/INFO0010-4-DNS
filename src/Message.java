import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Vector;

public class Message {
    public Message() {}

    public Message(ByteBuffer data) throws Exception {
        decode(data);
    }

    public ByteBuffer encode() {
        // Prepare buffer in big endian mode
        ByteBuffer data = ByteBuffer.allocate(getSize());
        data.order(ByteOrder.BIG_ENDIAN);
        
        // Encode message ID
        data.putShort(id);

        // Encode flags
        short flags = 0;
        flags |= (isResp ? 1 : 0) << 15;
        flags |= (opcode.id & 0xF) << 11;
        flags |= (isAuth ? 1 : 0) << 10;
        flags |= (isTruncated ? 1 : 0) << 9;
        flags |= (recurseDesired ? 1 : 0) << 8;
        flags |= (recurseAvailable ? 1 : 0) << 7;
        flags |= rcode.id & 0xF;
        data.putShort(flags);

        data.putShort((short)questions.size());
        data.putShort((short)0);
        data.putShort((short)0);
        data.putShort((short)0);

        // Encode questions
        for (Question q : questions) {
            data.put(q.encode().array());
        }

        return data;
    }

    public void decode(ByteBuffer data) throws Exception {
        if (data.capacity() < SIZE) {
            throw new Exception("Invalid data length");
        }
        id = data.getShort();
        short flags = data.getShort();
        isResp = ((flags >> 15) & 1) == 1;
        opcode = Opcode.fromId((flags >> 11) & 0xF);
        rcode = ResponseCode.fromId(flags & 0xF);

        int qCount = data.getShort();
        int aCount = data.getShort();
        
        // Ingore authoritative and additional record counts.
        data.getShort();
        data.getShort();

        questions.clear();
        for (int i = 0; i < qCount; i++) {
            questions.add(new Question(data));
        }

        answers.clear();
        for (int i = 0; i < aCount; i++) {
            answers.add(new ResourceRecord(data));
        }
    }

    public int getSize() {
        // Base size
        int size = SIZE;

        // Add the size of all records
        for (Question q : questions) { size += q.getSize(); }
        for (ResourceRecord a : answers) { size += a.getSize(); }

        return size;
    }
    
    public short id = 0;
    public Opcode opcode = Opcode.QUERY;
    public boolean isResp = false;
    public boolean isAuth = false;
    public boolean isTruncated = false;
    public boolean recurseDesired = false;
    public boolean recurseAvailable = false;
    public ResponseCode rcode = ResponseCode.SUCCESS;

    public Vector<Question> questions = new Vector<Question>();
    public Vector<ResourceRecord> answers = new Vector<ResourceRecord>();

    public static final int SIZE = 12;
}
