package soupbinnettytcp.handlers;

import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import soupbinnettytcp.IClientListener;
import soupbinnettytcp.LoginDetails;
import soupbinnettytcp.messages.Debug;
import soupbinnettytcp.messages.LoginAccepted;
import soupbinnettytcp.messages.LoginRejected;
import soupbinnettytcp.messages.LoginRequest;
import soupbinnettytcp.messages.SequencedData;
import soupbinnettytcp.messages.ServerHeartbeat;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final IClientListener _listener;
    private final LoginDetails _loginDetails;
    private AtomicLong sequence = new AtomicLong(0);

    public ClientHandler(LoginDetails _loginDetails, IClientListener _listener) {
        this._listener = _listener;
        this._loginDetails = _loginDetails;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        _listener.onConnect();
        ctx.writeAndFlush(new LoginRequest(_loginDetails));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        _listener.onDisconnect();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Debug Message
        if (Debug.class.isInstance(msg)) {
            _listener.onDebug(((Debug) msg).text);
        }
        // Login Accepted
        else if (LoginAccepted.class.isInstance(msg)) {
            LoginAccepted loginAccepted = (LoginAccepted) msg;
            _listener.onLoginAccepted(loginAccepted.requestedSession, loginAccepted.requestedSequenceNumber);
        }
        // Login Rejected
        else if (LoginRejected.class.isInstance(msg)) {
            LoginRejected loginRejected = (LoginRejected) msg;
            _listener.onLoginRejected(loginRejected.rejectReasonCode);
        } 
        // Sequenced Data
        else if (SequencedData.class.isInstance(msg)) {
            SequencedData sequencedData = (SequencedData) msg;
            _listener.onMessage(sequencedData.data, sequence.addAndGet(1));
        }
        else if(ServerHeartbeat.class.isInstance(msg)) {
            // System.out.println("Received Heartbeat from server");            
        }
    }

}
