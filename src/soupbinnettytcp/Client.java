package soupbinnettytcp;

import java.net.InetAddress;
import java.nio.ByteOrder;
import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicBoolean;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import soupbinnettytcp.common.SoupBinTcpMessageDecoder;
import soupbinnettytcp.common.SoupBinTcpMessageEncoder;
import soupbinnettytcp.handlers.ClientHandler;
import soupbinnettytcp.handlers.ClientTimeoutHandler;
import soupbinnettytcp.messages.Debug;
import soupbinnettytcp.messages.LogoutRequest;
import soupbinnettytcp.messages.UnsequencedData;

/**
 * Soup Bin Tcp Client
 */
public class Client implements Runnable {

    private final InetAddress _inetAddress;
    private final int _port;
    private final AtomicBoolean _stopRequested = new AtomicBoolean(false);
    private Channel _clientChannel;
    private final IClientListener _listener;
    private final LoginDetails _loginDetails;

    public Client(InetAddress inetAddress, int port, LoginDetails loginDetails, IClientListener listener) {
        if (port < 1)
            throw new InvalidParameterException("Invalid port number " + port);
        _inetAddress = inetAddress;
        _port = port;
        _listener = listener;
        _loginDetails = loginDetails;
    }

    public void start() {
        new Thread(this).start();
    }

    AtomicBoolean _isActive = new AtomicBoolean(false);
    
    public boolean isActive() {
        return _isActive.get();
    }

    public void run() {
        try {
            var bootstrap = new Bootstrap();
            var group = new NioEventLoopGroup();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(
                                    new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, Short.MAX_VALUE, 0, 2,
                                            0, 2, true));
                            pipeline.addLast(new LengthFieldPrepender(ByteOrder.BIG_ENDIAN, 2, 0, false));
                            pipeline.addLast(new SoupBinTcpMessageDecoder());
                            pipeline.addLast(new SoupBinTcpMessageEncoder());
                            pipeline.addLast(new IdleStateHandler(15, 1, 0));
                            pipeline.addLast(new ClientTimeoutHandler());
                            pipeline.addLast(new ClientHandler(_loginDetails, _listener));
                        }
                    });

            _clientChannel = bootstrap.connect(_inetAddress, _port).channel();

            _isActive.set(true);

            try {
                _clientChannel.closeFuture().sync();
            } catch (InterruptedException iex) {
            }

            _isActive.set(false);

            if (_clientChannel.isActive()) {
                _clientChannel.writeAndFlush(new LogoutRequest());
                _clientChannel.close();
                _listener.onDisconnect();
            }
        } finally {

        }
    }   

    public void send(byte[] message) {
        if (message.length > Short.MAX_VALUE - 1) {
            throw new InvalidParameterException(
                    "SoupBinTCP message payload exceeds maximum size (" + Short.MAX_VALUE + " bytes)");
        }
        if (_clientChannel.isActive()) {
            _clientChannel.writeAndFlush(new UnsequencedData(message, true));
        }
    }

    public void debug(String message) {
        if (_clientChannel.isActive() && message.length() < Short.MAX_VALUE) {
            _clientChannel.writeAndFlush(new Debug(message));
        }
    }

    public void shutdown() {
        _stopRequested.set(true);
    }

}
