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
	List<Card> testList = new ArrayList<>();
	testList.add(new Card(11, CardColor.HEARTS));
	testList.add(new Card(6, CardColor.HEARTS));
	testList.add(new Card(6, CardColor.DIAMONDS));
	testList.add(new Card(6, CardColor.HEARTS));
	testList.add(new Card(6, CardColor.HEARTS));
	testList.add(new Card(12, CardColor.HEARTS));

	List<List<Card>> test = getSameValueCards(testList);

	testList = getFourOfAKind(test, testList);

	for (Card card : testList) {
	    System.out.println(card);
	}

	/*
	for (List<Card> cards : test) {
	    for (Card card: cards) {
		System.out.println(card);
	    }
	}*/
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
	List<Card> cards = new ArrayList<>();
	cards.addAll(board.getOpenCards());
	cards.addAll(player.getHand());
	if(getFlush(cards) != null){
	    return getFlush(cards);
	}else if(getStraight(cards) != null){
	    return getStraight(cards);
	}else{
	    sortHand(cards);
	    while(cards.size() > 5){
		cards.remove(0);
	    }
	    return cards;
	}
    }

    private static List<List<Card>> getSameValueCards(List<Card> cards){
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



    private static List<Card> getStraightFlush(List<Card> cards){
	List<Card> testHand = getFlush(cards);
	if (testHand != null){
	    return getStraight(testHand);
	}
	return null;
    }



    private static List<Card> getFlush(List<Card> cards){
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
    private static List<Card> getStraight(List<Card> cards){
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

    private static List<Card> getFourOfAKind(List<List<Card>>  sameValueCards, List<Card> cards){
	sortHand(cards);
	List<Card> testHand = new ArrayList<>();
	for (List<Card> cardList : sameValueCards) {
	    if (cardList.size() == 4){
		testHand.addAll(cardList);
		for (int i = cards.size() - 1; i >= 0; i--) {
		    if (!testHand.contains(cards.get(i))){
			testHand.add(cards.get(i));
			return testHand;
		    }
		}
	    }
	}



	return null;
    }

    public static void sortHand(List<Card> cards){
    	Collections.sort(cards, new CardComparator());
        }

    private static void sortSameKind(List<List<Card>> sameValueCards){
	Collections.sort(sameValueCards, new SameKindComparator());
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
		for (Player player : players) {
		    System.out.println(player);
		}
		System.out.println(board);
		List<Card> bestHand = getBestHand(players[0]);
		System.out.println("Best hand for " + players[0].getName());
		for (Card card : bestHand) {
		    System.out.println(card);
		}
		dealCounter = 0;
	}

    }
}
