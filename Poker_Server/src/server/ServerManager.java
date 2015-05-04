package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ServerManager
{
    private ServerSocket server = null;
    private List<ClientWorker> clients;
    private ClientHost host = null;

    public ServerManager() {
        clients = new ArrayList<>();
    }

    public void shutdown() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not close socket");
            System.exit(-1);
        }
        System.exit(0);
    }

    public PokerBase listenSocket(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not listen on port " + port);
            System.exit(-1);
        }
        while (true) {
            try {
                ClientWorker worker;
                if (host == null) {
                    worker = new ClientHost(server.accept());
                    host = (ClientHost) worker;
                    System.out.println("Host Found");
                } else {
                    worker = new ClientWorker(server.accept());
                    System.out.println("Client added!");
                }
                clients.add(worker);
                worker.addClientWorkers(clients);
                Thread t = new Thread(worker);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Accept failed: " + port);
                System.exit(-1);
            }
        }
    }
}




