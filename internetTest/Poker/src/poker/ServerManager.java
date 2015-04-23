package poker;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ServerManager extends Thread{
    private ServerSocket server;
    private List<ClientWorker> clients;
    private ClientHost host;

    public ServerManager() { clients = new ArrayList<>(); }

    public void shutdown(){
	try{
	    server.close();
	} catch (IOException e) {
	    System.out.println("Could not close socket");
	    System.exit(-1);
	}
	System.exit(0);
    }

    public PokerBase listenSocket(int port) {
	try {
	    server = new ServerSocket(port);
	} catch (IOException e) {
	    System.out.println("Could not listen on port " + port);
	    System.exit(-1);
	}
	while (true) {
	    if (host != null && host.getStartGame() && clientsHavePlayers()) {
		switch(host.getGameMode()){
		    case "HOLDEM":
			if (host.getBettingRules() != null){
			    return new Holdem(new Board(), host.getBettingRules());
			}else{
			    return new Holdem(new Board());
			}
		    case "OMAHA":
			if (host.getBettingRules() != null){
			    return new Holdem(new Board(), host.getBettingRules());
			}else{
			    return new Holdem(new Board());//change to omaha later
			}
		}
		return new Holdem(new Board(), host.getBettingRules());
	    }
	    try {
		ClientWorker worker;
		if (host == null){
		    worker = new ClientHost(server.accept());
		    host = (ClientHost)worker;
		    System.out.println("Host Found");
		}else{
		    worker = new ClientWorker(server.accept());
		    System.out.println("Client added!");
		}
		clients.add(worker);
		worker.addClientWorkers(clients);
		Thread t = new Thread(worker);
		t.start();
	    } catch (IOException e) {
		System.out.println("Accept failed: " + port);
		System.exit(-1);
	    }
	}
    }

    public boolean clientsHavePlayers(){
	for (ClientWorker client : clients) {
	    if (client.getPlayer() == null) return false;
	}
	return true;
    }

    public List<ClientWorker> getClients() {
	return clients;
    }
}

