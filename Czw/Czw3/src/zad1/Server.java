package zad1;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.time.format.*;
import java.util.*;

public class Server extends JFrame implements Runnable {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Set<String> connectedClients = new TreeSet<>();
    private Selector serverSelector;
    private int port;

    public static void main(String[] args) {
//        Server server = new Server(Integer.parseInt(args[0]));
        Server server = new Server(3000);
        server.run();
    }

    private static void log(String str) {
        System.out.println("Server: " + str);
    }

    Server(int port) {
        this.port = port;
    }

    private void handleRequest(String request, SocketChannel client) throws IOException {
        String[] commandAndData = request.split("/@/@/@/@/");
        String command = commandAndData[0], data = commandAndData[1];
        log(command + ": " + data);

        switch (command) {
            case "login": {
                connectedClients.add(data);
                log(connectedClients.toString());
                broadcast("User " + data + " has joined the channel");
                break;
            }
            case "message": {
                broadcast(data);
                break;
            }
            case "logout": {
                connectedClients.remove(data);
                log(connectedClients.toString());
                broadcast("User " + data + " has left the channel");
                break;
            }
            default:
                break;
        }
    }

    private void broadcast(String data) throws IOException {
        String stringMessage = "[" + dtf.format(LocalDateTime.now()) + "] " + data;

        byte[] message = stringMessage.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(message);

        for (SelectionKey key : serverSelector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel sch = (SocketChannel) key.channel();
                sch.write(buffer);
                buffer.rewind();
            }
        }

        buffer.clear();
    }

    @Override
    public void run() {
        try {
            serverSelector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            int ops = serverSocketChannel.validOps();
            serverSocketChannel.register(serverSelector, ops, null);

            while (serverSocketChannel.isOpen()) {
                serverSelector.select();

                Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();

                while (selectionKeyIterator.hasNext()) {
                    SelectionKey myKey = selectionKeyIterator.next();

                    if (myKey.isAcceptable()) {
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(serverSelector, SelectionKey.OP_READ);
                        log("Connection Accepted: " + clientChannel.getRemoteAddress());
                    } else if (myKey.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) myKey.channel();
                        ByteBuffer clientBuffer = ByteBuffer.allocate(2048);
                        clientChannel.read(clientBuffer);
                        String result = new String(clientBuffer.array()).trim();
                        handleRequest(result, clientChannel);
                    }

                    selectionKeyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}