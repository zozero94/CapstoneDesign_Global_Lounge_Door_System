package control.serverReaction.aplication;

import com.google.gson.JsonObject;
import control.socket.Aplication;
import model.dto.Data;

public class ServerContextAP {
    private boolean QrFlag;
    private String qrString;

    private StateAP state;
    private Data info;
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

    public Data getInfo() {
        return info;
    }
    public void setInfo(Data info) {
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

    public Aplication getSocketThread() {
        return socketThread;
    }

}
