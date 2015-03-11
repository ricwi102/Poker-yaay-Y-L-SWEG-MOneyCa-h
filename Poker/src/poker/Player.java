package poker;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private List<Card> hand;
    private String name;
    public boolean isActive;

    public Player(String name) {
	hand = new ArrayList<Card>();
	this.name = name;
	isActive = true;
    }

    public void addCard(Card card){
	hand.add(card);
    }

    public List<Card> getHand() {
	return hand;
    }

    public String getName() {
	return name;
    }

    public void resetHand(){
	hand.clear();
    }



    @Override public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append(name);
	builder.append(": ");
	for(Card card: hand){
	    builder.append(card);
	    builder.append(", ");
	}

	return builder.toString();
    }
}
