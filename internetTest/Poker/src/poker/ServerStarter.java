package poker;

public class ServerStarter{


    public static void main(String[] args) {
	ServerManager serverManager = new ServerManager();

	int port = 15388;

	serverManager.listenSocket(port);


    }
}
