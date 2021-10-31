package server;

import config.Config;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author duyenthai
 */
public class SimpleChatServer {
    private static final Logger LOGGER = Logger.getLogger("SimpleChatServer");
    public static List clientOutPutStream;

    public void start(){
        clientOutPutStream = new ArrayList();
        try{
            ServerSocket serverSocket = new ServerSocket(Config.PORT);

            LOGGER.log(Level.INFO, "Init simpleChatServer, listening to connections");

            while(true){
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutPutStream.add(writer);

                Thread client = new Thread(new ClientHandler(clientSocket));
                client.start();
                LOGGER.log(Level.INFO, "New connection, socket: " + clientSocket);
            }

        } catch (Exception ex){
            LOGGER.log(Level.SEVERE, String.format("Init server via socket error, time %s, port %s", new Date(), Config.PORT), ex);
        }
    }

    public static void main(String[] args) {
        new SimpleChatServer().start();
    }
}
