package server;

import java.util.ArrayList;
import java.util.List;

public class Board
{
    private List<Card> openCards;

    public Board() {
	openCards = new ArrayList<Card>();
    }

    public void addCard(Card card){
	openCards.add(card);
    }

    public List<Card> getOpenCards() {
	return openCards;
    }

    public void resetBoard(){
	openCards.clear();
    }

    @Override public String toString() {
    	StringBuilder builder = new StringBuilder();
    	for(Card card: openCards){
    	    builder.append(card);
    	    builder.append("%");
    	}
	return builder.toString();
    }
}
