package tests;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.netty.channel.ChannelHandlerContext;
import soupbinnettytcp.Client;
import soupbinnettytcp.IClientListener;
import soupbinnettytcp.IServerListener;
import soupbinnettytcp.LoginDetails;
import soupbinnettytcp.LoginStatus;
import soupbinnettytcp.Server;
import soupbinnettytcp.common.Common;
import soupbinnettytcp.messages.SequencedData;

public class TestSoup implements IServerListener, IClientListener {

    public static void main(String[] args) throws UnknownHostException {

        TestSoup testSoup = new TestSoup();
        testSoup.start();

    }

    final Server server;
    final Client client;

    public TestSoup() throws UnknownHostException {

        this.server = new Server(30000, this);

        LoginDetails loginDetails = new LoginDetails(
                "user",
                "password",
                "",
                1);

        this.client = new Client(
                InetAddress.getLocalHost(),
                30000,
                loginDetails,
                this);
    }

    void start() {
        server.start();

        while (!server.isActive()) {
            if (!server.isActive())
                System.out.println("Waiting for Server to be active");
            sleep(1000);
        }

        client.start();
        while (!client.isActive()) {
            if (!client.isActive())
                System.out.println("Waiting for Client to be active");
            sleep(1000);
        }

        final int messageCount = 100_000;
        for (int times = 0; times < 3; times++) {

            System.out.println("Sending " + messageCount + " messages  ...");

            // Send back and forth messageCount messages
            for (int i = 1; i <= messageCount; i++) {
                String message = "Sending Debug Message " + i;
                client.send(Common.bytes(message));
            }

            // sleep 5 seconds
            System.out.println("Sleeping for 5s ...");
            sleep(5000);
        }
    }

    void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
        }
    }

    @Override
    public void onServerListening() {
        System.err.println("'onServerListening'");
    }

    @Override
    public LoginStatus OnLoginRequest(String username, String password, String requestedSession,
            long requestedSequenceNumber, String clientId) {
        return LoginStatus.Successful;
    }

    @Override
    public void onLogout(String clientId) {
        System.err.println("'onLogout'");
    }

    @Override
    public void onDebug(String message, String clientId) {
        System.err.println("'onDebug': " + message + " from " + clientId);
    }

    @Override
    public void onMessage(byte[] message, String clientId, ChannelHandlerContext context) {
        String notification = "'onMessage' from " + clientId;
        System.err.println(notification);
        context.channel().writeAndFlush(new SequencedData(Common.bytes(notification), true));
    }

    @Override
    public void onSessionStart(String sessionId) {
        System.err.println("'onSessionStart' " + sessionId);
    }

    @Override
    public void onSessionEnd(String clientId) {
        System.err.println("'onSessionEnd' " + clientId);
    }

    @Override
    public void onServerDisconnect() {
        System.err.println("'onServerDisconnect'");
    }

    @Override
    public void onConnect() {
        System.err.println("'onConnect'");
    }

    @Override
    public void onMessage(byte[] message, long sequence) {
        System.err.println("'onMessage' " + message.length + " bytes, sequence: " + sequence);
    }

    @Override
    public void onDebug(String debugText) {
        System.err.println("'onDebug' " + debugText);
    }

    @Override
    public void onLoginAccepted(String session, long sequence) {
        System.err.println("'onLoginAccepted' " + sequence + " / " + session);
    }

    @Override
    public void onLoginRejected(char rejectReasonCode) {
        System.err.println("'onLoginRejected': " + rejectReasonCode);
    }

    @Override
    public void onDisconnect() {
        System.err.println("'onDisconnect'");
    }
}
