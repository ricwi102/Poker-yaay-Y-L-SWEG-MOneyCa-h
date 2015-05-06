package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the connecting clients, creating an own thread for them and starts the communication
 * between the server and the clients.
 */


public class ServerManager
{
    private ServerSocket server = null;
    private List<ClientWorker> clients;
    private ClientHost host = null;

    public ServerManager() {
        clients = new ArrayList<>();
    }

    public void shutDown() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not close socket");
            System.exit(-1);
        }
    }

    public void listenSocket(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not listen on port " + port);
            System.exit(-1);
        }
        while (!server.isClosed()) {
            try {
                ClientWorker worker;
                if (host == null) {
                    worker = new ClientHost(server.accept(), this);
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
            } catch (IOException e) {   //will get a SockeException every time it closes while waiting for the "accept()" method
                                        //However, from the reading up i've done, the general consensus seems to be that it's fine if you catch it
                if (e.getMessage().equals("Socket closed")){
                    System.out.println("Closed Socket");
                } else {
                    System.out.println("Accept failed: " + port);
                    shutDown();
                }
                e.printStackTrace();
            }
            if (host.gameIsStarted() || clients.size() >= 9) {
                shutDown();
            }
        }
    }
}




