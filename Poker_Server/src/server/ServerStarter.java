package server;

/**
 * Starts the server letting clients connect on a given port.
 */


public final class ServerStarter{

    private ServerStarter() {}

    private static final int PORT = 15388;

    public static void main(String[] args) {
	ServerManager serverManager = new ServerManager();

	serverManager.listenSocket(PORT);


    }
}
