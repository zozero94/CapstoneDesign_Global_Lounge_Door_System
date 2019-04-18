package control.ServerReaction;

public class SeqTypeConstants {
    public final static int LOGIN = 100;
    public final static int LOGIN_OK = 101;
    public final static int LOGIN_ALREADY = 102;
    public final static int LOGIN_NO_DATA = 103;

    public final static int STATE_REQ = 200;
    public final static int STATE_DEL= 201;
    public final static int STATE_CREATE = 202;

    public final static int QR = 300;
    public final static int QR_OK = 301;
    public final static int QR_DIFF = 302;

    public final static int ACCESS_OK = 400;
    public final static int ACCESS_NO = 401;

    public final static int LOGOUT = 500;
}
