package control.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.SeqTypeConstants;
import control.db.DataAccessObject;
import control.serverReaction.aplication.ServerContextAP;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Aplication extends SocketThread {
    private JsonParser parser;
    private JsonObject object;

    private BufferedReader inMsg;
    private PrintWriter outMsg;

    private String msg;
    private ServerContextAP serverContextAP;
    private boolean androidLogoutFlag;

    private DataAccessObject dao;

    public Aplication(Socket socket, String msg, BufferedReader reader) throws Exception {
        this.inMsg = reader;
        this.outMsg = new PrintWriter(socket.getOutputStream(), true);
        this.parser = new JsonParser();
        this.serverContextAP = new ServerContextAP(this);
        this.androidLogoutFlag = true;
        this.msg = msg;
        this.dao = new DataAccessObject();
        SystemServerSocket.getInstance().addClient(this);
    }

    @Override
    public void run() {
        try {
            do {
                object = (JsonObject) parser.parse(msg);
                object = serverContextAP.response(object);
                if (object != null) {
                    outMsg.println(object.toString());
                    System.out.println(object.toString());
                }
                if(!androidLogoutFlag) break;
                msg = inMsg.readLine();
                if (msg == null) break;
                System.out.println(msg);
            } while (androidLogoutFlag);
        } catch (Exception e) {
            e.printStackTrace();
            if (serverContextAP.getInfo() != null) System.out.println(serverContextAP.getInfo().getStudentID());
        } finally {
//            if(serverContextAP.getInfo().adminCheck()) dao.admin_count--;
//            // TODO admin count를 단순히 줄이면 admin 아이디가 겹치는 경우가 있음 한명이 느리게 로그아웃을 수행 하면 (1 현재) 그사이에 로그인을 하면 (1을 할당 받고 하나 증가 2
//                //  첫번째 사용자가 로그아웃을 수행하면 (1)이되어 다른사람이 admin으로 로그인 했을 아이디가 겹치는 경우가 발생
            // QR 상태에서 로그아웃을 수행은 잘되지만 만약 NULL이 와서 종료되는 경우에는 로그아웃 처리가 제대로 수행되지않음 코드 수정이 필요함

//            else if (serverContextAP.getInfo() != null)
//                dao.setLoginFlag(serverContextAP.getInfo().getStudentID(), false);

            if(!serverContextAP.getInfo().adminCheck())
                dao.setLoginFlag(serverContextAP.getInfo().getStudentID(), false);
            SystemServerSocket.getInstance().removeClient(this);
        }
        System.out.println("Logout");
    }

    public boolean compareQrString(String qrString) {
        return serverContextAP.isQrFlag() ? serverContextAP.getQrString().equals(qrString) : false;// true 같음 false 틀림
    }
    public boolean compareStudentId(String studentId) {
        return serverContextAP.getInfo().getStudentID().equals(studentId);// true 같음 false 틀림
    }
    public void setAndroidLogoutFlag(boolean androidLogoutFlag) {
        this.androidLogoutFlag = androidLogoutFlag;
    }
    public void setQrFlagFalse() {
        this.serverContextAP.setQrFlag(false);
    }
    public void sendNewQrString(){
         JsonObject send = new JsonObject();
        send.addProperty("seqType", SeqTypeConstants.STATE_REQ);
        send = serverContextAP.response(send);
        outMsg.println(send.toString()); //원래코드
    }

    public boolean isAndroidLogoutFlag() {
        return androidLogoutFlag;
    }
}
