package poker_client;

import java.io.IOException;
import java.net.InetAddress;

public class GameStarter{


    public static void main(String[] args) throws IOException{
	GameInfo gameInfo = new GameInfo();
	ClientFrame clientFrame = new ClientFrame(gameInfo);
	Client client = new Client(gameInfo, clientFrame);

	clientFrame.connectToServerFrame();

	int port = 0;
	InetAddress address = InetAddress.getLocalHost();

	client.listenSocket(port, address);


	server = ConnectToServerFrame (ip, port);

	-->

	serverLobbyFrame;

	-->

	getStartGame from server;

	-->
	
	ClientFrame;

    }
}
