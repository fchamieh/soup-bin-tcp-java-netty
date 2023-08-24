package soupbinnettytcp.messages;

import soupbinnettytcp.common.Common;

public class ClientHeartbeat extends Message {

    public ClientHeartbeat(byte[] bytes) {
        super(bytes);
    }

    public ClientHeartbeat() {
        super(new byte[] { Common.SoupMessageTypes.ClientHeartbeat });
    }

}
