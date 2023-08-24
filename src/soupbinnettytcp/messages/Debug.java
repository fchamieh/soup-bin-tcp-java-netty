package soupbinnettytcp.messages;

import soupbinnettytcp.common.Common;

public class Debug extends Message {

    public final String text;

    public Debug(String message) {
        super(Common.padAll(
                'D', 1, Common.PaddingDirection.Right,
                message, message.length(), Common.PaddingDirection.Right));
        this.text = message;
    }

    public Debug(byte[] bytes) {
        super(bytes);
        this.text = Common.toString(bytes, 1);
    }

}
