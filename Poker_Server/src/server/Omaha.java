package server;

import java.util.List;

public class Omaha extends PokerBase
{
    public Omaha(final List<Player> players, final Board board, final BettingRules bettingRules) {
	super(players, board, bettingRules);
	dealCards();
	ai = new Ai(this);
    }

    @Override protected void dealCards() {
	super.dealCards();
	for(int i = 0; i < 4; i++) {
	    for (Player player : players) {
		player.addCard(deck.drawCard());
	    }
	}
    }
}
