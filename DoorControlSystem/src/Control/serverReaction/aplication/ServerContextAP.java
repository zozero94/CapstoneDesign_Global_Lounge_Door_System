package control.serverReaction.aplication;

import com.google.gson.JsonObject;
import control.socket.Aplication;
import control.socket.SocketThreadAP;
import model.ServerStudent;

public class ServerContextAP {
    private boolean QrFlag;
    private StateAP state;
    private ServerStudent info;
    private String qrString;
//    private SocketThreadAP socketThread;
    private Aplication socketThread;


    public ServerContextAP(Aplication socketThread){
        this.QrFlag = false;
        this.state = new Login(this);
        this.socketThread = socketThread;
        this.info = null;
    }

    public void setState(StateAP state){
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

//    public SocketThreadAP getSocketThread() {
//        return socketThread;
//    }
//    public void SocketThreadAP(SocketThreadAP socketThread) {
//        this.socketThread = socketThread;
//    }
    public Aplication getSocketThread() {
        return socketThread;
    }
    public void Aplication(Aplication socketThread) {
        this.socketThread = socketThread;
    }
}
