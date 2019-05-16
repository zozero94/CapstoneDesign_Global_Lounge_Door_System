package control.serverReaction.aplication;

import com.google.gson.JsonObject;
import control.SeqTypeConstants;
import control.socket.SystemServerSocket;
import model.DataAccessObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Qr implements StateAP {

    private JsonObject objectReturn;
    private ServerContextAP serverContext;
    private SimpleDateFormat dayTime;

    private DataAccessObject dao;
    public Qr(ServerContextAP serverContext) {
        this.serverContext = serverContext;
        this.dao = new DataAccessObject();
        dayTime = new SimpleDateFormat("yyyyMMddHHmmss");
    }

    @Override
    public JsonObject reaction(JsonObject object){
        this.objectReturn = new JsonObject();
        if(object.get("seqType").getAsString().equals(SeqTypeConstants.STATE_REQ) ){   // QR 갱신
            serverContext.setQrString(serverContext.getInfo().getStudentID() + dayTime.format(new Date(System.currentTimeMillis()) ));
            objectReturn.addProperty("seqType", SeqTypeConstants.STATE_CREATE);
            objectReturn.addProperty("qr", serverContext.getQrString());
            serverContext.setQrFlag(true);
        }else if(object.get("seqType").getAsString().equals(SeqTypeConstants.STATE_DEL)){// QR 코드를 만료
            this.objectReturn = null;
            serverContext.setQrFlag(false);
        }else if(object.get("seqType").getAsString().equals(SeqTypeConstants.STATE_IMG)){
            objectReturn.addProperty("seqType", 204);
            objectReturn.addProperty("img", "https://udream.sejong.ac.kr/upload/per/" +serverContext.getInfo().getStudentID()+".jpg?ver=20190515205000");//udram image
            //objectReturn.addProperty("img", DataAccessObject.getInstance().getStudentImageUrl(serverContext.getInfo().getStudentID())); //git에 저장된 이미지
        } else{
            this.objectReturn = null;
            SystemServerSocket.getInstance().removeClient(serverContext.getInfo().getStudentID());
            dao.setLoginFlag(serverContext.getInfo().getStudentID(), false);
            serverContext.getSocketThread().setAndroidLogoutFlag(false);
        }
        return objectReturn;
    }
}
