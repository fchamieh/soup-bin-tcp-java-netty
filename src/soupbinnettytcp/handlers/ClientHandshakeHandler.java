package soupbinnettytcp.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import soupbinnettytcp.IClientListener;
import soupbinnettytcp.LoginDetails;
import soupbinnettytcp.messages.LoginRequest;

public class ClientHandshakeHandler extends ChannelInboundHandlerAdapter {
    
    private final IClientListener _listener;
    private final LoginDetails _loginDetails;

    public ClientHandshakeHandler(LoginDetails _loginDetails, IClientListener _listener) {
        this._listener = _listener;
        this._loginDetails = _loginDetails;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        _listener.onConnect();
        ctx.writeAndFlush(new LoginRequest(_loginDetails));
    }
}
