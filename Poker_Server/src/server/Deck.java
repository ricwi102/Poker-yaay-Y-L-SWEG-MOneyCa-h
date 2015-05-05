package server;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates a deck of 52 cards of different colors and values, from which cards are then drawn when advancing the game.
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
