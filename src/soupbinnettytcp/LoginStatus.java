package soupbinnettytcp;

public class LoginStatus {
    public final boolean success;
    public final char rejectReasonCode;

    private LoginStatus(boolean success_, char rejectReasonCode_) {
        success = success_;
        rejectReasonCode = rejectReasonCode_;
    }

    public static LoginStatus Successful = new LoginStatus(true, '-');
    public static LoginStatus NotAuthorised = new LoginStatus(false, 'A');
    public static LoginStatus SessionNotAvailable = new LoginStatus(false, 'S');
}
