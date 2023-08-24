package soupbinnettytcp.messages;

import java.util.Arrays;

import soupbinnettytcp.common.Common;

public class UnsequencedData extends Message {

    public final byte[] data;

    public UnsequencedData(byte[] messageBytes) {
        super(messageBytes);
        data = Arrays.copyOfRange(messageBytes, 1, bytes.length - 1);
    }

    public UnsequencedData(byte[] data, boolean forData) {
        super(new byte[data.length + 1]);
        super.bytes[0] = Common.SoupMessageTypes.UnsequencedData;
        for (int i = 1; i < data.length; i++)
            super.bytes[i] = data[i];
        this.data = data;
    }

}