package poker;


import java.net.URL;
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
    private PokerBase pokerBase;
    private List<Player> players;
    private BufferedImage hiddenImage;
    private BufferedImage foldedImage;
    private BufferedImage buttonImage;
    private Board board;
    private int numberOfPlayers;
    private int cardsPerPlayer;
    private final int cardWidth;
    private final int cardHeight;

    public PokerComponent(final PokerBase pokerBase) {
        this.pokerBase = pokerBase;
        players = pokerBase.getPlayers();
        board = pokerBase.getBoard();
        numberOfPlayers = players.size();
        cardsPerPlayer = players.get(0).getHand().size();
        cardWidth = 64;
        cardHeight = 116;
        try {
            URL url1 = getClass().getResource("images"+File.separator+"CardBack.jpg");
            File file1 = new File(url1.getPath());
            URL url2 = getClass().getResource("images"+ File.separator+ "FoldedCardBack.jpg");
            File file2 = new File(url2.getPath());
            URL url3 = getClass().getResource("images"+ File.separator + "DealerButton.png");
            File file3 = new File(url3.getPath());
            hiddenImage = ImageIO.read(file1);
            foldedImage = ImageIO.read(file2);
            buttonImage = ImageIO.read(file3);
        }catch(IOException e){
            hiddenImage = null;
            foldedImage = null;
            buttonImage = null;
        }

    }


    public Dimension getPreferedSize(){
        return new Dimension(players.size()*cardsPerPlayer*cardWidth,cardHeight * 3);
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawPlayerCards(g);
        drawOpenCards(g);
        drawPlayersAndChips(g);
    }

    private void drawPlayersAndChips(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        final int spaceFromCards = 16;
        final int buttonSize = 16;
        for(Player player : players){
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString(player.getName(), player.getTablePosition() * cardWidth * cardsPerPlayer, cardHeight + (spaceFromCards*2));
            g2.drawString("Chips: " + player.getChips(), player.getTablePosition() * cardWidth * cardsPerPlayer , cardHeight + (spaceFromCards*3));
            g2.drawString("Bet: "+player.getActiveBet(), player.getTablePosition() * cardWidth * cardsPerPlayer , cardHeight + (spaceFromCards*4));
            if(player.getPosition() == PlayerPosition.DEALER){
                g2.drawImage(buttonImage, player.getTablePosition() * cardWidth * cardsPerPlayer, cardHeight, buttonSize, buttonSize,
                             null);
            }else if(player.getPosition() == PlayerPosition.SMALLBLIND){
                g2.drawString("SB",player.getTablePosition() * cardWidth * cardsPerPlayer, cardHeight + spaceFromCards);
            }else if(player.getPosition() == PlayerPosition.BIGBLIND){
                g2.drawString("BB",player.getTablePosition() * cardWidth * cardsPerPlayer, cardHeight + spaceFromCards);
            }
        }
        g2.setFont(new Font("Arial",Font.BOLD,22));
        g2.drawString("Pot: "+ pokerBase.getPot(),((numberOfPlayers*cardsPerPlayer*cardWidth)/2) -cardWidth,cardHeight +(spaceFromCards*6));
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
                if(player.equals(pokerBase.getCurrentPlayer())) {
                    g2.drawImage(image, (player.getTablePosition() * cardsPerPlayer + currentCard) * cardWidth, 0, cardWidth, cardHeight,
                                 null);
                    currentCard++;
                }else if(!player.isActive()){
                    g2.drawImage(foldedImage,(player.getTablePosition() * cardsPerPlayer + currentCard) * cardWidth, 0, cardWidth, cardHeight,
                                 null);
                    currentCard++;
                } else{
                    g2.drawImage(hiddenImage,(player.getTablePosition() * cardsPerPlayer + currentCard) * cardWidth, 0, cardWidth, cardHeight,
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


