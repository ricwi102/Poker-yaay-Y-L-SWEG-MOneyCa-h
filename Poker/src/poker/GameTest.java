package poker;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
/**
 * GameTest
 */
public final class GameTest
{
    private GameTest() {}

    public static void main(String[] args) throws IOException {
	List<Player> players = new ArrayList<>();
	players.add(new Player("Richard"));
	players.add(new Player("Johannes"));
	players.add(new Player("Axel"));
	players.add(new Player("Phil Ivey"));




	Board board = new Board();

	Holdem holdem = new Holdem(players,board);

	PokerFrame frame = new PokerFrame(holdem);

	frame.pack();
	frame.setVisible(true);

    }
}
