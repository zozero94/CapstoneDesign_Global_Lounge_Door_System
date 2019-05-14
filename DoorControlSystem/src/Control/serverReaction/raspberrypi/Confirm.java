package control.serverReaction.raspberrypi;

import com.google.gson.JsonObject;
import control.serverReaction.SystemServerSocket;

public class Confirm implements StateRA {

    private ServerContextRA serverContextRA;

    public Confirm(ServerContextRA serverContextRA){
        this.serverContextRA = serverContextRA;
    }
    @Override
    public JsonObject reaction(JsonObject object){
        JsonObject objectReturn = new JsonObject();
        if(serverContextRA.getRaspberrypi().isSendFlag() && SystemServerSocket.getInstance().compareQrString(object.get("data").getAsString()))
        {
            serverContextRA.getRaspberrypi().setSendFlag(false);
            this.serverContextRA.setQrString(object.get("data").getAsString());
            objectReturn.addProperty("seqType", 301);
        }
        else if(object.get("seqType").getAsInt() == 402) {
            objectReturn.addProperty("seqType", 302);
            serverContextRA.getRaspberrypi().setSendFlag(true);
            System.out.println("set flag");
        }
        else objectReturn.addProperty("seqType", 302);
        return objectReturn;
    }
}
