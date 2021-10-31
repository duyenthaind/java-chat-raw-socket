package client;

import javax.swing.*;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author duyenthai
 */
public class IncomingReaderHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger("IncomingReaderHandler");

    private BufferedReader bufferedReader;
    private JTextArea incoming;

    public IncomingReaderHandler(Builder builder) {
        this.bufferedReader = builder.bufferedReader;
        this.incoming = builder.incoming;
    }


    @Override
    public void run() {
        String message;

        try {
            while ((message = bufferedReader.readLine()) != null) {
                LOGGER.log(Level.INFO, "read " + message);
                incoming.append(message + "\n");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error", ex);
        }
    }

    public static class Builder {

        private BufferedReader bufferedReader;
        private JTextArea incoming;

        public Builder withReader(BufferedReader bufferedReader) {
            this.bufferedReader = bufferedReader;
            return this;
        }

        public Builder withChatBox(JTextArea incoming) {
            this.incoming = incoming;
            return this;
        }

        public IncomingReaderHandler build() {
            return new IncomingReaderHandler(this);
        }
    }
}
