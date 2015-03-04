package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Holdem
{
    private Player[] players;
    private Board board;
    private Deck deck;
    private int dealCounter;

    public Holdem(final Player[] players, final Board board) {
	this.players = players;
	this.board = board;
	dealCounter = 0;
	deck = new Deck();
    }

    private void dealCards(){
	for(int i = 0; i < 2; i++) {
	    for (Player player : players) {
		player.addCard(deck.drawCard());
	    }
	}
    }

    private void dealFlop(){
	for (int i = 0; i < 3; i++) {
	    board.addCard(deck.drawCard());
	}
    }

    private void dealTurn(){
	dealOneCard();
    }

    private void dealRiver(){
	dealOneCard();
    }

    private void dealOneCard(){
	board.addCard(deck.drawCard());
    }

    private List<Card> getBestHand(Player player){
	List<Card> cards = new ArrayList<Card>();
	cards.addAll(board.getOpenCards());
	cards.addAll(player.getHand());
    }

    private List<Card> getFlush(List<Card> cards){
	List<Card> testHand = new ArrayList<Card>();
	for (int i = 0; i < 4; i++) {
	    testHand = new ArrayList<Card>();
	    CardColor currentColor = CardColor.values()[i];
	    for (Card card : cards) {
		if(card.getColor() == currentColor){
		    testHand.add(card);
		}
	    }
	    if (testHand.size() >= 5){

		break;
	    }
	}
	if (testHand.size() >= 5){
	    sortHand(testHand);
	    while (testHand.size() > 5){
		testHand.remove(0);
	    }
	    return testHand;
	}
	return null;
    }

    public void sortHand(List<Card> cards){
    	Collections.sort(cards, new CardComparator());
        }

    public void nextStreet(){
	switch(dealCounter){
	    case 0:
		dealCards();
		dealCounter++;
		break;
	    case 1:
		dealFlop();
		dealCounter++;
		break;
	    case 2:
		dealTurn();
		dealCounter++;
		break;
	    case 3:
		dealRiver();
		dealCounter = 0;
		break;
	}
	for (Player player : players) {
	    System.out.println(player);
	}
	System.out.println(board);
    }
}
