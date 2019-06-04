package control.socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.SeqTypeConstants;
import control.serverReaction.raspberrypi.ServerContextRA;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Raspberrypi extends SocketThread implements Runnable{

    private JsonParser parser;

    private BufferedReader inMsg;
    private PrintWriter outMsg;

    private String msg;
    private JsonObject object;
    private ServerContextRA serverContextRA;
    private boolean sendFlag;

    private static final String OPEN = "{\"seqType\":\""+ SeqTypeConstants.OPEN+"\"}";

    public Raspberrypi(Socket socket, BufferedReader reader) throws Exception{
        this.inMsg = reader;
        this.outMsg = new PrintWriter(socket.getOutputStream(), true);
        this.parser = new JsonParser();
        this.serverContextRA = new ServerContextRA(this);
        SystemServerSocket.getInstance().setRaspberrypi(this);
        sendFlag = true;
    }

    public void run(){
        try{
            while(true) {
                msg = inMsg.readLine();
                if (msg == null) break;
                System.out.println("RA run"+msg);
                object = (JsonObject) parser.parse(msg);
                object = serverContextRA.response(object);
                if(object != null) {
                    System.out.println("RA response"+ object.toString());
                    outMsg.println(object.toString());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            SystemServerSocket.getInstance().setRaspberrypi(null);
        }finally {
            SystemServerSocket.getInstance().setRaspberrypi(null);
        }
    }
    public void openDoor(){
            if(isSendFlag()) {
                this.setSendFlag(false);
                outMsg.println(OPEN);
            }
    }
    public boolean isSendFlag() {
        return sendFlag;
    }
    public synchronized void setSendFlag(boolean sendFlag) {
        this.sendFlag = sendFlag;
    }
}