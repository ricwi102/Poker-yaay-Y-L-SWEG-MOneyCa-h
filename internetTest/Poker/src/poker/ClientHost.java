package poker;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;


public class ClientHost extends ClientWorker{
    private boolean startGame;
    private String gameMode = "HOLDEM";
    private String bettingRules = "NO_LIMIT";//No limit when implemented
    private static final Collection<String> GAME_MODES = Arrays.asList("HOLDEM", "OMAHA");//add all poker modes
	private static final Collection<String> BETTING_RULES = Arrays.asList("NO_LIMIT", "POT_LIMIT");

    public ClientHost(final Socket client) {
	super(client);
	startGame = false;
    }

    public String getGameMode() { return gameMode; }

    public String getBettingRules() { return bettingRules; }

    public boolean getStartGame(){ return startGame;}

    @Override public void run(){

	BufferedReader in = getInAndOutput();
	getOut().println("HOST&TRUE");

	while(true){
	    try{
		String[] command;
		command = in.readLine().split("&");
		recieveOptions(command);
		hostOptions(command);
	    }catch (IOException e) {
		System.out.println("Read failed");
		System.exit(-1);
	    }
	}
    }



    private void hostOptions(String[] command){
	switch (command[0]) {
	    case "RULES":
	    	if (GAME_MODES.contains(command[1])) gameMode = command[1];
	    	break;
	    case "BETRULES":
			if (BETTING_RULES.contains(command[1])) bettingRules = command[1];
			break;

	    case "STARTGAME":
		startGame = true;
		}
    }
}

