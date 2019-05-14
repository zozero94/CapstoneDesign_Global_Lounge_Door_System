package control.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.serverReaction.SystemServerSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketThreadRA extends Thread{


    private JsonParser parser;
    private JsonObject objectIn;
    private JsonObject objectOut;

    private BufferedReader inMsg;
    private PrintWriter outMsg;

    private String msg;


    public SocketThreadRA(Socket socket) throws Exception{
        this.inMsg = new BufferedReader(new InputStreamReader((socket.getInputStream())));
        this.outMsg = new PrintWriter(socket.getOutputStream(), true);
        parser = new JsonParser();
    }

    public void run(){
        try{
            while(true) {
                msg = inMsg.readLine();
                if (msg == null) break;
                System.out.println(msg);
                objectIn = (JsonObject) parser.parse(msg);


                if(objectIn.get("seqType").getAsString().equals("300")){
                    objectOut = new JsonObject();
                    if(SystemServerSocket.getInstance().compareQrString(objectIn.get("data").getAsString())) objectOut.addProperty("seqType", "301");
                    else objectOut.addProperty("seqType", "302");
                }
                else if(objectIn.get("seqType").getAsString().equals("400")){
                    // 방금 인증한 qr코드가 지나감
                    System.out.println(msg + "지나감");
                    // 데이터 베이스에 해당 학번 저장
                }else if(objectIn.get("seqType").getAsString().equals("401")){
                    // 방금 인증한 qr코드가 지나가지 않음
                    System.out.println(msg + "안지나감");
                }
                if(objectOut != null) {
                    outMsg.write(objectOut.toString());
                    outMsg.flush();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }finally {
            System.out.println("연결종료");
        }

    }
}
