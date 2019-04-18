package control.ServerReaction;

import com.google.gson.JsonObject;
import control.SocketThread;
import model.ServerStudent;

public class ServerContext {
    private boolean QrFlag;
    private State state;
    private ServerStudent info;
    private String qrString;
    private SocketThread socketThread;


    public ServerContext(SocketThread socketThread){
        this.QrFlag = false;
        this.state = new Login(this);
        this.socketThread = socketThread;
    }

    public void setState(State state){
        this.state = state;
    }
    public JsonObject response(JsonObject object){
        return state.reaction(object);
    }

    public ServerStudent getInfo() {
        return info;
    }
    public void setInfo(ServerStudent info) {
        this.info = info;
    }

    public void setQrFlag(boolean qrFlag) {
        QrFlagTurn(qrFlag);
    }
    private synchronized void QrFlagTurn(boolean qrFlag){
        this.QrFlag = qrFlag;
    }
    public boolean isQrFlag() {
        return QrFlag;
    }

    public String getQrString() {
        return qrString;
    }
    public void setQrString(String qrString) {
        this.qrString = qrString;
    }

    public SocketThread getSocketThread() {
        return socketThread;
    }
    public void setSocketThread(SocketThread socketThread) {
        this.socketThread = socketThread;
    }
}
