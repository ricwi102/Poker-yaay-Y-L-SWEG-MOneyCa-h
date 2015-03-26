package poker;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Player
{
    private List<Card> hand;
    private String name;
    private int chips;
    private boolean called;
    private boolean active;

    public Player(String name) {
	hand = new ArrayList<Card>();
	this.name = name;
	active = true;
        called = true;
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

    public void resetHand(){
	hand.clear();
    }

    public boolean hasCalled() {
        return called;
    }

    public void setCalled(final boolean hasCalled) {
        this.called = hasCalled;
    }

    public void activate(){ active = true;}

    public void check(){}

    public int bet(){
        int ammount;
        do {
            String input = JOptionPane.showInputDialog("Ammount to bet: ");
            ammount = Integer.parseInt(input);
        } while (chips - ammount < 0);
        chips -= ammount;
        called = true;
        return ammount;

    }

    public int call(int ammount){
        if(ammount < chips){
            chips -= ammount;
            called = true;
            return ammount;
        }else{
            ammount = chips;
            called = true;
            chips = 0;
            return ammount;
        }
    }

    public void fold(){active = false;}

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
