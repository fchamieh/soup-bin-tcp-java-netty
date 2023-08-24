package soupbinnettytcp.messages;

import soupbinnettytcp.common.Common;

public class ServerHeartbeat extends Message {

    public ServerHeartbeat(byte[] bytes) {
        super(bytes);
    }

    public ServerHeartbeat() {
        super(new byte[] { Common.SoupMessageTypes.ServerHeartbeat });
    }
    
}
