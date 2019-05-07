package control.serverReaction.aplication;

import com.google.gson.JsonObject;
import control.SystemServerSocket;
import model.DataAccessObject;
import org.apache.commons.codec.binary.Base64;

import java.text.SimpleDateFormat;

import java.util.Date;

public class Qr implements StateAP {

    private JsonObject objectReturn;
    private ServerContextAP serverContext;
    private SimpleDateFormat dayTime;



    public Qr(ServerContextAP serverContext) {
        this.serverContext = serverContext;
        dayTime = new SimpleDateFormat("yyyyMMddHHmmss");
    }

    @Override
    public JsonObject reaction(JsonObject object){
        this.objectReturn = new JsonObject();
        if(object.get("seqType").getAsInt() == SeqTypeConstants.STATE_REQ ){
            serverContext.setQrString(serverContext.getInfo().getStudentID() + dayTime.format(new Date(System.currentTimeMillis()) ));
            objectReturn.addProperty("seqType", SeqTypeConstants.STATE_CREATE);
            objectReturn.addProperty("qr", serverContext.getQrString());
            // QR 갱신
            serverContext.setQrFlag(true);
        }
        else if(object.get("seqType").getAsInt() == SeqTypeConstants.STATE_DEL){
            this.objectReturn = null;
            // QR 코드를 만료
            System.out.println("qr 코드 만료");
            serverContext.setQrFlag(false);
        }else if(object.get("seqType").getAsInt() == SeqTypeConstants.STATE_IMG){
            objectReturn.addProperty("seqType", 204);
            objectReturn.addProperty("img", Base64.encodeBase64String(DataAccessObject.getInstance().getStudentImageByte(serverContext.getInfo().getStudentID())));
        } else{
            this.objectReturn = null;
            SystemServerSocket.getInstance().removeClient(serverContext.getInfo().getStudentID());
            DataAccessObject.getInstance().setLoginFlag(serverContext.getInfo().getStudentID(), false);
            serverContext.getSocketThread().setAndroidLogoutFlag(false);
            // 로그아웃 flag 수정하고
            // 이런식의 로그아웃이 아닌 경우 소켓이 끊어 질때마다 로그아웃 플래그 수정해야함
        }
        return objectReturn;
    }
}
