package poker;



/**
 * This class contains the main method. Run this when you want to start the game.
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */
public final class GameStarter
{
    private GameStarter() {}

    public static void main(String[] args) {


	StartMenuFrame frame = new StartMenuFrame();
	frame.pack();
	frame.setVisible(true);

    }
}
