package control.serverReaction.raspberrypi;

import com.google.gson.JsonObject;

public class ServerContextRA {

    private StateRA state;
    private Confirm confirm;
    private QrCertified qrCertified;
    private String qrString;

    public ServerContextRA(){
        this.confirm = new Confirm(this);
        this.qrCertified = new QrCertified(this);
        this.state = confirm;
        this.qrString = null;
    }
    public JsonObject response(JsonObject object){
        JsonObject objectReturn = state.reaction(object);
        if(objectReturn == null || objectReturn.get("seqType").getAsInt() != 302 ) SwitchState();
        return objectReturn;
    }

    public String check(){
        return this.state==qrCertified ? "QR 인증후":"QR 인증전";
    }
    public void SwitchState(){
        this.state = state == confirm ? qrCertified : confirm;
    }
    public String getQrString() {
        return qrString;
    }
    public void setQrString(String qrString) {
        this.qrString = qrString;
    }
    public String getStudentId(){
        return this.qrString.substring(0,8);
    }
}
