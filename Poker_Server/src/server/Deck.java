package server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck
{
    private List<Card> deck;
    private int numberOfValues;
    private int numberOfColors;
    private BufferedImage[][] deckImages;
    private final int cardWidth;
    private final int cardHeight;


    public Deck() {
	deck = new ArrayList<Card>();
	numberOfValues = 13;
	numberOfColors = 4;
	cardWidth = 73;
	cardHeight = 98;
	deckImages = new BufferedImage[numberOfColors][numberOfValues];
	addCardImages();
	makeDeck();
    }

    private void addCardImages(){
	try{
	    URI url1 = getClass().getResource("images" + File.separator + "Deck.jpg").toURI();
	    File file1 = new File(url1.getPath());
	    BufferedImage deckImage = ImageIO.read(file1);
	    for(int i = 0; i < numberOfColors; i++){
		for(int j = 0; j < numberOfValues; j++){
		    deckImages[i][j] = deckImage.getSubimage(j * cardWidth, i * cardHeight, cardWidth, cardHeight);
		}
	    }
	}catch(Exception e){
	    BufferedImage deckImage = null;
	}

    }
    
    private void makeDeck(){
	final int values = 14;
	for(int j = 0; j < 4; j++){
	    for (int i = 2; i <= values; i++) {
		if(i == 14){
		    deck.add(new Card(i,CardColor.values()[j],deckImages[j][0]));
		}else {
		    deck.add(new Card(i, CardColor.values()[j], deckImages[j][i - 1]));
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
