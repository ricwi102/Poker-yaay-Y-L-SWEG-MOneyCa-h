package poker_client;

import poker.Card;
import poker.CardColor;
import poker.Player;
import poker.PlayerPosition;
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
                System.out.println("READLINE");
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
                                        switch (command[6]) {
                                            case "BIGBLIND":
                                                player.setPosition(PlayerPosition.BIGBLIND);
                                                break;
                                            case "SMALLBLIND":
                                                player.setPosition(PlayerPosition.SMALLBLIND);
                                                break;
                                            case "DEALER":
                                                player.setPosition(PlayerPosition.DEALER);
                                                break;
                                            default:
                                                player.setPosition(PlayerPosition.STANDARD);
                                                break;
                                        }
                                    }
                                }
                                break;
                            case "CARDS":
                                switch (command[2]){
                                    case "PLAYER":
                                        for (Player player : players) {
                                            if (player.getName().equals(command[3])){
                                                if (command.length > 4){
                                                    player.setHand(getCardsFromCommand(command[4]));
                                                }else{
                                                    player.resetHand();
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                    case "BOARD":
                                        break;
                                }

                                break;
                            case "BOARD":
                                if (command.length > 2){
                                    pokerRules.getBoard().setOpenCards(getCardsFromCommand(command[2]));
                                }else{
                                    pokerRules.getBoard().resetBoard();
                                }

                                break;
                            case "POT":
                                pokerRules.setPot(Integer.parseInt(command[2]));
                                pokerRules.getBettingRules().setLatestBet(Integer.parseInt(command[3]));
                                break;
                            case "YOU":
                                if (command[2].equals("CARDS")) {
                                    if(command.length > 2) {
                                        you.setHand(getCardsFromCommand(command[3]));
                                    } else {
                                        you.resetHand();
                                    }
                                }
                                break;
                            case "NEWSTREET":
                                pokerRules.resetPlayerBets();
                                pokerRules.getBettingRules().setLatestBet(0);
                                break;
                            case "TABLEPOSITIONS":
                                System.out.println(1);
                                String[] playerInfo = command[2].split("%");
                                for (String pInfo : playerInfo) {
                                    System.out.println(2);
                                    String[] position = pInfo.split("#");
                                    for (Player player : players) {
                                        System.out.println(3);
                                        if (player.getName().equals(position[0])){
                                            System.out.println(4);
                                            System.out.println("HEJ: " + position[1]);
                                            player.setTablePosition(Integer.parseInt(position[1]));
                                            break;
                                        }
                                    }
                                }
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
                    case "REMOVEPLAYER":
                        for (Player player : players) {
                            if (player.getName().equals(command[1])){
                                pokerRules.addPlayerToLosers(players.remove(players.indexOf(player)));
                                break;
                            }
                        }


                        if ((players.size() == 1 && players.contains(you))|| you.getName().equals(command[1])){
                            if (players.size() == 1){
                                pokerRules.addPlayerToLosers(players.get(0));
                            }
                            pokerRules.gameOver();
                            System.out.println("Game over: " + you.getName());
                        }
                        break;
                    case "GAMERULES":
                        frame.getLobbyComponent().setGameMode(command[1], host);
                        break;
                    case "BETRULES":
                        frame.getLobbyComponent().setBetRules(command[1], host);
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
                if (pokerRules != null && pokerRules.getFrame() != null){
                    pokerRules.getFrame().updateUi();
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

    public Player getYou() { return you; }

    public void addFrame(ClientFrame frame){this.frame = frame;}

    public void addPokerRules(PokerBase pokerRules){this.pokerRules = pokerRules;}

    public boolean isHost() { return host; }

    public PrintWriter getOut() { return out; }
}
