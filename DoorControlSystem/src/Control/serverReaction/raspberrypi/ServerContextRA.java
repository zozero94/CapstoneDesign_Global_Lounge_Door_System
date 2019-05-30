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
    public void setQrString(String qrString) {
        this.qrString = qrString;
    }
    public String getStudentId(){
        if(this.checkAdmin())
            return getAdmin();
        return this.qrString.substring(0,8);
    }
    public String getAdmin(){
        return this.qrString.substring(0,6);
    }
    public boolean checkAdmin(){
        return this.qrString.substring(0,5).equals("admin");
    }
    public Raspberrypi getRaspberrypi() {
        return raspberrypi;
    }
}
