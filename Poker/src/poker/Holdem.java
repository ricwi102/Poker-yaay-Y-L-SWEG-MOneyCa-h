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

    public static void main(String[] args) {

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

    private PokerHand getBestHand(Player player){
	final int fourOfAKind = 4;
	final int threeOfAKind = 3;
	final int pair = 2;
	List<Card> cards = new ArrayList<>();
	cards.addAll(board.getOpenCards());
	cards.addAll(player.getHand());
	List<List<Card>> numOfAKind = getSameValueCards(cards);
	if(getStraightFlush(cards) != null){
	    return new PokerHand(HandType.STRAIGHTFLUSH, getStraightFlush(cards));
	}else if(getNumOfAKind(numOfAKind, cards, fourOfAKind) != null) {
	    return new PokerHand(HandType.FOUROFAKIND, getNumOfAKind(numOfAKind, cards, fourOfAKind));
	}else if(getNumOfAKind(numOfAKind, cards, threeOfAKind, pair) != null) {
	    return new PokerHand(HandType.FULLHOUSE, getNumOfAKind(numOfAKind, cards, threeOfAKind, pair));
	} else if(getFlush(cards) != null){
	    return new PokerHand(HandType.FLUSH, getFlush(cards));
	}else if(getStraight(cards) != null){
	    return new PokerHand(HandType.STRAIGHT, getStraight(cards));
	}else if(getNumOfAKind(numOfAKind, cards, threeOfAKind) != null) {
	    return new PokerHand(HandType.THREEOFAKIND, getNumOfAKind(numOfAKind, cards, threeOfAKind));
	}else if(getNumOfAKind(numOfAKind, cards, pair, pair) != null){
	    return new PokerHand(HandType.TWOPAIR, getNumOfAKind(numOfAKind, cards, pair, pair));
	}else if (getNumOfAKind(numOfAKind, cards, pair) != null) {
	    return new PokerHand(HandType.PAIR, getNumOfAKind(numOfAKind, cards, pair));
	}else{
	    sortHand(cards);
	    while(cards.size() > 5){
		cards.remove(0);
	    }
	    return new PokerHand(HandType.HIGHCARD, cards);
	}
    }

    private List<List<Card>> getSameValueCards(List<Card> cards){
	List<List<Card>> sameValueCards = new ArrayList<>();
	List<Card> testHand = new ArrayList<>();
	sortHand(cards);
	for (int i = 0; i < cards.size(); i++) {
	    if (testHand.isEmpty() || cards.get(i).getValue() == cards.get(i - 1).getValue()){
		testHand.add(cards.get(i));
	    } else{
		if (testHand.size() >= 2){
		    sameValueCards.add(testHand);
		}
		testHand = new ArrayList<>();
		testHand.add(cards.get(i));
	    }
	}

	if (testHand.size() >= 2) sameValueCards.add(testHand);
	sortSameKind(sameValueCards);
	if (!sameValueCards.isEmpty()) return sameValueCards;
	return null;
    }



    private List<Card> getStraightFlush(List<Card> cards){
	List<Card> testHand = getFlush(cards);
	if (testHand != null){
	    return getStraight(testHand);
	}
	return null;
    }



    private List<Card> getFlush(List<Card> cards){
	List<Card> testHand = new ArrayList<>();
	for (int i = 0; i < 4; i++) {
	    testHand = new ArrayList<>();
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
	    return testHand;
	}
	return null;
    }
/*
    private static List<Card> getStraight(List<Card> cards){
	List<Card> testHand = new ArrayList<>();
	sortHand(cards);
	int cardsChecked = 0;
	int testIndex = 0;
	while(cardsChecked < cards.size()){
	    if(testIndex +cardsChecked + 1 < cards.size() && cards.get(testIndex+cardsChecked).getValue() == cards.get(testIndex+cardsChecked + 1).getValue() - 1){
		testHand.add(cards.get(testIndex+cardsChecked));
		testIndex++;
	    }else if(testHand.size() == 5){
		return testHand;
	    }else if(testHand.size() > 5){
		return testHand;
	    }else{
		cardsChecked++;
		testHand = new ArrayList<Card>();
		testIndex = 0;
	    }
	}
	return null;
    }
*/
    private List<Card> getStraight(List<Card> cards){
	List<Card> testHand = new ArrayList<>();
	sortHand(cards);

	for (int i = 0; i < cards.size(); i++) {
	    if (testHand.isEmpty() || cards.get(i).getValue() == cards.get(i - 1).getValue() + 1){
		testHand.add(cards.get(i));
	    } else if (cards.get(i).getValue() != cards.get(i - 1).getValue()){
		if (testHand.size() >= 5){
		    return testHand;
		} else if (i > cards.size() - 5) {
		    break;
		} else {
		    testHand = new ArrayList<>();
		    testHand.add(cards.get(i));
		}
	    }
	}
	if (testHand.size() >= 5) {
	    return testHand;
	}
	return null;
    }

    private List<Card> getNumOfAKind(List<List<Card>>  sameValueCards, List<Card> cards, int num){
	sortHand(cards);
	List<Card> testHand = new ArrayList<>();
	if (sameValueCards == null) return null;
	for (List<Card> cardList : sameValueCards) {
	    if (cardList.size() == num){
		testHand.addAll(cardList);
		for (int i = cards.size() - 1; i >= 0; i--) {
		    if (!testHand.contains(cards.get(i))){
			testHand.add(cards.get(i));
		    }
		    if (testHand.size() >= 5){
			return testHand;
		    }
		}
	    }
	}
	return null;
    }

    private List<Card> getNumOfAKind(List<List<Card>>  sameValueCards, List<Card> cards, int num1, int num2){
	sortHand(cards);
	List<Card> testHand = new ArrayList<>();
	boolean foundFirst = false;
	if (sameValueCards == null) return null;
	for (List<Card> cardList : sameValueCards) {
	    if (cardList.size() == num1 && !foundFirst){
		testHand.addAll(cardList);
		foundFirst = true;
	    }else if (foundFirst && cardList.size() >= num2){
		for (int i = 0; i < num2; i++) {
		    testHand.add(cardList.get(i));
		}
		for (int i = cards.size() - 1; i >= 0; i--) {
		    if (!(testHand.size() >= 5) && !testHand.contains(cards.get(i))){
			testHand.add(cards.get(i));
		    }
		    if (testHand.size() >= 5){
			return testHand;
		    }
		}
	    }
	}
	return null;
    }

    public void sortHand(List<Card> cards){
    	Collections.sort(cards, new CardComparator());
        }

    private void sortSameKind(List<List<Card>> sameValueCards){
	Collections.sort(sameValueCards, new SameKindComparator());
    }

    private void compareHands(){
	for (Player player : players) {
	    System.out.println(player);
	}
	System.out.println(board);
	PokerHand bestHand = new PokerHand();
	PokerHand bestHand1 = getBestHand(players[0]);
	PokerHand bestHand2 = getBestHand(players[1]);
	if(bestHand1.getHandStrength() < bestHand2.getHandStrength()){
	    bestHand = bestHand2;
	}else if(bestHand1.getHandStrength() > bestHand2.getHandStrength()) {
	    bestHand = bestHand1;
	}else {
	    for (int i = 4; i >= 0; i--) {
		if (bestHand1.getCards().get(i).getValue() < bestHand2.getCards().get(i).getValue()) {
		    bestHand = bestHand2;
		    break;
		} else if (bestHand1.getCards().get(i).getValue() > bestHand2.getCards().get(i).getValue()) {
		    bestHand = bestHand1;
		    break;
		}
	    }
	}

	System.out.println("Best hand for " + players[0].getName());
		for (Card card : bestHand1.getCards()) {
		    System.out.println(card);
		}
		System.out.println("Best hand for " + players[1].getName());
		for (Card card : bestHand2.getCards()) {
		    System.out.println(card);
		}
		System.out.println("Best hand overall: ");
		for (Card card : bestHand.getCards()) {
		    System.out.println(card);
		}
    }

    private void resetGame(){
	deck.shuffleDeck();
	board.resetBoard();
	for (Player player : players) {
	    player.resetHand();
	}
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
		dealCounter++;
		break;
	    case 4:
		compareHands();
		resetGame();
		dealCounter = 0;
	}
    }
}
