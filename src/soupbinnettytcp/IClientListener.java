package soupbinnettytcp;

public interface IClientListener {
    void onConnect();
    void onMessage(byte[] message, long sequence);
    void onDebug(String debugText);
    void onLoginAccepted(String session, long sequence);    
    void onLoginRejected(char rejectReasonCode);
    void onDisconnect();    
}
