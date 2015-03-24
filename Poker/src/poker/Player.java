package poker;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Player
{
    private List<Card> hand;
    private String name;
    private int chips;
    public boolean active;

    public Player(String name) {
	hand = new ArrayList<Card>();
	this.name = name;
	active = true;
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

    public void resetHand(){
	hand.clear();
    }

    public void activate(){ active = true;}

    public void check(){}

    public int bet(){
	String input = JOptionPane.showInputDialog("Ammount to bet: ");
	int ammount = Integer.parseInt(input);
	chips -= ammount;
	return ammount;
    }

    public void fold(){active = false;}

    public boolean isActive(){
	return active;
    }

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
