package control.serverReaction.raspberrypi;

import com.google.gson.JsonObject;
import control.SeqTypeConstants;
import control.socket.SystemServerSocket;
import model.DataAccessObject;
import model.dto.ExcelOutInfo;
import model.dto.StudentAccessInfo;
import view.LogPanel;

public class QrCertified implements StateRA{
    private StudentAccessInfo studentAccessInfo;
    private ServerContextRA serverContextRA;
    private ExcelOutInfo log;
    private DataAccessObject dao;
    public  QrCertified(ServerContextRA serverContextRA){
        this.serverContextRA = serverContextRA;
        this.dao = new DataAccessObject();
    }
    @Override
    public JsonObject reaction(JsonObject object){
        if(object.get("seqType").getAsString().equals(SeqTypeConstants.ACCESS_OK)){
            SystemServerSocket.getInstance().sendMsg(serverContextRA.getStudentId());
            this.studentAccessInfo = new StudentAccessInfo(this.serverContextRA.getStudentId());
            dao.insertStudentLog(studentAccessInfo);
            log = new ExcelOutInfo(dao.getStudentInfo(studentAccessInfo.getStudentID()),studentAccessInfo.getTime());
            LogPanel.getInstance().insertTableData(log);


        }else if(object.get("seqType").getAsString().equals(SeqTypeConstants.ACCESS_NO))
            this.serverContextRA.setQrString(null);
        serverContextRA.getRaspberrypi().setSendFlag(true);
        return null;
    }
}
