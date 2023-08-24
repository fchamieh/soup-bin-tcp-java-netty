package soupbinnettytcp.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import soupbinnettytcp.IServerListener;
import soupbinnettytcp.LoginDetails;
import soupbinnettytcp.messages.LoginAccepted;
import soupbinnettytcp.messages.LoginRejected;
import soupbinnettytcp.messages.LoginRequest;

public class ServerHandshakeHandler extends SimpleChannelInboundHandler<LoginRequest> {

    private final IServerListener _listener;

    public ServerHandshakeHandler(IServerListener listener) {
        _listener = listener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequest msg) throws Exception {
        LoginDetails login = msg.loginDetails; 
        var result = _listener.OnLoginRequest(
            login.username, 
            login.password, 
            login.requestedSession, 
            login.requestedSequenceNumber, 
            ctx.channel().id().asLongText());

            if (result.success)
            {
                ChannelPipeline pipeline = ctx.channel().pipeline();
                pipeline.remove(this);
                pipeline.remove("LoginRequestFilter");
                pipeline.addLast(new ServerHandler(_listener));
                ctx.write(new LoginAccepted(login.requestedSession, login.requestedSequenceNumber));
            }
            else
            {
                ctx.write(new LoginRejected(result.rejectReasonCode));
                ctx.close();
            }
    }

}
