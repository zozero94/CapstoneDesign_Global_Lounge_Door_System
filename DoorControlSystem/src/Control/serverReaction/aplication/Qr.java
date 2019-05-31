package control.serverReaction.aplication;

import com.google.gson.JsonObject;
import control.SeqTypeConstants;
import control.socket.SystemServerSocket;
import model.DataAccessObject;
import view.DateCalculator;

import javax.swing.*;

public class Qr implements StateAP {

    private JsonObject objectReturn;
    private ServerContextAP serverContext;

    private DataAccessObject dao;
    public Qr(ServerContextAP serverContext) {
        this.serverContext = serverContext;
        this.dao = new DataAccessObject();

    }

    @Override
    public JsonObject reaction(JsonObject object){
        this.objectReturn = new JsonObject();
        if(object.get("seqType").getAsString().equals(SeqTypeConstants.STATE_REQ) ){   // QR 갱신
            serverContext.setQrString(DateCalculator.currentTime(serverContext.getInfo().getStudentID()));
            objectReturn.addProperty("seqType", SeqTypeConstants.STATE_CREATE);
            objectReturn.addProperty("qr", serverContext.getQrString());
            serverContext.setQrFlag(true);
        }else if(object.get("seqType").getAsString().equals(SeqTypeConstants.STATE_DEL)){// QR 코드를 만료
            this.objectReturn = null;
            serverContext.setQrFlag(false);
        }else if(object.get("seqType").getAsString().equals(SeqTypeConstants.STATE_IMG)){
            objectReturn.addProperty("seqType", SeqTypeConstants.STATE_URL);
            if(serverContext.getInfo().getName().equals("관리자")) objectReturn.addProperty("img", "https://user-images.githubusercontent.com/34762799/57904190-bb160000-78ac-11e9-9d52-120703c3f028.png");//udram image
            else objectReturn.addProperty("img", "https://udream.sejong.ac.kr/upload/per/" +serverContext.getInfo().getStudentID()+".jpg?ver=20190515205000");//udram image
            //objectReturn.addProperty("img", DataAccessObject.getInstance().getStudentImageUrl(serverContext.getInfo().getStudentID())); //git에 저장된 이미지
        }else if(object.get("seqType").getAsString().equals(SeqTypeConstants.STATE_ADMIN)){
            if (SystemServerSocket.getInstance().getRaspberrypi() != null) {
                SystemServerSocket.getInstance().getRaspberrypi().openDoor();
                objectReturn.addProperty("seqType", SeqTypeConstants.STATE_OPEN);
            }
            else
                objectReturn.addProperty("seqType", SeqTypeConstants.STATE_NO);
        }else{
            this.objectReturn = null;
            serverContext.setQrFlag(false);
            SystemServerSocket.getInstance().removeClient(serverContext.getInfo().getStudentID());
            dao.setLoginFlag(serverContext.getInfo().getStudentID(), false);
            serverContext.getSocketThread().setAndroidLogoutFlag(false);
        }
        return objectReturn;
    }
}
