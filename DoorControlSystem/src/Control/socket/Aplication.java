package control.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.SeqTypeConstants;
import control.serverReaction.aplication.ServerContextAP;
import model.DataAccessObject;

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
                if (object != null)
                    outMsg.println(object.toString()); //원래코드

                msg = inMsg.readLine();
                if (msg == null) break;
                System.out.println(msg);
            } while (androidLogoutFlag);
        } catch (Exception e) {
            e.printStackTrace();
            if (serverContextAP.getInfo() != null) System.out.println(serverContextAP.getInfo().getStudentID());
        } finally {
            if (serverContextAP.getInfo() != null)
                dao.setLoginFlag(serverContextAP.getInfo().getStudentID(), false);
            SystemServerSocket.getInstance().removeClient(this);
        }
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
    public void sendNewQrString(){
        JsonObject send = new JsonObject();
        send.addProperty("seqType", SeqTypeConstants.STATE_REQ);
        send = serverContextAP.response(send);
        outMsg.println(send.toString()); //원래코드
    }
}
