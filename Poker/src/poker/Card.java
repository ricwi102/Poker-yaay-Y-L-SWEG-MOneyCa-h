package poker;



import java.awt.image.BufferedImage;


public class Card
{

    private BufferedImage openImage;
    private int value;
    private CardColor color;


    public Card(final int value, final CardColor color, final BufferedImage openImage)  {
	this.value = value;
	this.color = color;
        this.openImage = openImage;



    }


    public int getValue() {
	return value;
    }

    public CardColor getColor() {
	return color;
    }

    public BufferedImage getOpenImage() { return openImage; }

    @Override public String toString() {
	return value + " of " + color;
    }
}
