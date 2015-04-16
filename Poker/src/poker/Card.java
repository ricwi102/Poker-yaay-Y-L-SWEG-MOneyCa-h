package poker;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card
{
    private BufferedImage deckImage;
    private BufferedImage openImage;
    private int value;
    private CardColor color;
    private final int width;
    private final int height;

    public Card(final int value, final CardColor color)  {
	this.value = value;
	this.color = color;
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

    private void getCardImage(){
        if(value == 14){
            openImage = deckImage.getSubimage(0, color.getValue() * height, width, height);
        }else {
            openImage = deckImage.getSubimage((value - 1) * width, color.getValue() * height, width, height);
        }
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
