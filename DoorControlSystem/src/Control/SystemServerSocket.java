package Control;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class SystemServerSocket {

    private ServerSocket serverSocket;
    private Socket socket;

    private Logger logger;

    public void start(){
        logger = Logger.getLogger(this.getClass().getName());
        try{
            serverSocket = new ServerSocket(5050);
            logger.info("Server Start");

            while(true){
                socket = serverSocket.accept();
                logger.info("Client access");

            }
        }catch (Exception e){
            logger.info("SystemServer Exception start()");
            e.printStackTrace();
        }
    }
}
