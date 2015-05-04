package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class ClientHost extends ClientWorker
{
    private boolean gameStarted = false;
    private String gameMode = "HOLDEM";
    private String bettingRules = "NO_LIMIT";//No limit when implemented
    private static final Collection<String> GAME_MODES = Arrays.asList("HOLDEM", "OMAHA");//add all poker modes
    private static final Collection<String> BETTING_RULES = Arrays.asList("NOLIMIT", "POTLIMIT");

    public ClientHost(final Socket client) {
	super(client);
    }


    @Override protected void recieveOptions(final String[] command) {
	super.recieveOptions(command);
	hostOptions(command);
    }

    @Override protected void getInAndOutput() throws IOException {
	super.getInAndOutput();
	out.println("HOST&TRUE");
    }

    private void hostOptions(String[] command) {
	switch (command[0]) {
	    case "GAMERULES":
		if (GAME_MODES.contains(command[1])) {
		    gameMode = command[1];
		    for (ClientWorker client : clients) {
		    	client.sendMessageToOut("GAMERULES&" + gameMode);
		    }
		}
		break;
	    case "BETRULES":
		if (BETTING_RULES.contains(command[1])) {
		    bettingRules = command[1];
		    for (ClientWorker client : clients) {
			client.sendMessageToOut("BETRULES&" + bettingRules);
		    }

		}
		break;
	    case "STARTGAME":
		if (!gameStarted) {
		    startGame();
		    gameStarted = true;
		}
	}
    }

    private void startGame(){

	BettingRules bettingRules = createBettingRules(this.bettingRules);
	PokerBase pokerRules = createPokerRules(gameMode, bettingRules);

	for (ClientWorker client : clients) {
	    client.sendMessageToOut("STARTGAME&" + gameMode + "&" + this.bettingRules);
	    client.addPokerRules(pokerRules);
	}
	pokerRules.addClients(clients);
	pokerRules.sendUpdateNewRound();
	pokerRules.checkForAction();
    }

    private BettingRules createBettingRules(String bettingRules){
	switch (bettingRules) {
	    case "POTLIMIT":
		return new PotLimit();
	    case "NOLIMIT":
	    default:
		return new NoLimit();
	}
    }

    private PokerBase createPokerRules(String gameMode, BettingRules bettingRules) {
	switch (gameMode) {
	    case "OMAHA":
		return new Omaha(getClientPlayers(), new Board(), bettingRules);
	    case "HOLDEM":
	    default:
		return new Holdem(getClientPlayers(), new Board(), bettingRules);
	}
    }

    private List<Player> getClientPlayers(){
	List<Player> players = new ArrayList<>();
	for (ClientWorker client : clients) {
	    players.add(client.getPlayer());
	}
	return players;
    }
}

