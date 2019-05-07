package control.serverReaction.raspberrypi;

import com.google.gson.JsonObject;
import control.SystemServerSocket;

public class Confirm implements StateRA {

    private ServerContextRA serverContextRA;

    public Confirm(ServerContextRA serverContextRA){
        this.serverContextRA = serverContextRA;
    }
    @Override
    public JsonObject reaction(JsonObject object){
        JsonObject objectReturn = new JsonObject();
        if(SystemServerSocket.getInstance().compareQrString(object.get("data").getAsString()))
        {
            this.serverContextRA.setQrString(object.get("data").getAsString());
            objectReturn.addProperty("seqType", 301);
        }
        else objectReturn.addProperty("seqType", 302);
        return objectReturn;
    }
}
