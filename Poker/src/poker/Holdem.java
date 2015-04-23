package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hold'em rules
 */

public class Holdem extends PokerBase{


    public Holdem(final List<Player> players, final Board board) {
	super(players, board,new NoLimit());
	ai = new Ai(this);
	dealCards();
    }

    @Override protected void dealCards() {
	super.dealCards();
	for(int i = 0; i < 2; i++) {
	    for (Player player : players) {
		player.addCard(deck.drawCard());
	    }
	}
    }
}
