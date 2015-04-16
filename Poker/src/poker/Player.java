package poker;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private List<Card> hand;
    private String name;
    private int chips;
    private int activeBet;
    private int tablePosition;
    private boolean raised;
    private boolean called;
    private boolean active;
    private PlayerPosition position;

    public Player(String name) {
        hand = new ArrayList<Card>();
        this.name = name;
        active = true;
        called = true;
        raised = false;
        tablePosition = 0;
        position = PlayerPosition.STANDARD;
        activeBet = 0;
        chips = 2000;
    }

    public void addCard(Card card){
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public int getChips() { return chips; }

    public int getActiveBet() {return activeBet;}

    public int getTablePosition() { return tablePosition; }

    public PlayerPosition getPosition() { return position;}

    public void resetHand(){
        hand.clear();
    }

    public boolean hasCalled() {
        return called;
    }

    public boolean hasRaised(){return raised;}

    public void setCalled(final boolean hasCalled) {
        this.called = hasCalled;
    }

    public void setRaised(final boolean hasRaised){
        this.raised = hasRaised;
    }

    public void setPosition(PlayerPosition position) {this.position = position;}

    public void setTablePosition(final int tablePosition) { this.tablePosition = tablePosition; }

    public void activate(){ active = true;}

    public void check(){
        called = true;
        raised = false;
    }

    public void newRound(){
        called = true;
        raised = false;
        activeBet = 0;
    }

    public int bet(int ammount){
        chips -= ammount;
        called = true;
        raised = true;
        activeBet += ammount;
        return ammount;
    }

    public int call(int ammount){
        if(ammount < chips){
            int chipToPot = ammount - activeBet;
            chips -= chipToPot;
            activeBet = ammount;
            called = true;
            raised = false;
            return chipToPot;
        }else{
            ammount = chips;
            called = true;
            raised = false;
            chips = 0;
            return ammount;
        }
    }

    public void fold(){
        active = false;
        raised = false;
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
