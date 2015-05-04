package poker;

import java.util.Comparator;

/**
 * This comparator defines how we compare 2 cards with each other.
 *
 * @author Johannes Palm Myllylä, RIchard Wigren
 * @version 1.0
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
