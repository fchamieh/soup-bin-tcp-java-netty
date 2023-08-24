package soupbinnettytcp.messages;

import soupbinnettytcp.LoginDetails;
import soupbinnettytcp.common.Common;

public class LoginRequest extends Message {

    public final LoginDetails loginDetails;

    public LoginRequest(byte[] bytes) {
        super(bytes);

        loginDetails = new LoginDetails(
                Common.toString(bytes, 1, 7),
                Common.toString(bytes, 7, 17),
                Common.toString(bytes, 17, 27),
                Long.parseLong(Common.toString(bytes, 27, 47).trim()));
    }

    public LoginRequest(LoginDetails _loginDetails) {
        super(toBytes(_loginDetails));
        loginDetails = _loginDetails;
    }

    static byte[] toBytes(LoginDetails loginDetails) {
        return Common.padAll(
                'L', 1, Common.PaddingDirection.Left,
                loginDetails.username, 6, Common.PaddingDirection.Right,
                loginDetails.password, 10, Common.PaddingDirection.Right,
                loginDetails.requestedSession, 10, Common.PaddingDirection.Right,
                loginDetails.requestedSequenceNumber, 20, Common.PaddingDirection.Left);
    }

}
