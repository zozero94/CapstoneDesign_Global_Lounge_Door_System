package control.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.SystemServerSocket;
import control.serverReaction.aplication.SeqTypeConstants;
import control.serverReaction.aplication.ServerContextAP;
import model.DataAccessObject;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Aplication extends SocketThread {
    private JsonParser parser;
    private JsonObject object;

    private BufferedReader inMsg;
    private PrintWriter outMsg;

    private String msg;
    private ServerContextAP serverContext;
    private boolean androidLogoutFlag;

    private int check;

    public Aplication(Socket socket, String msg, BufferedReader reader) throws Exception {
        this.inMsg = reader;
        this.outMsg = new PrintWriter(socket.getOutputStream(), true);
        this.parser = new JsonParser();
        this.serverContext = new ServerContextAP(this);
        this.androidLogoutFlag = true;
        this.check = 0;
        this.msg = msg;
        SystemServerSocket.getInstance().addClient(this);
        object = (JsonObject) parser.parse(msg);
        object = serverContext.response(object);
        if (object != null) {
            outMsg.println(object.toString()); //원래코드
        }
    }

    @Override
    public void run() {
        try {
            do {
                msg = inMsg.readLine();
                //System.out.println(msg);
                if (msg == null) break;
                object = (JsonObject) parser.parse(msg);
                object = serverContext.response(object);
                if (object != null) {
                    if (object.get("seqType").getAsInt() == 204){
                        outMsg.println(object.get("img"));
                    }
                    else outMsg.println(object.toString()); //원래코드

                }
            } while (androidLogoutFlag);
        } catch (Exception e) {
            e.printStackTrace();
            if (serverContext.getInfo() != null) System.out.println(serverContext.getInfo().getStudentID());
        } finally {
            if (serverContext.getInfo() != null)
                DataAccessObject.getInstance().setLoginFlag(serverContext.getInfo().getStudentID(), false);
            SystemServerSocket.getInstance().removeClient(this);
        }
    }

    public boolean compareQrString(String qrString) {
        return serverContext.isQrFlag() == true ? serverContext.getQrString().equals(qrString) : false;// true 같음 false 틀림
    }

    public boolean compareStudentId(String studentId) {
        return serverContext.getInfo().getStudentID().equals(studentId);// true 같음 false 틀림
    }

    public void setAndroidLogoutFlag(boolean androidLogoutFlag) {
        this.androidLogoutFlag = androidLogoutFlag;
    }
    

}
