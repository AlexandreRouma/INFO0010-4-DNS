import dns.*;
import dns.Class;

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
            Type qtype = Type.A;
            if (args.length == 3) {
                String qstr = args[2];
                if (!Type.nameIsValid(qstr)) {
                    System.err.println("Unknown question type.");
                    return;
                }
                qtype = Type.fromString(qstr);
            }

            // Print the question
            System.out.printf("Question (NS=%s, NAME=%s, TYPE=%s)\n", host, domain, qtype);
            
            // Perform DNS query
            Vector<Question> questions = new Vector<Question>();
            questions.add(new Question(new Name(domain), qtype, Class.IN));
            Vector<ResourceRecord> answers = DNS.query(host, 53, questions);

            // Print results
            for (ResourceRecord rr : answers) {
                System.out.printf("Answer %s\n", rr.toString());
            }
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