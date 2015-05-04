package poker;


import java.util.List;

/**
 *This is the class for made poker hands. It contains a list of the cards in the hand, a HandType (flush, pair etc)
 * and the player who is the owner of the hand.
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public class PokerHand{
    private HandType handType;
    private List<Card> cards;
    private Player player;

    public PokerHand(final HandType handType, final List<Card> cards, final Player player) {
	this.handType = handType;
	this.cards = cards;
        this.player = player;
    }


    public Player getPlayer() {
        return player;
    }

    public List<Card> getCards() {
	return cards;
    }

    public int getHandStrength() { return handType.getValue(); }
}
