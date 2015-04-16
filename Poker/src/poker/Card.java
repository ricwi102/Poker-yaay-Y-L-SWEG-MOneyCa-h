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
<<<<<<< HEAD
        width = 73;
        height = 98;
        /*
        try {
            deckImage = ImageIO.read(new File("/home/johmy592/java/projekt/Poker-yaay-Y-L-SWEG-MOneyCa-h/Poker/images/Deck.jpg"));
        }catch(IOException e){
            deckImage = null;
        }
        getCardImage();*/
    }
=======
        this.openImage = openImage;


>>>>>>> origin/master

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
