package server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class contains the open community cards that are available to all players during the game
 *
 * @author Johannes Palm Myllylä, RIchard Wigren
 * @version 1.0
 */

public class Board
{
    private List<Card> openCards;

    public Board() {
	openCards = new ArrayList<>();
    }

    public void addCard(Card card){
	openCards.add(card);
    }

    public Collection<Card> getOpenCards() {
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
