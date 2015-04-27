package poker;

import java.util.List;

public final class ServerStarter{

    private ServerStarter() {}

    public static void main(String[] args) {
	ServerManager serverManager = new ServerManager();

	int port = 15388;

	PokerBase pokerRules = serverManager.listenSocket(port);




	pokerRules.checkForAction();

    }
}
