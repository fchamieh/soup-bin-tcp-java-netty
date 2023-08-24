package soupbinnettytcp.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import soupbinnettytcp.IServerListener;
import soupbinnettytcp.messages.ClientHeartbeat;
import soupbinnettytcp.messages.Debug;
import soupbinnettytcp.messages.LogoutRequest;
import soupbinnettytcp.messages.UnsequencedData;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    
    private final IServerListener _listener;

    public ServerHandler(IServerListener listener) {
        _listener = listener;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) throws Exception {
        String clientId = context.channel().id().asLongText();
        if (Debug.class.isInstance(message)) {
            _listener.onDebug(((Debug) message).text, clientId);
        }
        else if (LogoutRequest.class.isInstance(message)) {
            _listener.onLogout(clientId);
            context.close();
        }
        else if (UnsequencedData.class.isInstance(message)) {
            _listener.onMessage(((UnsequencedData) message).data, clientId, context);
        }
        else if(ClientHeartbeat.class.isInstance(message)) {
           // System.out.println("Received Heartbeat from client " + clientId);
        }
    }    
}
