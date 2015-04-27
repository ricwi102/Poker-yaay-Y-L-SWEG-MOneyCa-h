package poker_client;

import poker.Card;
import poker.CardColor;
import poker.Player;
import poker.PokerBase;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable
{
    private PokerBase pokerRules;
    private ClientFrame frame;
    private PrintWriter out;
    private BufferedReader in;
    private List<Player> players = new ArrayList<>();
    private Player you;
    private boolean host = false;


    public void listenSocket(int port, InetAddress address) {
	try {
	    Socket socket;
	    socket = new Socket(address, port);
	    out = new PrintWriter(socket.getOutputStream(), true);
	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	} catch (UnknownHostException e) {
	    System.out.println("Unknown host");
	} catch (IOException e) {
	    System.out.println("No I/O");
	}
    }

    public void run() {
	while (true) {
	    try {
            String[] command = in.readLine().split("&");

            switch (command[0]){
                case "TEXT":

                break;
                case "UPDATE":
                    switch (command[1]){
                        case "CURRENTPLAYER":
                            for (Player player : players) {
                                if (command[2].equals(player.getName())){
                                    pokerRules.setCurrentPlayer(player);
                                }
                            }
                            break;
                        case "PLAYER":
                            for (Player player : players) {
                                if (command[2].equals(player.getName())){
                                    player.setChips(Integer.parseInt(command[3]));
                                    player.setActiveBet(Integer.parseInt(command[4]));
                                    player.setActive(Boolean.parseBoolean(command[5]));
                                }
                            }
                            break;
                        case "BOARD":
                            pokerRules.getBoard().setOpenCards(getCardsFromCommand(command[2]));
                            break;
                        case "POT":
                            pokerRules.setPot(Integer.parseInt(command[2]));
                            break;
                        case "YOU":
                            you.setHand(getCardsFromCommand(command[2]));
                            break;

                    }
                    break;
                case "ADDPLAYER":
                    if (command.length > 3){
                        System.out.println("SERVER ADDED PLAYER, YOU");
                        you = new Player(command[1]);
                        players.add(you);
                        frame.lobbyFrame();
                    } else {
                        System.out.println("server added player, someone else");
                        players.add(new Player(command[1]));
                    }
                    frame.getLobbyComponent().updatePlayersInLobby(players);
                    break;
                case "VALID":

                break;
                case "ERROR":

                break;
                case "STARTGAME":
                    frame.startGame(command[1], command[2], players);
                    break;
                case "HOST":
                    if (command[1].equals("TRUE")) host = true;
                    System.out.println(host);
                    break;
            }
	    } catch (IOException e) {
		System.out.println("Read failed");
		System.exit(-1);
	    }
	}
    }

    public List<Card> getCardsFromCommand(String command){
        List<Card> cards = new ArrayList<>();
        String[] cardStrings = command.split("%");
        for (String card : cardStrings) {
            String[] cardInfo = card.split("#");
            switch(cardInfo[1]){
                case "HEARTS":
                    cards.add(new Card(Integer.parseInt(cardInfo[0]), CardColor.HEARTS));
                    break;
                case "DIAMONDS":
                    cards.add(new Card(Integer.parseInt(cardInfo[0]), CardColor.DIAMONDS));
                    break;
                case "CLUBS":
                    cards.add(new Card(Integer.parseInt(cardInfo[0]), CardColor.CLUBS));
                    break;
                case "SPADES":
                    cards.add(new Card(Integer.parseInt(cardInfo[0]), CardColor.SPADES));
                    break;
            }
        }
        return cards;
    }

    public void addFrame(ClientFrame frame){this.frame = frame;}

    public void addPokerRules(PokerBase pokerRules){this.pokerRules = pokerRules;}

    public boolean isHost() { return host; }

    public PrintWriter getOut() { return out; }
}
