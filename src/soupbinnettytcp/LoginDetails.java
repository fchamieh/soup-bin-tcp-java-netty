package soupbinnettytcp;

public class LoginDetails {

    public final long requestedSequenceNumber;
    public final String username;
    public final String password;
    public final String requestedSession;

    public LoginDetails(String username_, String password_, String requestedSession_, long requestedSequenceNumber_) {
        this.username = username_;
        this.password = password_;
        this.requestedSequenceNumber = requestedSequenceNumber_;
        this.requestedSession = requestedSession_;
    }

}
