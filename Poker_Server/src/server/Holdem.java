package server;

import java.util.List;

/**
 * Contains the rules of the poker game "Texas Hold'em"
 */

public class Holdem extends PokerBase{

    public Holdem(final List<Player> players, final Board board, final BettingRules bettingRules) {
	super(players, board,bettingRules);
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
