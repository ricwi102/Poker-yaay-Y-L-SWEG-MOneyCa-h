package poker;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card
{
    private BufferedImage image;
    private int value;
    private CardColor color;

    public Card(final int value, final CardColor color)  {
	this.value = value;
	this.color = color;
        try {
            image = ImageIO.read(new File("/home/johmy592/java/projekt/Poker-yaay-Y-L-SWEG-MOneyCa-h/Poker/images/AceOfSpades.png"));
        }catch(IOException e){
            image = null;
        }
    }

    public int getValue() {
	return value;
    }

    public CardColor getColor() {
	return color;
    }

    public BufferedImage getImage() { return image; }

    @Override public String toString() {
	return value + " of " + color;
    }
}
