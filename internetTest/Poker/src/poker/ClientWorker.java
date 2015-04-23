package poker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientWorker implements Runnable{

    private Socket client;
    private List<ClientWorker> clients;
    private PrintWriter out;
    private Player player;
    private PokerBase pokerRules;

    ClientWorker(Socket client) {
	this.client = client;

    }

    public void addClientWorkers(List<ClientWorker> clients){
	this.clients = clients;
    }

    public void addPokerRules(PokerBase pokerRules){ this.pokerRules = pokerRules; }

    public boolean hasPlayer(){return player != null;}

    private void sendError(String error){
	out.println("ERROR&" + error);
    }

    private void sendUpdate(){

    }

    public void run(){

	BufferedReader in = getInAndOutput();
	while(true){
	    try{
		String[] command;
		command = in.readLine().split("&");
		recieveOptions(command);
	    }catch (IOException e) {
		System.out.println("Read failed");
		System.exit(-1);
	    }
	}
    }
    protected BufferedReader getInAndOutput(){
	BufferedReader in = null;
	out = null;
	try{
	    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    out = new PrintWriter(client.getOutputStream(), true);
	} catch (IOException e) {
	    System.out.println("in or out failed");
	    System.exit(-1);
	}
	return in;
    }

    protected void recieveOptions(String[] command){
	switch (command[0]){
	    case "TEXT":
		for (ClientWorker clientWorker : clients) {
		    clientWorker.getOut().println("TEXT&" + player.getName() + "&" + command[1]);
		}
		break;
	    case "BET":
		int bet = Integer.parseInt(command[1]);
		if(player.equals(pokerRules.getCurrentPlayer())){
		    if(pokerRules.raise(bet)){
			sendUpdate();
		    }else{
			sendError("Can't bet that ammount");
		    }
		}
		break;
	    case "CALL":
		if(player.equals(pokerRules.getCurrentPlayer())){
		    if(pokerRules.call()){
			sendUpdate();
		    }else{
			sendError("There is nothing to call");
		    }
		}
		break;
	    case "CHECK":
		if(player.equals(pokerRules.getCurrentPlayer())){
		    if(pokerRules.check()){
			sendUpdate();
		    }else{
			sendError("There is an active bet, you can't check");
		    }
		}
		break;
	    case "FOLD":
		if(player.equals(pokerRules.getCurrentPlayer())){
		    pokerRules.fold();
		}
		break;
	    case "NEWPLAYER":
		player = new Player(command[1]);
		break;
	    case "LOGIN":
		//if we in the future want to save chips
		break;
	}


    }

    public PrintWriter getOut() {
	return out;
    }

    public Player getPlayer() { return player; }

    public PokerBase getPokerRules() { return pokerRules; }
}


