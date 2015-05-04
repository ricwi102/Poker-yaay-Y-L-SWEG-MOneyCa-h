package poker;


/**
 * This is the class for individual playing cards and contains information about the cards color and value
 *
 * @author Johannes Palm Myllylä, RIchard Wigren
 * @version 1.0
 */


public class Card
{

    private int value;
    private CardColor color;


    public Card(final int value, final CardColor color)  {
	this.value = value;
	this.color = color;



    }


    public int getValue() {
	return value;
    }

    public CardColor getColor() {
	return color;
    }


    @Override public String toString() {
	return value + " of " + color;
    }
}
