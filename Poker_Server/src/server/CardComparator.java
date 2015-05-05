package server;

import java.util.Comparator;

/**
 * Compares the value of a card to another.
 */

public class CardComparator implements Comparator<Card>
{
    public int compare(Card card1, Card card2){

	if(card1.getValue() > card2.getValue()){
	    return 1;
	}else if(card1.getValue() < card2.getValue()){
	    return -1;
	}else{
	    return 0;
	}
    }

}
