package poker;

import java.util.ArrayList;
import java.util.List;

/**
 * GameTest
 */
public final class GameTest
{
    private GameTest() {}

    public static void main(String[] args) {
	List<Player> players = new ArrayList<>();
	players.add(new Player("Richard","ai"));
	players.add(new Player("Johannes"));
	players.add(new Player("Axel","ai"));
	players.add(new Player("Phil Ivey"));




	Board board = new Board();

	PokerBase pokerBase = new Holdem(players,board);

	PokerFrame frame = new PokerFrame(pokerBase);

	frame.pack();
	frame.setVisible(true);
	pokerBase.checkForAction();

    }
}
