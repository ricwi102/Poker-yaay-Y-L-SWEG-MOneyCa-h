package poker;

import java.util.List;

public class Omaha extends PokerBase
{
    public Omaha(final List<Player> players, final Board board, final BettingRules bettingRules) {
	super(players, board, bettingRules);
    }

    @Override public void startSingleplayer() {
	super.startSingleplayer();
	dealCards();
	checkForAction();
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
