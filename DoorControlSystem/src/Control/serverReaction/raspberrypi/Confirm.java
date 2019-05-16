package control.serverReaction.raspberrypi;

import com.google.gson.JsonObject;
import control.SeqTypeConstants;
import control.socket.SystemServerSocket;

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
            objectReturn.addProperty("seqType", SeqTypeConstants.QR_OK);
        }
        else if(object.get("seqType").getAsInt() == 402) {
            objectReturn.addProperty("seqType", SeqTypeConstants.QR_DIFF);
            serverContextRA.getRaspberrypi().setSendFlag(true);
        }
        else objectReturn.addProperty("seqType", SeqTypeConstants.QR_DIFF);
        return objectReturn;
    }
}
