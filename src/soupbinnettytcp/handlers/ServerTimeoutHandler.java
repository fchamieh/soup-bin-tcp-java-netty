package soupbinnettytcp.handlers;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import soupbinnettytcp.IServerListener;
import soupbinnettytcp.messages.ServerHeartbeat;

public class ServerTimeoutHandler extends ChannelDuplexHandler {
    
    private final IServerListener _listener;

    public ServerTimeoutHandler(IServerListener listener) {
        _listener = listener;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object evt) throws Exception {
        // If this is an idle state event
        if(IdleStateEvent.class.isInstance(evt)) {
            IdleStateEvent event = (IdleStateEvent)evt;
            if(event.state() == IdleState.WRITER_IDLE) {
                context.writeAndFlush(new ServerHeartbeat());
            } else if(event.state() == IdleState.READER_IDLE) {
                context.close();
                _listener.onSessionEnd(context.channel().id().asLongText());
            }
        };
    }
}
