package poker;

/**
 * GameTest
 */
public final class GameTest
{
    private GameTest() {}

    public static void main(String[] args) {
	Player[] players = new Player[2];
	players[0] = new Player("Richard");
	players[1] = new Player("Johannes");
	Board board = new Board();

	Holdem holdem = new Holdem(players,board);

	PokerFrame frame = new PokerFrame(holdem);
	frame.pack();
	frame.setVisible(true);
    }
}
