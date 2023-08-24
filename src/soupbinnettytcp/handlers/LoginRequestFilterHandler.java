package soupbinnettytcp.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import soupbinnettytcp.messages.LoginRequest;

public class LoginRequestFilterHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(LoginRequest.class.isInstance(msg)) {
            ctx.fireChannelRead(msg);
        } else {
            ReferenceCountUtil.release(msg);
        }
    }
}
