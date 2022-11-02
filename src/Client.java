import dns.*;

import java.nio.ByteBuffer;
import java.util.Vector;

public class Client {
    public static void main(String[] args) {
        try {
            // Verify that enough arguments were given
            if (args.length < 2 || args.length > 3) {
                System.err.println("java Client [host] [domain] [question]");
                return;
            }

            // Parse arguments
            String host = args[0];
            String domain = args[1];
            RecordType qtype = RecordType.A;
            if (args.length == 3) {
                String qstr = args[2];
                if (!RecordType.nameIsValid(qstr)) {
                    System.err.println("Unknown question type.");
                    return;
                }
                qtype = RecordType.fromString(qstr);
            }

            // Create question
            Vector<Question> questions = new Vector<Question>();
            questions.add(new Question(new Name(domain), qtype, AddressClass.IN));

            // Print the question
            System.out.printf("Question (NS=%s, %s)\n", host, questions.get(0));

            // Create client
            dns.Client client = new dns.Client(host, 53);

            // Perform DNS query
            Vector<ResourceRecord> answers = client.query(questions);

            // Close the client
            client.close();

            // Print results
            for (ResourceRecord rr : answers) {
                System.out.printf("Answer %s\n", rr.toString());
            }

            // Close the client
        }
        catch (Exception e) {
            e.printStackTrace();
        }   
    }

    public static void dumpByteBuffer(ByteBuffer buf) {
        for (byte b : buf.array()) {
            System.out.printf("%02X ", b);
        }
        System.out.printf("\n");
    }
}