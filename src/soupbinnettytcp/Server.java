package soupbinnettytcp;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import soupbinnettytcp.common.SoupBinTcpMessageDecoder;
import soupbinnettytcp.common.SoupBinTcpMessageEncoder;
import soupbinnettytcp.handlers.LoginRequestFilterHandler;
import soupbinnettytcp.handlers.ServerHandshakeHandler;
import soupbinnettytcp.handlers.ServerTimeoutHandler;
import soupbinnettytcp.messages.LogoutRequest;

public class Server implements Runnable {
    public IServerListener _listener;
    private Channel _serverChannel;
    public final int port;

    public Server(int port, IServerListener listener) {
        this._listener = listener;
        this.port = port;
    }

    public void start() {
        new Thread(this).start();
    }

     AtomicBoolean _isActive = new AtomicBoolean(false);
    
    public boolean isActive() {
        return _isActive.get();
    }

    public void run() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        HashMap<String, Channel> channels = new HashMap<>();

        try {
            var bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            var pipeline = channel.pipeline();
                            channels.put(channel.id().asLongText(), channel);
                            pipeline.addLast(
                                    new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, Short.MAX_VALUE, 0, 2, 0,
                                            2, true));
                            pipeline.addLast(new LengthFieldPrepender(ByteOrder.BIG_ENDIAN, 2, 0, false));
                            pipeline.addLast(new SoupBinTcpMessageDecoder());
                            pipeline.addLast(new SoupBinTcpMessageEncoder());
                            pipeline.addLast("LoginRequestFilter", new LoginRequestFilterHandler());
                            pipeline.addLast(new IdleStateHandler(5, 1, 0));
                            pipeline.addLast(new ServerTimeoutHandler(_listener));
                            pipeline.addLast("ServerHandshake", new ServerHandshakeHandler(_listener));
                        }
                    });

            _serverChannel = bootstrap.bind(port).sync().channel();
            _listener.onServerListening();

            _isActive.set(true);

            while (!_stopRequested.get()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException iex) {
                }
            }

            _isActive.set(false);

            if (_serverChannel.isActive()) {
                for (Channel channel : channels.values()) {
                    channel.writeAndFlush(new LogoutRequest());
                }
                _serverChannel.close();
            }

            _listener.onServerDisconnect();

        } catch (Exception ex) {
            System.err.println(ex);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    private final AtomicBoolean _stopRequested = new AtomicBoolean(false);

    public void shutdown() {
        _stopRequested.set(true);
    }
}
