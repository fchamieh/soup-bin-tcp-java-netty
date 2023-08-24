package soupbinnettytcp.messages;

import soupbinnettytcp.common.Common;

public class Message {
    public final byte[] bytes;
    public final char type;

    public Message(byte[] bytes) {
        this.bytes = bytes;
        if (bytes.length > 0)
            type = Common.toString(bytes, 0, 1).charAt(0);
        else
            type = '-';
    }
}
