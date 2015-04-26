package poker_client;

import java.io.IOException;


public class GameStarter{


    public static void main(String[] args) throws IOException{
        GameInfo gameInfo = new GameInfo();
        Client client = new Client(gameInfo);
        ClientFrame clientFrame = new ClientFrame(gameInfo, client);
        client.addFrame(clientFrame);


        clientFrame.connectToServerFrame();

        clientFrame.pack();
        clientFrame.setVisible(true);


    }
}


