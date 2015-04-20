package poker;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private List<Card> hand;
    private String name;
    private String controller;
    private int chips;
    private int activeBet;
    private int tablePosition;
    private boolean active;
    private PlayerPosition position;

    public Player(String name) {
        hand = new ArrayList<>();
        this.name = name;
        controller = "player";
        active = true;
        tablePosition = 0;
        position = PlayerPosition.STANDARD;
        activeBet = 0;
        chips = 2*1000;
    }

    public Player(final String name, final String controller) {
        hand = new ArrayList<>();
        this.name = name;
        this.controller = controller;
        active = true;
        tablePosition = 0;
        position = PlayerPosition.STANDARD;
        activeBet = 0;
        chips = 2*1000;
    }

    public void addCard(Card card){
        hand.add(card);
    }

    public List<Card> getHand() { return hand; }

    public String getName() { return name; }

    public int getChips() { return chips; }

    public int getActiveBet() {return activeBet;}

    public int getTablePosition() { return tablePosition; }

    public PlayerPosition getPosition() { return position;}

    public String getController() { return controller; }

    public void resetHand(){ hand.clear(); }

    public void setPosition(PlayerPosition position) {this.position = position;}

    public void setTablePosition(final int tablePosition) { this.tablePosition = tablePosition; }

    public void activate(){ active = true;}

    public void check(){ }

    public void newRound(){ activeBet = 0; }

    public int bet(int amount){
        chips -= amount;
        activeBet += amount;
        return amount;
    }

    public int call(int amount){
        if(amount < chips){
            int chipToPot = amount - activeBet;
            chips -= chipToPot;
            activeBet = amount;
            return chipToPot;
        }else{
            amount = chips;
            chips = 0;
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
