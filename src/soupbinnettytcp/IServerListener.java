package soupbinnettytcp;

import io.netty.channel.ChannelHandlerContext;

public interface IServerListener {
    /**
     * Server is ready to accept client connections
     */
    void onServerListening();

    /**
     * Called when a client attempts to login
     * @param username
     * @param password
     * @param requestedSession
     * @param requestedSequenceNumber
     * @param clientId
     * @return
     */
    LoginStatus OnLoginRequest(String username, String password, String requestedSession, long requestedSequenceNumber, String clientId);

    /**
     * Client Requested to Logout
     * @param clientId
     */
    void onLogout(String clientId);

    /**
     * Debugging message received
     * @param message
     * @param clientId
     */
    void onDebug(String message, String clientId);


    /**
     * Message Received
     * @param message
     * @param clientId
     */
    void onMessage(byte[] message, String clientId, ChannelHandlerContext context);

    /**
     * Session Started
     * @param sessionId
     */
    void onSessionStart(String sessionId);

    /**
     * Session Ended for client
     * @param clientId
     */
    void onSessionEnd(String clientId);

    /**
     * after server shutdown
     */
    void onServerDisconnect();
}