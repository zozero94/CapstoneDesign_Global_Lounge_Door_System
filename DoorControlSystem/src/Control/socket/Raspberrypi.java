package control.socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.serverReaction.SystemServerSocket;
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
                System.out.println(msg);
                object = (JsonObject) parser.parse(msg);
                object = serverContextRA.response(object);
                if(object != null)
                    outMsg.println(object.toString());
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }finally {
            System.out.println("연결종료");
        }

    }
    public void openDoor(){
        JsonObject object = new JsonObject();
        try{
            object.addProperty("seqType", 700);
            this.setSendFlag(false);
            outMsg.println(object.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isSendFlag() {
        return sendFlag;
    }
    public synchronized void setSendFlag(boolean sendFlag) {
        this.sendFlag = sendFlag;
    }
}