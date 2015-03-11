package poker;

import java.util.ArrayList;
import java.util.List;

public class PokerHand{
    private HandType handType;
    private List<Card> cards;
    private int handStrength;

    public PokerHand(final HandType handType, final List<Card> cards) {
	this.handType = handType;
	this.cards = cards;
        handStrength = handType.getValue();
    }

    public PokerHand(){
        cards = new ArrayList<>();
    }

    public HandType getHandType() {
	return handType;
    }

    public List<Card> getCards() {
	return cards;
    }

    public int getHandStrength() { return handStrength; }
}
