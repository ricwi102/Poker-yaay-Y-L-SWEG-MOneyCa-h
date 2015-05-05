package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Handles all the communication between the server and the client
 */

public class ClientWorker implements Runnable{

    private Socket client;
    protected List<ClientWorker> clients = null;
    protected PrintWriter out = null;
    protected BufferedReader in = null;
    private Player player = null;
    private PokerBase pokerRules = null;
    private boolean closed = false;


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

    public void shutDown(){
        clients.remove(this);
        closed = true;
        try{
            in.close();
            out.close();
            client.close();
        } catch (IOException exc){
            exc.printStackTrace();
            System.out.println("Failed to close the client completely");
        }
    }

    public void run(){

        try {
            getInAndOutput();
        } catch (IOException exc){
            exc.printStackTrace();
            System.exit(-1);
        }
        while (true) {
            if (closed){
                break;
            }
            try {
                String[] command;
                command = in.readLine().split("&");
                receiveOptions(command);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Read failed");
                shutDown();
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

    protected void receiveOptions(String[] command){
        switch (command[0]){
            case "TEXT":
                for (ClientWorker clientWorker : clients) {
                        clientWorker.out.println("TEXT&" + player.getName() + "&" + command[1]);
                }
                break;
            case "RAISE":
                int bet = Integer.parseInt(command[1]);
                if(canDoAction() && !pokerRules.raise(bet)){
                        sendError("Can't bet that ammount");
                }
                break;
            case "CALL":
                if(canDoAction()){
                    pokerRules.call();
                }
                break;
            case "CHECK":
                if(canDoAction() && !pokerRules.check()){
                        sendError("There is an active bet, you can't check");
                }
            break;
            case "FOLD":
                if(canDoAction()){
                    pokerRules.fold();
                }
                break;
            case "ALLIN":
                if (canDoAction() && pokerRules.getBettingRules().isLegalAllIn()){
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
        clients.stream().filter(worker -> !worker.equals(this) && worker.player != null).forEach(worker -> {
            out.println("ADDPLAYER&" + worker.player.getName());
            worker.out.println("ADDPLAYER&" + name);
        });
    }



    public void sendMessageToOut(String message){
        out.println(message);
    }

    private boolean isCurrentPlayer(){ return player.equals(pokerRules.getCurrentPlayer());}

    private boolean lookingForAction(){ return pokerRules.isCheckingForAction();}

    private boolean canDoAction(){ return lookingForAction() && isCurrentPlayer();}

    public Player getPlayer() { return player; }
}


