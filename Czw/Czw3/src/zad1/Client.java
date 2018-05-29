package zad1;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client extends JFrame implements Runnable {
    private int hostPort;
    private String host;
    private boolean loggedIn = false;
    private SocketChannel clientChannel;
    private String name;

    private JPanel cards;
    private JTextArea chatTextArea;
    private final static String CHAT_PANEL = "Card with Chat";
    private final static String LOGIN_PANEL = "Card with Login";

    private static void log(String str) {
        System.out.println("Client: " + str);
    }

    public static void main(String[] args) {
//        Client client = new Client(Integer.parseInt(args[0]));
        Client client = new Client("localhost", 3000);
        client.run();
    }

    Client(String host, int hostPort) {
        this.host = host;
        this.hostPort = hostPort;

        Container mainPane = getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setPreferredSize(new Dimension(640, 480));

        JPanel loginCard = getLoginPanel();
        JPanel chatCard = getChatPanel();

        cards = new JPanel(new CardLayout());

        cards.add(loginCard, LOGIN_PANEL);
        cards.add(chatCard, CHAT_PANEL);

        mainPane.add(cards, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void sendRequest(String data) throws IOException {
        byte[] message = data.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(message); // write out message to buffer
        clientChannel.write(buffer);
        buffer.clear();
    }

    private void read() throws IOException {
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int buffLength = clientChannel.read(buffer);

            if (buffLength > 0 && loggedIn) {
                String read = new String(buffer.array()).trim();
                chatTextArea.append(read + "\n");
            }
        }
    }

    private JPanel getChatPanel() {
        JPanel jPanel = new JPanel();

        JButton logoutButton = new JButton("Logout");
        JButton sendMessageButton = new JButton("Send");
        JTextField messageTextField = new JTextField("", 5);

        chatTextArea = new JTextArea(15, 20);
        JScrollPane messagePane = new JScrollPane(chatTextArea);
        chatTextArea.setEditable(false);
        chatTextArea.setFont(new Font("Serif", Font.ITALIC, 16));
        chatTextArea.setLineWrap(true);
        chatTextArea.setWrapStyleWord(true);

        logoutButton.addActionListener(e -> {
            try {
                this.loggedIn = false;
                sendRequest("logout/@/@/@/@/" + name);
                CardLayout cl = (CardLayout) (cards.getLayout());
                cl.show(cards, LOGIN_PANEL);
                chatTextArea.setText("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        sendMessageButton.addActionListener(e -> {
            try {
                String message = messageTextField.getText();
                if (!message.isEmpty()) {
                    sendRequest("message/@/@/@/@/" + name + ": " + message);
                    messageTextField.setText("");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        jPanel.add(logoutButton);
        jPanel.add(messagePane);
        jPanel.add(messageTextField);
        jPanel.add(sendMessageButton);

        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        return jPanel;
    }

    private JPanel getLoginPanel() {
        JPanel jPanel = new JPanel();
        JTextField nicknameTextField = new JTextField("", 5);
        JButton confirmButton = new JButton("Login!");

        confirmButton.addActionListener(e -> {
            try {
                String name = nicknameTextField.getText();
                if (!name.isEmpty()) {
                    this.name = name;
                    this.loggedIn = true;
                    sendRequest("login/@/@/@/@/" + name);
                    CardLayout cl = (CardLayout) (cards.getLayout());
                    cl.show(cards, CHAT_PANEL);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        jPanel.add(nicknameTextField);
        jPanel.add(confirmButton);

        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        return jPanel;
    }

    @Override
    public void run() {
        try {
            InetSocketAddress hostAddress = new InetSocketAddress(host, hostPort);
            clientChannel = SocketChannel.open();
            clientChannel.connect(hostAddress);
            clientChannel.configureBlocking(false);
            read();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
