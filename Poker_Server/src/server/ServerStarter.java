package server;

/**
 * Starts the server letting clients connect on a given port.
 */


public final class ServerStarter{

    private ServerStarter() {}

    public static void main(String[] args) {
	ServerManager serverManager = new ServerManager();

	int port = 15388;

	PokerBase pokerRules = serverManager.listenSocket(port);

	pokerRules.checkForAction();

    }
}
