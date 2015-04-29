package poker;


import poker_client.Client;

import java.net.URI;
import java.net.URISyntaxException;
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
    private BufferedImage[][] deckImages;
    private Client client;
    private static final int NUMBEROFCOLORS = 4;
    private static final int NUMBEROFVALUES = 13;
    private Board board;
    private int numberOfPlayers;
    private int cardsPerPlayer;
    private static final int CARD_WIDTH = 64;
    private static final int CARD_HEIGHT = 116;
    private static final int SPACE_BETWEEN_PLAYERS = 15;

    public PokerComponent(final PokerBase pokerBase) {
        this.pokerBase = pokerBase;
        deckImages = new BufferedImage[NUMBEROFCOLORS][NUMBEROFVALUES];
        players = pokerBase.getPlayers();
        board = pokerBase.getBoard();
        numberOfPlayers = players.size();
        if(pokerBase instanceof Omaha){
            cardsPerPlayer = 4;
        }else {
            cardsPerPlayer = 2;
        }
        try {
            URI url1 = getClass().getResource("images" + File.separator + "CardBack.jpg").toURI();
            File file1 = new File(url1.getPath());

            URI url2 = getClass().getResource("images"+ File.separator+ "FoldedCardBack.jpg").toURI();
            File file2 = new File(url2.getPath());

            URI url3 = getClass().getResource("images"+ File.separator + "DealerButton.png").toURI();
            File file3 = new File(url3.getPath());


            hiddenImage = ImageIO.read(file1);
            foldedImage = ImageIO.read(file2);
            buttonImage = ImageIO.read(file3);
        }catch(IOException | URISyntaxException e){
            System.out.println("could not find image");
            hiddenImage = null;
            foldedImage = null;
            buttonImage = null;
        }

        addCardImages();
    }

    private void addCardImages(){
        final int cardWidth = 73;
        final int cardHeight = 98;
        try{
            URI url1 = getClass().getResource("images" + File.separator + "Deck.jpg").toURI();
       	    File file1 = new File(url1.getPath());
       	    BufferedImage deckImage = ImageIO.read(file1);
       	    for(int i = 0; i < NUMBEROFCOLORS; i++){
       		for(int j = 0; j < NUMBEROFVALUES; j++){
        	    deckImages[i][j] = deckImage.getSubimage(j * cardWidth, i * cardHeight, cardWidth, cardHeight);
        	}
            }
       	}catch(URISyntaxException |IOException e){
       	    BufferedImage deckImage = null;
       	}
    }


    public Dimension getPreferedSize(){
        final int numberOfOpenCards = 5;
        if(players.size()*cardsPerPlayer* CARD_WIDTH + SPACE_BETWEEN_PLAYERS * (players.size() - 1) > CARD_WIDTH *numberOfOpenCards) {
            return new Dimension(players.size()*cardsPerPlayer* CARD_WIDTH + SPACE_BETWEEN_PLAYERS * (players.size() - 1), CARD_HEIGHT * 3);
        }else{
            return new Dimension(CARD_WIDTH *numberOfOpenCards, CARD_HEIGHT * 3);
        }
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
            g2.drawString(player.getName(), player.getTablePosition() * (CARD_WIDTH * cardsPerPlayer + SPACE_BETWEEN_PLAYERS), CARD_HEIGHT + (spaceFromCards*2));
            g2.drawString("Chips: " + player.getChips(), player.getTablePosition() * (CARD_WIDTH * cardsPerPlayer + SPACE_BETWEEN_PLAYERS) , CARD_HEIGHT + (spaceFromCards*3));
            g2.drawString("Bet: "+player.getActiveBet(), player.getTablePosition() * (CARD_WIDTH * cardsPerPlayer + SPACE_BETWEEN_PLAYERS), CARD_HEIGHT + (spaceFromCards*4));
            if(player.getPosition() == PlayerPosition.DEALER){
                g2.drawImage(buttonImage, player.getTablePosition() * (CARD_WIDTH * cardsPerPlayer + SPACE_BETWEEN_PLAYERS), CARD_HEIGHT, buttonSize, buttonSize,
                             null);
            }else if(player.getPosition() == PlayerPosition.SMALLBLIND){
                g2.drawString("SB",player.getTablePosition() * (CARD_WIDTH * cardsPerPlayer + SPACE_BETWEEN_PLAYERS), CARD_HEIGHT + spaceFromCards);
            }else if(player.getPosition() == PlayerPosition.BIGBLIND){
                g2.drawString("BB",player.getTablePosition() * (CARD_WIDTH * cardsPerPlayer + SPACE_BETWEEN_PLAYERS), CARD_HEIGHT + spaceFromCards);
            }
        }
        g2.setFont(new Font("Arial",Font.BOLD,22));
        g2.drawString("Pot: "+ pokerBase.getPot(),((numberOfPlayers*cardsPerPlayer* CARD_WIDTH)/2) - CARD_WIDTH,
                      CARD_HEIGHT +(spaceFromCards*6));
    }

    private void drawPlayerCards(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        List<BufferedImage> images;

        for (Player player : players) {
            int currentCard = 0;
            images = new ArrayList<>();
            for(Card card : player.getHand()){
                if(card.getValue() < 14) {
                    images.add(deckImages[card.getColor().getValue()][card.getValue() - 1]);
                }else{
                    images.add(deckImages[card.getColor().getValue()][0]);
                }
            }

            for(BufferedImage image : images){
                if((!pokerBase.isMultiplayer() && player.equals(pokerBase.getCurrentPlayer())) ||
                   (pokerBase.isMultiplayer() && !images.isEmpty() && player.isActive())) {
                    g2.drawImage(image, (player.getTablePosition() * cardsPerPlayer + currentCard) * CARD_WIDTH
                                        + player.getTablePosition() * SPACE_BETWEEN_PLAYERS, 0, CARD_WIDTH,
                                 CARD_HEIGHT,
                                 null);
                    currentCard++;
                }else if(!player.isActive()){
                    g2.drawImage(foldedImage,(player.getTablePosition() * cardsPerPlayer + currentCard) * CARD_WIDTH
                                             + player.getTablePosition() * SPACE_BETWEEN_PLAYERS, 0,
                                 CARD_WIDTH, CARD_HEIGHT,
                                 null);
                    currentCard++;
                } else{
                    g2.drawImage(hiddenImage,(player.getTablePosition() * cardsPerPlayer + currentCard) * CARD_WIDTH
                                             + player.getTablePosition() * SPACE_BETWEEN_PLAYERS, 0,
                                 CARD_WIDTH, CARD_HEIGHT,
                                 null);
                    currentCard++;
                }
            }
            if(images.isEmpty() && player.isActive()){
                for(int i = 0; i < cardsPerPlayer; i++){
                    g2.drawImage(hiddenImage,(player.getTablePosition() * cardsPerPlayer + currentCard) * CARD_WIDTH
                                             + player.getTablePosition() * SPACE_BETWEEN_PLAYERS, 0, CARD_WIDTH, CARD_HEIGHT, null);
                    currentCard++;
                }
            }else if(images.isEmpty() && !player.isActive()){
                for(int i = 0; i < cardsPerPlayer; i++){
                    g2.drawImage(foldedImage,(player.getTablePosition() * cardsPerPlayer + currentCard) * CARD_WIDTH
                                             + player.getTablePosition() * SPACE_BETWEEN_PLAYERS, 0, CARD_WIDTH, CARD_HEIGHT, null);
                    currentCard++;
                }
            }
            if(player.equals(pokerBase.getCurrentPlayer())){
                int thickness = 2;
                Stroke oldStroke = g2.getStroke();
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(thickness));
                g2.drawRect(player.getTablePosition() * cardsPerPlayer * CARD_WIDTH
                             + player.getTablePosition() * SPACE_BETWEEN_PLAYERS, 0, cardsPerPlayer* CARD_WIDTH, CARD_HEIGHT);
                g2.setColor(Color.BLACK);
                g2.setStroke(oldStroke);
            }
        }
    }

    private void drawOpenCards(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        List<BufferedImage> images = new ArrayList<>();
        int currentCard = 0;
        final int spacing = 110;

        for(Card card : board.getOpenCards()){
            if(card.getValue() < 14) {
                images.add(deckImages[card.getColor().getValue()][card.getValue() - 1]);
            }else{
                images.add(deckImages[card.getColor().getValue()][0]);
            }
        }
        for(BufferedImage image : images){
            g2.drawImage(image, currentCard* CARD_WIDTH, CARD_HEIGHT + spacing, CARD_WIDTH, CARD_HEIGHT, null);
            currentCard++;
        }
    }

    public void addClient(Client client){
        this.client = client;
    }

}


