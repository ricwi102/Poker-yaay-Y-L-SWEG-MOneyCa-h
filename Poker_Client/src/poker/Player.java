package poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is the class for individual players in the game. Contains all relevant information for each player, like table
 * position, cards, chips and so on.
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public class Player
{
    private List<Card> hand  = new ArrayList<>();
    private String name;
    private String controller;
    private int chips = STARTING_CHIPS;
    private int activeBet = 0;
    private int totalBetThisRound = 0;
    private int tablePosition = 0;
    private boolean active = true;
    private PlayerPosition position = PlayerPosition.STANDARD;
    private PokerHand bestHand = null;

    private final static int STARTING_CHIPS = 2000;

    public Player(String name) {
        this.name = name;
        controller = "player";
    }

    public Player(final String name, final String controller) {
        this.name = name;
        this.controller = controller;
    }

    public void addCard(Card card){
        hand.add(card);
    }

    public Collection<Card> getHand() { return hand; }

    public String getName() { return name; }

    public int getChips() { return chips; }

    public int getActiveBet() {return activeBet;}

    public int getTablePosition() { return tablePosition; }

    public PlayerPosition getPosition() { return position;}

    public String getController() { return controller; }

    public int getTotalBetThisRound() { return totalBetThisRound; }

    public PokerHand getBestHand() { return bestHand; }

    public void resetHand(){ hand.clear(); }

    public void setPosition(PlayerPosition position) {this.position = position;}

    public void setTablePosition(final int tablePosition) { this.tablePosition = tablePosition; }

    public void setChips(final int chips) { this.chips = chips; }

    public void setActiveBet(final int activeBet) { this.activeBet = activeBet; }

    public void setActive(boolean active){ this.active = active;}

    public void setHand(final List<Card> hand) { this.hand = hand; }

    public void setBestHand(PokerHand bestHand) { this.bestHand = bestHand; }

    private void resetTotalBet(){ totalBetThisRound = 0;}

    private void resetBestHand(){ bestHand = null;}

    public void resetActiveBet() { activeBet = 0; }

    public void newRound(){
        resetActiveBet();
        resetBestHand();
        resetTotalBet();
    }

    public int bet(int amount){
        chips -= amount;
        activeBet += amount;
        totalBetThisRound += amount;
        return amount;
    }

    public int call(int amount){
        if(amount - activeBet < chips){
            int chipToPot = amount - activeBet;
            chips -= chipToPot;
            activeBet = amount;
            totalBetThisRound += chipToPot;
            return chipToPot;
        }else{
            amount = chips;
            chips = 0;
            activeBet += amount;
            totalBetThisRound += amount;
            return amount;
        }
    }

    public void fold(){
        active = false;
    }

    public boolean isActive(){
        return active;
    }

    public void addChips(int winnings){ chips += winnings;  }

    @Override public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(": ");
        for(Card card: hand){
            builder.append(card);
            builder.append(", ");
        }

        return builder.toString();
    }
}
