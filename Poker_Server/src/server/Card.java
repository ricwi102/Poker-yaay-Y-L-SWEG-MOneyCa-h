package server;


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
	return value + "#" + color;
    }
}
