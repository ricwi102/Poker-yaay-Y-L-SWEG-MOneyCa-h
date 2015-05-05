package server;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class Creates instances of the Card class to make a playing-card deck. The draw card method can then be used
 * to get a random card from the deck.
 *
 * @author Johannes Palm Myllylä, RIchard Wigren
 * @version 1.0
 */

public class Deck
{
    private List<Card> deck;
    private static final int NUMBER_OF_COLORS = 4;
    private static final int HIGHEST_VALUE = 14;


    public Deck() {
	deck = new ArrayList<>();
	makeDeck();
    }
    
    private void makeDeck(){
	for(int j = 0; j < NUMBER_OF_COLORS; j++){
	    for (int i = 2; i <= HIGHEST_VALUE; i++) {
		deck.add(new Card(i,CardColor.values()[j]));
	    }
	}
    }

    public void shuffleDeck(){
	deck.clear();
	makeDeck();
    }

    public Card drawCard(){
	Random rand = new Random();
	int n = rand.nextInt(deck.size());
	return deck.remove(n);
    }
}
