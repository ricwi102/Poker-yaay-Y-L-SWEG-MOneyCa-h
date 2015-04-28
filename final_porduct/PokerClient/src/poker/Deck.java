package poker;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Deck
{
    private List<Card> deck;


    public Deck() {
	deck = new ArrayList<Card>();
	makeDeck();
    }

    
    private void makeDeck(){
	final int values = 14;
	for(int j = 0; j < 4; j++){
	    for (int i = 2; i <= values; i++) {
		if(i == 14){
		    deck.add(new Card(i,CardColor.values()[j]));
		}else {
		    deck.add(new Card(i, CardColor.values()[j]));
		}
	    }
	}
    }

    public void shuffleDeck(){
	deck.clear();
	makeDeck();
    }

    public List<Card> getDeck() {
	return deck;
    }

    public Card drawCard(){
	Random rand = new Random();
	int n = rand.nextInt(deck.size());
	return deck.remove(n);
    }
}
