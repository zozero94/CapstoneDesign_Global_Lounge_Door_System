package control.serverReaction.raspberrypi;

import com.google.gson.JsonObject;
import control.SeqTypeConstants;
import control.db.Data;
import control.db.DataAccessObject;
import control.socket.SystemServerSocket;
import view.DateCalculator;
import view.GuiConstant;
import view.right.LogPanel;

public class QrCertified implements StateRA{
    private ServerContextRA serverContextRA;
    private Data studentInfo;
    private DataAccessObject dao;
    private LogPanel logPanel;

    public  QrCertified(ServerContextRA serverContextRA){
        this.serverContextRA = serverContextRA;
        this.dao = new DataAccessObject();
        this.logPanel = LogPanel.getInstance();
        this.studentInfo = null;
    }
    @Override
    public JsonObject reaction(JsonObject object){

        if(object.get("seqType").getAsString().equals(SeqTypeConstants.ACCESS_OK)){
            SystemServerSocket.getInstance().sendMsg(serverContextRA.getStudentId());
            if(!serverContextRA.checkAdmin()){
                studentInfo = dao.getStudent(serverContextRA.getStudentId());
                studentInfo.setTime(DateCalculator.currentTime());
                dao.insertStudentLog(studentInfo);
                logPanel.insertTableData(studentInfo, true);
                logPanel.imageChange(GuiConstant.getImageUrl(serverContextRA.getStudentId()), studentInfo.getStudentID());
            }
        }else if(object.get("seqType").getAsString().equals(SeqTypeConstants.ACCESS_NO))
            this.serverContextRA.setQrString(null);
        serverContextRA.getRaspberrypi().setSendFlag(true);
        return null;
    }
}
