package zad1;

public class Main {
    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> {
            Server server = new Server(3000);
            server.run();
        });
        Thread clientThread = new Thread(() -> {
            Client client = new Client("localhost", 3000);
            client.run();
        });
        Thread client2Thread = new Thread(() -> {
            Client client = new Client("localhost", 3000);
            client.run();
        });

        serverThread.start();
        client2Thread.start();
        clientThread.start();
    }
}
