package poker;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class PokerComponent extends JComponent
{
    private List<Player> players;
    private List<BufferedImage> images;
    private BufferedImage testImage;

    public PokerComponent(final List<Player> players) {
        this.players = players;
        images = new ArrayList<>();
        try {
            testImage = ImageIO.read(new File("/home/johmy592/java/projekt/Poker-yaay-Y-L-SWEG-MOneyCa-h/Poker/images/AceOfSpades.png"));
        }catch(IOException e){
            testImage = null;
        }
        addImages();
    }


    public Dimension getPreferedSize(){
        return new Dimension(200,200);
    }

    private void addImages(){
        for (Player player : players) {
            for(Card card : player.getHand()){
                images.add(card.getImage());
            }
        }
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        List<BufferedImage> images;
        for (Player player : players) {
            int currentCard = 0;
            images = new ArrayList<>();
            for(Card card : player.getHand()){
                images.add(card.getImage());
                System.out.println("added image");
            }
            for(BufferedImage image : images){
                g2.drawImage(image, (player.getTablePosition() * 2 + currentCard) * 160 ,0,null);
                currentCard++;
            }
        }

       /* g2.drawImage(testImage,0,0,null);
        if(testImage == null) {
            System.out.println("null");
        }*/
    }

}


