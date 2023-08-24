package soupbinnettytcp.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import soupbinnettytcp.messages.ClientHeartbeat;

public class ClientTimeoutHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object evt) throws Exception {
        if (IdleStateEvent.class.isInstance(evt)) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
                context.writeAndFlush(new ClientHeartbeat());
            } else if (e.state() == IdleState.READER_IDLE) {
                context.close();
            }
        }
    }
}
