package poker;

import java.util.Collections;

public class GameTest
{
    public static void main(String[] args) {
	Player[] players = new Player[2];
	players[0] = new Player("Richard");
	players[1] = new Player("Johannes");
	Board board = new Board();

	Holdem holdem = new Holdem(players,board);

	for (int i = 0; i < 10; i++) {
	    holdem.nextStreet();
	}
    }
}
