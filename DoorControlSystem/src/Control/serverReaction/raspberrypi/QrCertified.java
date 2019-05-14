package control.serverReaction.raspberrypi;

import com.google.gson.JsonObject;
import model.dto.StudentAccessInfo;

public class QrCertified implements StateRA{
    private StudentAccessInfo studentAccessInfo;
    private ServerContextRA serverContextRA;

    public  QrCertified(ServerContextRA serverContextRA){
        this.serverContextRA = serverContextRA;
    }
    @Override
    public JsonObject reaction(JsonObject object){
        serverContextRA.getRaspberrypi().setSendFlag(true);
        if(object.get("seqType").getAsInt() == 400){
            //TODO
            // 데이터 베이스 저장
            this.studentAccessInfo = new StudentAccessInfo(this.serverContextRA.getStudentId());


        }
        return null;
    }
}
