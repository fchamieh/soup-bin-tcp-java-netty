package soupbinnettytcp.messages;

import soupbinnettytcp.common.Common;

public class LogoutRequest extends Message {

    public LogoutRequest() {
        super(new byte[] { Common.SoupMessageTypes.LogoutRequest });
    }

    public LogoutRequest(byte[] bytes) {
        super(bytes);
    }

}
