package soupbinnettytcp.common;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class Common {

    public static Charset ASCII = Charset.forName("us-ascii");

    public enum PaddingDirection {
        Right,
        Left
    }

    public final class SoupMessageTypes {
        public static final byte Debug = 68;
        public static final byte LoginAccepted = 65;
        public static final byte LoginRejected = 74;
        public static final byte SequencedData = 83;
        public static final byte ServerHeartbeat = 72;
        public static final byte EndOfSession = 90;
        public static final byte LoginRequest = 76;
        public static final byte UnsequencedData = 85;        
        public static final byte ClientHeartbeat = 82;
        public static final byte LogoutRequest = 79;
    }

    public static String padLeft(Object a, int length) {
        StringBuffer sb = new StringBuffer(length);
        String aString = a == null ? "" : a.toString();
        sb.append(aString);
        sb.insert(0, " ".repeat(length - aString.length()));
        return sb.toString();
    }

    public static String padRight(Object a, int length) {
        StringBuffer sb = new StringBuffer(length);
        String aString = a == null ? "" : a.toString();
        sb.append(aString);
        sb.append(" ".repeat(length - aString.length()));
        return sb.toString();
    }

    public static byte[] padAll(Object... args) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i += 3) {
            Object a = args[i];
            int length = (int) args[i + 1];
            PaddingDirection direction = (PaddingDirection) args[i + 2];
            if (direction == PaddingDirection.Left)
                sb.append(padLeft(a, length));
            else
                sb.append(padRight(a, length));
        }
        return bytes(sb);
    }

    public static String toString(byte[] bytes, int startIndex, int endIndex) {
        byte[] copy = Arrays.copyOfRange(bytes, startIndex, endIndex);
        return ASCII.decode(ByteBuffer.wrap(copy)).toString();
    }

    public static String toString(byte[] bytes, int startIndex) {
        byte[] copy = Arrays.copyOfRange(bytes, startIndex, bytes.length);
        return ASCII.decode(ByteBuffer.wrap(copy)).toString();
    }

    public static String toString(byte[] bytes) {
        return ASCII.decode(ByteBuffer.wrap(bytes)).toString();
    }

    /**
     * Retuns the bytes for ASCII representation of object o as string
     * 
     * @param o
     * @return
     */
    public static byte[] bytes(Object o) {
        return ASCII.encode(o.toString()).array();
    }

}
