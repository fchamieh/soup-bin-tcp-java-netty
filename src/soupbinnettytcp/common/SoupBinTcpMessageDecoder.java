package soupbinnettytcp.common;

import java.util.List;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import soupbinnettytcp.messages.Message;

public final class SoupBinTcpMessageDecoder extends MessageToMessageEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        var message = Unpooled.wrappedBuffer(msg.bytes);
        out.add(message);
    }

}
