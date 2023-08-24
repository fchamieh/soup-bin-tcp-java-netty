package soupbinnettytcp.messages;
import soupbinnettytcp.common.Common;

public class LoginAccepted extends Message {

    public final String requestedSession;
    public final long requestedSequenceNumber;

    public LoginAccepted(byte[] bytes) {
        super(bytes);
        requestedSession = Common.toString(bytes, 3, 13).trim();
        requestedSequenceNumber = Long.parseLong(Common.toString(bytes, 13, 33).trim());
    }

    public LoginAccepted(String requestedSession, long requestedSequenceNumber) {
        super(toBytes(requestedSession, requestedSequenceNumber));
        this.requestedSession = requestedSession;
        this.requestedSequenceNumber = requestedSequenceNumber;
    }

    static byte[] toBytes(String requestedSession, long requestedSequenceNumber) {
        return Common.padAll(
            'A', 1, Common.PaddingDirection.Left,
            requestedSession, 10, Common.PaddingDirection.Right,
            requestedSequenceNumber, 20, Common.PaddingDirection.Left
        );
    }

    
}
