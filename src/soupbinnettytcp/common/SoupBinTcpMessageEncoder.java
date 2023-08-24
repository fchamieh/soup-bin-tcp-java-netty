package soupbinnettytcp.common;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import soupbinnettytcp.messages.ClientHeartbeat;
import soupbinnettytcp.messages.Debug;
import soupbinnettytcp.messages.EndOfSession;
import soupbinnettytcp.messages.LoginAccepted;
import soupbinnettytcp.messages.LoginRejected;
import soupbinnettytcp.messages.LoginRequest;
import soupbinnettytcp.messages.LogoutRequest;
import soupbinnettytcp.messages.SequencedData;
import soupbinnettytcp.messages.ServerHeartbeat;
import soupbinnettytcp.messages.UnsequencedData;

public final class SoupBinTcpMessageEncoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> output) throws Exception {
        if (!input.isReadable())
            return;

        var bytes = new byte[input.readableBytes()];
        input.readBytes(bytes);
        var type = bytes[0];

        switch (type) {
            case Common.SoupMessageTypes.Debug:
                output.add(new Debug(bytes));
                break;
            case Common.SoupMessageTypes.LoginAccepted:
                output.add(new LoginAccepted(bytes));
                break;
            case Common.SoupMessageTypes.LoginRejected:
                output.add(new LoginRejected(bytes));
                break;
            case Common.SoupMessageTypes.SequencedData:
                output.add(new SequencedData(bytes));
                break;
            case Common.SoupMessageTypes.ServerHeartbeat:
                output.add(new ServerHeartbeat(bytes));
                break;
            case Common.SoupMessageTypes.EndOfSession:
                output.add(new EndOfSession(bytes));
                break;
            case Common.SoupMessageTypes.LoginRequest:
                output.add(new LoginRequest(bytes));
                break;
            case Common.SoupMessageTypes.UnsequencedData:
                output.add(new UnsequencedData(bytes));
                break;
            case Common.SoupMessageTypes.ClientHeartbeat:
                output.add(new ClientHeartbeat(bytes));
                break;
            case Common.SoupMessageTypes.LogoutRequest:
                output.add(new LogoutRequest(bytes));
                break;
            default:
                System.err.println("unknown message type " + type);
                break;
        }
    }

}
