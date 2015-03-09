package poker;

import java.util.List;

public class PokerHand{
    private HandType handType;
    private List<Card> cards;

    public PokerHand(final HandType handType, final List<Card> cards) {
	this.handType = handType;
	this.cards = cards;
    }

    public HandType getHandType() {
	return handType;
    }

    public List<Card> getCards() {
	return cards;
    }
}
