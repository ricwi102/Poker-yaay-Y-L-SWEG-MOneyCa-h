package poker;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class PokerComponent extends JComponent
{
    private Holdem holdem;
    private List<Player> players;
    private BufferedImage hiddenImage;
    private BufferedImage foldedImage;
    private Board board;
    private int numberOfPlayers;
    private int cardsPerPlayer;
    private final int cardWidth;
    private final int cardHeight;

    public PokerComponent(final Holdem holdem) {
        this.holdem = holdem;
        players = holdem.getPlayers();
        board = holdem.getBoard();
        numberOfPlayers = players.size();
        cardsPerPlayer = players.get(0).getHand().size();
        cardWidth = 64;
        cardHeight = 116;
        try {
            hiddenImage = ImageIO.read(new File("/home/johmy592/java/projekt/Poker-yaay-Y-L-SWEG-MOneyCa-h/Poker/images/CardBack.jpg"));
            foldedImage = ImageIO.read(new File("/home/johmy592/java/projekt/Poker-yaay-Y-L-SWEG-MOneyCa-h/Poker/images/FoldedCardBack.jpg"));
        }catch(IOException e){
            hiddenImage = null;
            foldedImage = null;
        }

    }


    public Dimension getPreferedSize(){
        return new Dimension(players.size()*2*cardWidth,cardHeight * 3);
    }



    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawPlayerCards(g);
        drawOpenCards(g);
        drawPlayersAndChips(g);
    }

    private void drawPlayersAndChips(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        final int spaceFromCards = 18;
        for(Player player : players){
            g2.setFont(new Font("Arial",Font.BOLD,18));
            g2.drawString(player.getName(), player.getTablePosition() * cardWidth * cardsPerPlayer , cardHeight + spaceFromCards);
            g2.drawString(""+player.getActiveBet(), player.getTablePosition() * cardWidth * cardsPerPlayer , cardHeight + (spaceFromCards*2));
        }
        g2.setFont(new Font("Arial",Font.BOLD,22));
        g2.drawString("Pot: "+holdem.getPot(),((numberOfPlayers*cardsPerPlayer*cardWidth)/2) -cardWidth,cardHeight +(spaceFromCards*6));
    }

    private void drawPlayerCards(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        List<BufferedImage> images;

        for (Player player : players) {
            int currentCard = 0;
            images = new ArrayList<>();
            for(Card card : player.getHand()){
                images.add(card.getOpenImage());
            }
            for(BufferedImage image : images){
                if(player.equals(holdem.getCurrentPlayer())) {
                    g2.drawImage(image, (player.getTablePosition() * 2 + currentCard) * cardWidth, 0, cardWidth, cardHeight,
                                 null);
                    currentCard++;
                }else if(!player.isActive()){
                    g2.drawImage(foldedImage,(player.getTablePosition() * 2 + currentCard) * cardWidth, 0, cardWidth, cardHeight,
                                 null);
                    currentCard++;
                }else{
                    g2.drawImage(hiddenImage,(player.getTablePosition() * 2 + currentCard) * cardWidth, 0, cardWidth, cardHeight,
                                 null);
                    currentCard++;
                }
            }
        }
    }

    private void drawOpenCards(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        List<BufferedImage> images = new ArrayList<>();
        int currentCard = 0;
        final int spacing = 110;

        for(Card card : board.getOpenCards()){
            images.add(card.getOpenImage());
        }
        for(BufferedImage image : images){
            g2.drawImage(image, currentCard*cardWidth, cardHeight + spacing, cardWidth, cardHeight, null);
            currentCard++;
        }
    }

}


