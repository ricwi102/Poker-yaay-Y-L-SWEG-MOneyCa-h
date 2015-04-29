package poker;


import java.util.List;

/**
 * Hold'em rules
 */

public class Holdem extends PokerBase{


    public Holdem(final List<Player> players, final Board board, final BettingRules bettingRules) {
	super(players, board,bettingRules);
    }

    @Override public void startSingleplayer() {
	super.startSingleplayer();
	dealCards();
	checkForAction();
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