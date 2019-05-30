package control.serverReaction.raspberrypi;

import com.google.gson.JsonObject;
import control.SeqTypeConstants;
import control.socket.SystemServerSocket;
import model.DataAccessObject;
import model.dto.Data;
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
            SystemServerSocket.getInstance().setQrFlagFalse(serverContextRA.getStudentId());
            if(!serverContextRA.checkAdmin()){
                studentInfo = dao.getStudent(serverContextRA.getStudentId());
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
