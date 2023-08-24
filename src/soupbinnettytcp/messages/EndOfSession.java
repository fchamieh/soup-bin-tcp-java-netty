package soupbinnettytcp.messages;

import soupbinnettytcp.common.Common;

public class EndOfSession extends Message {

    public EndOfSession(byte[] bytes) {
        super(bytes);
    }
    
    public EndOfSession() {
        super(new byte[] { Common.SoupMessageTypes.EndOfSession });
    }
}
