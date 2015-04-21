package poker_client;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private List<Card> hand;
    private String name;
    private int chips;
    private int activeBet;
    private int tablePosition;
    private boolean active;
    private PlayerPosition position;


    public Player(String name, int chips) {
        hand = new ArrayList<>();
        this.name = name;
        active = true;
        tablePosition = 0;
        position = PlayerPosition.STANDARD;
        activeBet = 0;
        this.chips = chips;
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

    public void resetHand(){ hand.clear(); }

    public void setHand(List<Card> hand){ this.hand = hand; }

    public void setPosition(PlayerPosition position) {this.position = position;}

    public void setTablePosition(final int tablePosition) { this.tablePosition = tablePosition; }

    public void setChips(int chips) {this.chips = chips;}

    public void setStatus(boolean status){ active = status;}

    public void setActiveBet(int bet) { activeBet = bet;}

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

