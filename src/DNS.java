import java.net.*;
import java.nio.*;
import java.util.Vector;

public class DNS {
    public static Vector<ResourceRecord> query(String host, int port, Vector<Question> questions) throws Exception {
        // Create message
        Message msg = new Message();
        msg.id = 0x4269;
        msg.opcode = Opcode.QUERY;
        msg.isAuth = false;
        msg.isResp = false;
        msg.isTruncated = false;
        msg.recurseDesired = true;
        msg.recurseAvailable = false;
        msg.rcode = ResponseCode.SUCCESS;
        msg.questions = questions;

        // Connect to DNS server
        Socket sock = new Socket(host, port);
        sock.setTcpNoDelay(true);
        sock.setSoTimeout(5000);

        // Send request packet
        ByteBuffer mdata = msg.encode();
        ByteBuffer data = ByteBuffer.allocate(mdata.capacity() + 2);
        data.putShort((short)mdata.capacity());
        data.put(mdata.array(), 0, mdata.capacity());
        sock.getOutputStream().write(data.array(), 0, data.capacity());
        sock.getOutputStream().flush();
        
        // Read the length first
        ByteBuffer lenBuf = ByteBuffer.allocate(2);
        sock.getInputStream().read(lenBuf.array());
        int msgLen = lenBuf.getShort();

        // Read the message itself
        byte[] buf = new byte[msgLen];
        sock.getInputStream().read(buf);

        // Decode response
        ByteBuffer rbuf = ByteBuffer.wrap(buf, 0, msgLen);
        Message rmsg = new Message(rbuf);

        // Close socket
        sock.close();

        return rmsg.answers;
    }
}
