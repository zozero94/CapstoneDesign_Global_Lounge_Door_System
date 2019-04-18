package control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.ServerReaction.SeqTypeConstants;
import control.ServerReaction.ServerContext;
import model.DataAccessObject;

import java.io.*;
import java.net.Socket;

public class SocketThread extends Thread {

    private Gson gson;
    private JsonParser parser;
    private JsonObject object;

    private BufferedReader inMsg;
    private PrintWriter outMsg;

    private String msg;
    private ServerContext serverContext;
    private boolean androidLogoutFlag;

    private ObjectOutputStream outFile;
    private DataOutput out;

    public SocketThread(Socket socket) throws Exception{
        this.inMsg = new BufferedReader(new InputStreamReader((socket.getInputStream())));
        this.outMsg = new PrintWriter(socket.getOutputStream(), true);
        this.outFile = new ObjectOutputStream(socket.getOutputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        gson = new Gson();
        parser = new JsonParser();
        serverContext = new ServerContext(this);
        this.androidLogoutFlag = true;
    }

    public void run(){
        try{
            while(androidLogoutFlag) {
                msg = inMsg.readLine();
                System.out.println("소켓 thread" +msg);
                if(msg == null) break;
                object = (JsonObject) parser.parse(msg);
                object = serverContext.response(object);
                if(object != null) {
                    outMsg.println(object.toString()); //원래코드
//                    if(object.get("seqType").getAsInt() == SeqTypeConstants.LOGIN_OK)
//                    {
//                        int cur = 0;
//                        int size = object.toString().length();
//                        String msg = object.toString();
//                        outMsg.flush();
//                        while((cur+512) < size){
//                            outMsg.write(msg, cur, 512);
//                            cur += 512;
//                        }
//                        outMsg.write(msg, (cur - 512), (size-cur));
//                    }
//                    else    outMsg.println(object.toString());

                    System.out.println(object.toString().length());
                    System.out.println("s -> c " + msg);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(serverContext.getInfo().getStudentID());
        }finally {
            DataAccessObject.getInstance().setLoginFlag(serverContext.getInfo().getStudentID(), false);
            SystemServerSocket.getInstance().removeClient(serverContext.getInfo().getStudentID());
        }

    }

    public boolean compareQrString(String qrString){
        return serverContext.getQrString().equals(qrString);// true 같음 false 틀림
    }
    public boolean compareStudentId(String studentId){
        return serverContext.getInfo().getStudentID().equals(studentId);// true 같음 false 틀림
    }
    public void setAndroidLogoutFlag(boolean androidLogoutFlag) {
        this.androidLogoutFlag = androidLogoutFlag;
    }
}
