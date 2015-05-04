package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientWorker implements Runnable{

    private Socket client;
    protected List<ClientWorker> clients = null;
    protected PrintWriter out = null;
    protected BufferedReader in = null;
    private Player player = null;
    private PokerBase pokerRules = null;


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

    public void run(){

        try {
            getInAndOutput();
        } catch (IOException exc){
            exc.printStackTrace();
            System.exit(-1);
        }
        while (true) {
            try {
                String[] command;
                command = in.readLine().split("&");
                recieveOptions(command);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Read failed");
                System.exit(-1);
            }
        }
    }

    protected void getInAndOutput() throws IOException{
        out = null;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException exc) {
            System.out.println("in or out failed");
            throw exc;
        }
    }

    protected void recieveOptions(String[] command){
        switch (command[0]){
            case "TEXT":
                for (ClientWorker clientWorker : clients) {
                        clientWorker.out.println("TEXT&" + player.getName() + "&" + command[1]);
                }
                break;
            case "RAISE":
                int bet = Integer.parseInt(command[1]);
                if(isCurrentPlayer() && lookingForAction()){
                    if(!pokerRules.raise(bet)){
                        sendError("Can't bet that ammount");
                    }
                }
                break;
            case "CALL":
                if(isCurrentPlayer() && lookingForAction()){
                    pokerRules.call();
                }
                break;
            case "CHECK":
                if(isCurrentPlayer() && lookingForAction()){
                    if(!pokerRules.check()){
                        sendError("There is an active bet, you can't check");
                    }
                }
            break;
            case "FOLD":
                if(isCurrentPlayer() && lookingForAction()){
                    pokerRules.fold();
                }
                break;
            case "ALLIN":
                if (isCurrentPlayer() && lookingForAction() && pokerRules.getBettingRules().isLegalAllIn()){
                    pokerRules.allIn();
                }
                break;
            case "NEWPLAYER":
                if (!(hasPlayer())) {
                   addNewPlayer(command[1]);
                }
            break;
        }
    }

    public void sendPlayerCards(){
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE&CARDS&YOU&");
        for (Card card: player.getHand()){
            builder.append(card);
            builder.append("%");
        }
        out.println(builder);
    }

    private void addNewPlayer(String name){
        System.out.println("player added");
        player = new Player(name);
        out.println("ADDPLAYER&" + name + "&YOU");
        for (ClientWorker worker : clients) {
            if (!worker.equals(this) && worker.player != null){
                out.println("ADDPLAYER&" + worker.player.getName());
                worker.out.println("ADDPLAYER&" + name);
            }
        }
    }

    public void sendMessageToOut(String message){
        out.println(message);
    }

    private boolean isCurrentPlayer(){ return player.equals(pokerRules.getCurrentPlayer());}

    private boolean lookingForAction(){ return pokerRules.isCheckingForAction();}

    public Player getPlayer() { return player; }
}


