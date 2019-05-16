package control.serverReaction.raspberrypi;

import com.google.gson.JsonObject;
import control.socket.Raspberrypi;

public class ServerContextRA {

    private StateRA state;
    private Confirm confirm;
    private QrCertified qrCertified;
    private String qrString;
    private Raspberrypi raspberrypi;

    public ServerContextRA(Raspberrypi raspberrypi){
        this.raspberrypi = raspberrypi;
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

    private void SwitchState(){
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
    public Raspberrypi getRaspberrypi() {
        return raspberrypi;
    }
    public void setRaspberrypi(Raspberrypi raspberrypi) {
        this.raspberrypi = raspberrypi;
    }
}
