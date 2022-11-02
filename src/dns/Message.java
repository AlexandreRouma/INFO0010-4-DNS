package dns;

import dns.exceptions.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Vector;

/**
 * DNS Message
 */
public class Message implements ProtocolObject {
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

    /**
     * Create a Client to Server message.
     * @param opcode
     */
    public Message(Opcode opcode, short id, boolean recurseDesired, Vector<Question> questions) {
        this.opcode = opcode;
        this.id = id;
        this.recurseDesired = recurseDesired;
        this.questions.addAll(questions);
    }
    
    /**
     * Create a Server to Client message.
     * @param rcode
     */
    public Message(ResponseCode rcode, short id, boolean isAuth, boolean isTruncated, boolean recurseAvailable) {
        this.rcode = rcode;
        this.id = id;
        this.isAuth = isAuth;
        this.isTruncated = isTruncated;
        this.recurseAvailable = recurseAvailable;
    }

    /**
     * Create a message from serialized bytes.
     * @param data Serialized bytes.
     * @throws Exception
     */
    public Message(ByteBuffer data) throws Exception {
        deserialize(data);
    }

    public ByteBuffer serialize() {
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
            data.put(q.serialize().array());
        }

        return data;
    }

    public void deserialize(ByteBuffer data) throws Exception {
        if (data.capacity() < BASE_SIZE) {
            throw new InvalidMessageException("Invalid data length.");
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
        int size = BASE_SIZE;

        // Add up the size of all records
        for (Question q : questions) { size += q.getSize(); }
        for (ResourceRecord a : answers) { size += a.getSize(); }

        return size;
    }

    private static final int BASE_SIZE = 12;
}
