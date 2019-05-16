package control.socket;

import com.google.gson.JsonObject;
import control.SeqTypeConstants;

import java.io.BufferedReader;
import java.net.Socket;

public class SocketFactory {
    public static SocketThread getSocket(JsonObject object, Socket socket, BufferedReader reader){
        SocketThread socketThread = null;
        try {
            if (object.get("seqType").getAsString().equals(SeqTypeConstants.LOGIN) || object.get("seqType").getAsString().equals(SeqTypeConstants.LOGIN_IOS))
                socketThread = new Aplication(socket, object.toString(), reader);
             else
                socketThread = new Raspberrypi(socket, reader);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return socketThread;
        }
    }
}
