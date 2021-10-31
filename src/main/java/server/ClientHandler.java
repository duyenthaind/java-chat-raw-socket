package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author duyenthai
 */
public class ClientHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("ClientHandler");

    BufferedReader bufferedReader;
    Socket socket;


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("Error init client handler for client with socket %s", socket), ex);
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = bufferedReader.readLine()) != null) {
                LOGGER.log(Level.INFO, "read " + message);
                flushMsgToAllMember(message);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("Error while handling client handler for client with socket %s", socket), ex);
        }
    }

    public void flushMsgToAllMember(String message){
        Iterator iterator = SimpleChatServer.clientOutPutStream.iterator();
        while(iterator.hasNext()){
            try{
                PrintWriter printWriter = (PrintWriter) iterator.next();
                printWriter.println(message);
                printWriter.flush();
            } catch (Exception ex){
                LOGGER.log(Level.SEVERE, "Error while sending msg to all client ", ex);
            }

        }
    }
}
