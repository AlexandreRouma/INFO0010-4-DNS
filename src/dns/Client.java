package dns;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.net.*;
import java.nio.*;

import dns.exceptions.ResponseErrorException;

public class Client {
    public Client(String host, int port) throws SocketException, IOException {
        this(host, port, DEFAULT_TIMEOUT);
    }

    public Client(String host, int port, int timeout) throws SocketException, IOException {
        setTimeout(timeout);
        connect(host, port);
    }

    public void connect(String host, int port) throws IOException {
        // Close in case connection is already open
        close();

        // Create socket and set parameters
        sock = new Socket();
        sock.setTcpNoDelay(true);
        sock.setSoTimeout(timeout);
        sock.connect(new InetSocketAddress(host, port), timeout);
    }

    public void close() throws IOException {
        // Close socket if it isn't already
        if (sock != null && isOpen()) {
            sock.close();
        }
    }

    public Vector<ResourceRecord> query(Vector<Question> questions) throws Exception {
        // Generate a random message ID
        short msgId = (short)rng.nextInt(Short.MAX_VALUE + 1);

        // Create message
        Message msg = new Message(Opcode.QUERY, msgId, true, questions);

        // Send request packet
        ByteBuffer mdata = msg.serialize();
        ByteBuffer data = ByteBuffer.allocate(mdata.capacity() + 2);
        data.order(ByteOrder.BIG_ENDIAN);
        data.putShort((short)mdata.capacity());
        data.put(mdata.array(), 0, mdata.capacity());
        sock.getOutputStream().write(data.array(), 0, data.capacity());
        sock.getOutputStream().flush();
        
        // Read the length first
        ByteBuffer lenBuf = ByteBuffer.allocate(2);
        lenBuf.order(ByteOrder.BIG_ENDIAN);
        sock.getInputStream().read(lenBuf.array());
        int msgLen = lenBuf.getShort();

        // Read the message itself
        byte[] buf = new byte[msgLen];
        sock.getInputStream().read(buf);

        // Decode response
        ByteBuffer rbuf = ByteBuffer.wrap(buf, 0, msgLen);
        Message rmsg = new Message(rbuf);

        // If query was unsucessful, throw exception
        if (rmsg.rcode != ResponseCode.SUCCESS) {
            throw new ResponseErrorException(rmsg.rcode);
        }

        return rmsg.answers;
    }

    public void setTimeout(int timeout) throws SocketException {
        this.timeout = timeout;
        if (sock != null && isOpen()) {
            sock.setSoTimeout(timeout);
        }
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean isOpen() {
        if (sock == null) {
            return false;
        }
        return !sock.isClosed();
    }

    private Socket sock;
    private int timeout = DEFAULT_TIMEOUT;
    Random rng = new Random();

    private static final int DEFAULT_TIMEOUT = 5000;
}
