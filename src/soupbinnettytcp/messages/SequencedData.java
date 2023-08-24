package soupbinnettytcp.messages;

import java.util.Arrays;

import soupbinnettytcp.common.Common;

public class SequencedData extends Message {

    public final byte[] data;

    public SequencedData(byte[] messageBytes) {
        super(messageBytes);
        data = Arrays.copyOfRange(messageBytes, 1, bytes.length - 1);
    }

    public SequencedData(byte[] data, boolean fromData) {
        super(new byte[data.length + 1]);
        super.bytes[0] = Common.SoupMessageTypes.SequencedData;
        for (int i = 1; i < data.length; i++)
            super.bytes[i] = data[i];
        this.data = data;
    }

}
