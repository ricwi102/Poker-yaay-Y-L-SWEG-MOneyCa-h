package poker_client;

import poker.Card;
import poker.CardColor;
import poker.Player;
import poker.PlayerPosition;
import poker.PokerBase;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Socket socket = null;


    public void listenSocket(int port, InetAddress address) throws UnknownHostException, IOException {
        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host");
            throw e;
        } catch (IOException e) {
            System.out.println("No I/O");
            throw e;
        }
    }

    public void run() {
        while (true) {
            try {
                String[] command = in.readLine().split("&");
                commandOptions(command);
            } catch (IOException e) {
                System.out.println("Read failed");
                e.printStackTrace();
                System.exit(-1);
            }

                if (pokerRules != null && pokerRules.getFrame() != null) {
                    pokerRules.getFrame().updateUi();
                }

                /*switch (command[0]){
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
                }*/
        }
    }

    private void commandOptions(String[] commands){
        switch (commands[0]) {
            case "TEXT":

                break;
            case "UPDATE":
                updateOptions(getRestOfArray(commands));
                break;
            case "ADDPLAYER":
                addPlayer(getRestOfArray(commands));
                break;
            case "REMOVEPLAYER":
                removePlayer(getRestOfArray(commands));
                break;
            case "GAMERULES":
                frame.getLobbyComponent().setGameMode(commands[1], host);
                break;
            case "BETRULES":
                frame.getLobbyComponent().setBetRules(commands[1], host);
                break;
            case "ERROR":
                showErrorMessage(commands[1]);
                break;
            case "STARTGAME":
                frame.startGame(commands[1], commands[2], players);
                break;
            case "HOST":
                if (commands[1].equals("TRUE")) host = true;
                break;
        }

    }

    private void updateOptions(String[] commands){

        switch (commands[0]) {
            case "CURRENTPLAYER":
                setCurrentPlayer(commands[1]);
                break;
            case "PLAYER":
                updatePlayer(getRestOfArray(commands));
                break;
            case "CARDS":
                updateCardOptions(getRestOfArray(commands));
                break;
            case "POT":
                pokerRules.setPot(Integer.parseInt(commands[1]));
                pokerRules.getBettingRules().setLatestBet(Integer.parseInt(commands[2]));
                break;
            case "NEWSTREET":
                pokerRules.resetPlayerBets();
                pokerRules.getBettingRules().setLatestBet(0);
                break;
            case "TABLEPOSITIONS":
                setPlayerPosition(getRestOfArray(commands));
                break;
        }
    }

    private void updateCardOptions(String[] commands) {
        switch (commands[0]) {
            case "PLAYER":
                setPlayerCards(getPartOfArray(commands, 2, commands.length), getPlayerFromName(commands[1]));
                break;
            case "YOU":
                setPlayerCards(getRestOfArray(commands), you);
                break;
            case "BOARD":
                setBoardCards(getRestOfArray(commands));
                break;
        }
    }

    private void showErrorMessage(String error){
        JOptionPane.showMessageDialog(null, error);
    }

    private void addPlayer(String[] playerInfo){
        if (playerInfo.length > 1) {
            System.out.println("SERVER ADDED PLAYER, YOU");
            you = new Player(playerInfo[0]);
            players.add(you);
            frame.lobbyFrame();
        } else {
            System.out.println("server added player, someone else");
            players.add(new Player(playerInfo[0]));
        }
        frame.getLobbyComponent().updatePlayersInLobby(players);
    }

    private void removePlayer(String[] playerInfo) {
        for (Player player : players) {
            if (player.getName().equals(playerInfo[0])) {
                pokerRules.addPlayerToLosers(players.remove(players.indexOf(player)));
                break;
            }
        }
        if (isGameOverForYou(playerInfo[0])) {
            gameOver();
        }
    }

    private boolean isGameOverForYou(String name){
        return (players.size() == 1 && players.contains(you)) || you.getName().equals(name);
    }

    private void gameOver(){
        if (players.size() == 1) {
            pokerRules.addPlayerToLosers(players.get(0));
        }
        pokerRules.gameOver();
        System.out.println("Game over: " + you.getName());
    }



    private String[] getRestOfArray(String[] array){
       return getPartOfArray(array, 1, array.length);
    }

    private String[] getPartOfArray(String[] array, int start, int end){
        return Arrays.copyOfRange(array, start, end);
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

    private void setCurrentPlayer(String name) {
        for (Player player : players) {
            if (name.equals(player.getName())) {
                pokerRules.setCurrentPlayer(player);
            }
        }
    }

    private void setPlayerCards(String[] cards, Player player){
        if (cards.length > 0) {
            player.setHand(getCardsFromCommand(cards[0]));
        } else {
            player.resetHand();
        }
    }

    private void setBoardCards(String[] cards){
        if (cards.length > 0) {
            pokerRules.getBoard().setOpenCards(getCardsFromCommand(cards[0]));
        } else {
            pokerRules.getBoard().resetBoard();
        }
    }

    private void setPlayerPosition(String[] info){
        String[] playerInfo = info[0].split("%");
        for (String pInfo : playerInfo) {
            String[] position = pInfo.split("#");
            for (Player player : players) {
                if (player.getName().equals(position[0])) {
                    player.setTablePosition(Integer.parseInt(position[1]));
                    break;
                }
            }
        }
    }


    private void updatePlayer(String[] info){
        for (Player player : players) {
            if (info[0].equals(player.getName())){
                player.setChips(Integer.parseInt(info[1]));
                player.setActiveBet(Integer.parseInt(info[2]));
                player.setActive(Boolean.parseBoolean(info[3]));
                switch (info[4]) {
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
                break;
            }
        }
    }

    private Player getPlayerFromName(String name){
        for (Player player : players) {
            if (player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    public Player getYou() { return you; }

    public void addFrame(ClientFrame frame){this.frame = frame;}

    public void addPokerRules(PokerBase pokerRules){this.pokerRules = pokerRules;}

    public boolean isHost() { return host; }

    public PrintWriter getOut() { return out; }
}
