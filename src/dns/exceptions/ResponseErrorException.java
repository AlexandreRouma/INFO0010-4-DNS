package dns.exceptions;

import dns.ResponseCode;

public class ResponseErrorException extends Exception {
    public ResponseErrorException(ResponseCode rcode) { super(rcode.toString()); }
}
