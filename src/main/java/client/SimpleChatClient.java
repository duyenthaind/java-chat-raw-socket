package client;

import config.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author duyenthai
 */
public class SimpleChatClient {

    private static final Logger LOGGER = Logger.getLogger("SimpleChatClient");

    private JTextArea incoming;
    private JTextField outgoing;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Socket socket;

    public static void main(String[] args) {
        SimpleChatClient simpleChatClient = new SimpleChatClient();
        simpleChatClient.start();
    }

    public void start() {
        JFrame jFrame = new JFrame("This is a simple chat client");
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane scrollerPane = new JScrollPane(incoming);
        scrollerPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollerPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(scrollerPane);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        setUpNetWorking();

        Thread client = new Thread(new IncomingReaderHandler.Builder()
                .withReader(bufferedReader)
                .withChatBox(incoming)
                .build());
        client.start();

        jFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        jFrame.setSize(600, 500);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setUpNetWorking() {
        try {
            this.socket = new Socket(Config.HOST, Config.PORT);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            printWriter = new PrintWriter(socket.getOutputStream());
            LOGGER.log(Level.INFO, String.format("Net work established, address=%s:%s, socket=%s", Config.HOST, Config.PORT, socket));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format("Error while setup network with host %s and port %s", Config.HOST, Config.PORT), ex);
        }
    }

    public class SendButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                printWriter.println(outgoing.getText());
                printWriter.flush();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Send msg error ", ex);
            } finally {
                outgoing.setText("");
                outgoing.requestFocus();
            }
        }
    }
}
