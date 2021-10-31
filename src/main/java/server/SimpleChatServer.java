package server;

import config.Config;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author duyenthai
 */
public class SimpleChatServer {
    private static final Logger LOGGER = Logger.getLogger("SimpleChatServer");
    public static final List<PrintWriter> CLIENT_OUTPUT_STREAMS = new CopyOnWriteArrayList<>();

    private boolean isRunning = true;

    public void start() {
        try {
            try (ServerSocket serverSocket = new ServerSocket(Config.PORT)) {

                LOGGER.log(Level.INFO, String.format("Init simpleChatServer at %s, listening to connections", new Date()));

                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                    CLIENT_OUTPUT_STREAMS.add(writer);

                    Thread client = new Thread(new ClientHandler(clientSocket));
                    client.start();
                    LOGGER.log(Level.INFO, String.format("New connection, socket: %s", clientSocket));
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("Init server via socket error, time %s, port %s", new Date(), Config.PORT), ex);
        }
    }

    public void stop() {
        this.isRunning = false;
    }

    public static void main(String[] args) {
        new SimpleChatServer().start();
    }
}
