package control.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.serverReaction.aplication.SeqTypeConstants;
import control.serverReaction.aplication.ServerContextAP;
import model.DataAccessObject;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.Socket;

public class SocketThreadAP extends Thread {

    private JsonParser parser;
    private JsonObject object;

    private BufferedReader inMsg;
    private PrintWriter outMsg;

    private String msg;
    private ServerContextAP serverContext;
    private boolean androidLogoutFlag;


    private int check;

    public SocketThreadAP(Socket socket) throws Exception{
        this.inMsg = new BufferedReader(new InputStreamReader((socket.getInputStream())));
        this.outMsg = new PrintWriter(socket.getOutputStream(), true);
        check =0;
        parser = new JsonParser();
       // serverContext = new ServerContextAP(this);
        this.androidLogoutFlag = true;
    }

    public void run(){
        try{
            while(androidLogoutFlag) {
                msg = inMsg.readLine();

                System.out.println(msg);
                if(msg == null) break;
                object = (JsonObject) parser.parse(msg);
                object = serverContext.response(object);
                if(object != null) {
                    outMsg.println(object.toString()); //원래코드
                    if(object.get("seqType").getAsInt() == SeqTypeConstants.STATE_CREATE && check == 0)
                    {
                        System.out.println("이미지 전송");
                        check++;
                        byte[] b = DataAccessObject.getInstance().getStudentImageByte(serverContext.getInfo().getStudentID());
                        outMsg.println(Base64.encodeBase64String(b));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(serverContext.getInfo().getStudentID());
        }finally {
            if(serverContext.getInfo() != null)
                DataAccessObject.getInstance().setLoginFlag(serverContext.getInfo().getStudentID(), false);
           // SystemServerSocket.getInstance().removeClient(this);
        }

    }

    public boolean compareQrString(String qrString){
        return serverContext.isQrFlag()==true ? serverContext.getQrString().equals(qrString) : false;// true 같음 false 틀림
    }
    public boolean compareStudentId(String studentId){
        return serverContext.getInfo().getStudentID().equals(studentId);// true 같음 false 틀림
    }
    public void setAndroidLogoutFlag(boolean androidLogoutFlag) {
        this.androidLogoutFlag = androidLogoutFlag;
    }
}
