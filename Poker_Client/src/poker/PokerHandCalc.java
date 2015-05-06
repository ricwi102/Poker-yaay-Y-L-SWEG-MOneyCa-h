package poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class contains all methods used to calculate a players best possible hand given their hand and
 * the community cards.
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public final class PokerHandCalc
{
    private PokerHandCalc() {}

    /*
    This method is used to calculate the best hand in a game of Omaha. In Omaha you have 4 cards in hand and 5
    community cards. You have to use EXACTLY 2 cards from your hand and 3 cards from the community cards to form
    your poker hand
     */
    public static PokerHand getBestOmahaHand(Player player, Board board){
	assert player.getHand().size() == 4 : "Omaha player does not have 4 cards";
	List<PokerHand> possibleBestHands = new ArrayList<>();
	List<List<Card>> setsOfPlayerCards = getSetsOfCards(player.getHand(),2);
	List<List<Card>> setsOfOpenCards = getSetsOfCards(board.getOpenCards(),3);
	List<Card> cards = new ArrayList<>();
	for(List<Card> currentSetOfPlayerCards : setsOfPlayerCards){
	    for(List<Card> currentSetOfOpenCards : setsOfOpenCards){
		cards.clear();
		cards.addAll(currentSetOfPlayerCards);
		cards.addAll(currentSetOfOpenCards);
		possibleBestHands.add(getBestHand(cards,player));
	    }
	}
	Collections.sort(possibleBestHands, new HandComparator());
	return possibleBestHands.get(0);
    }

    private static List<List<Card>> getSetsOfCards(Iterable<Card> cards, int size){
	List<List<Card>> powersets = new ArrayList<>();
	powersets.add(new ArrayList<>());

	for(Card card : cards){
	    List<List<Card>> newPowersets = new ArrayList<>();

	    for(List<Card> subset: powersets){
		newPowersets.add(subset);

		List<Card> newSubset = new ArrayList<>(subset);
		newSubset.add(card);
		newPowersets.add(newSubset);
	    }
	    powersets = newPowersets;
	}
	Collection<List<Card>> wrongSizeSets = new ArrayList<>();
	for(List<Card> set : powersets){
	    if(set.size() != size){
		wrongSizeSets.add(set);
	    }
	}
	powersets.removeAll(wrongSizeSets);
	return powersets;
    }

    /*
    This method is used to calculate the best hand in a game of Hold'em. In Hold'em you have 2 cards in hand and 5
     community cards. You may ues any combination of 5 between these cards to form you poker hand
     */
    public static PokerHand getBestHoldemHand(Player player, Board board){
	assert player.getHand().size() == 2 : "Holdem player does not have 2 cards";
	List<Card> cards = new ArrayList<>();
	cards.addAll(board.getOpenCards());
	cards.addAll(player.getHand());
	return getBestHand(cards,player);
    }

    private static PokerHand getBestHand(List<Card> cards, Player player){
	final int fourOfAKind = 4;
	final int threeOfAKind = 3;
	final int pair = 2;
	List<List<Card>> numOfAKind = getSameValueCards(cards);
	if(getStraightFlush(cards) != null){
	    return new PokerHand(HandType.STRAIGHTFLUSH, getStraightFlush(cards),player);
	}else if(getNumOfAKind(numOfAKind, cards, fourOfAKind) != null) {
	    return new PokerHand(HandType.FOUROFAKIND, getNumOfAKind(numOfAKind, cards, fourOfAKind),player);
	}else if(getNumOfAKind(numOfAKind, cards, threeOfAKind, pair) != null) {
	    return new PokerHand(HandType.FULLHOUSE, getNumOfAKind(numOfAKind, cards, threeOfAKind, pair),player);
	} else if(getFlush(cards) != null){
	    return new PokerHand(HandType.FLUSH, getBestFive(getFlush(cards)),player);
	}else if(getStraight(cards) != null){
	    return new PokerHand(HandType.STRAIGHT, getStraight(cards),player);
	}else if(getNumOfAKind(numOfAKind, cards, threeOfAKind) != null) {
	    return new PokerHand(HandType.THREEOFAKIND, getNumOfAKind(numOfAKind, cards, threeOfAKind),player);
	}else if(getNumOfAKind(numOfAKind, cards, pair, pair) != null){
	    return new PokerHand(HandType.TWOPAIR, getNumOfAKind(numOfAKind, cards, pair, pair),player);
	}else if (getNumOfAKind(numOfAKind, cards, pair) != null) {
	    return new PokerHand(HandType.PAIR, getNumOfAKind(numOfAKind, cards, pair),player);
	}else{
	    sortHand(cards);
	    while(cards.size() > 5){
		cards.remove(0);
	    }
	    Collections.reverse(cards);
	    return new PokerHand(HandType.HIGHCARD, cards,player);
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


    private static List<Card> getStraightFlush(Iterable<Card> cards){
	List<Card> testHand = getFlush(cards);
	if (testHand != null){
	    return getStraight(testHand);
	}
	return null;
    }

    private static List<Card> getFlush(Iterable<Card> cards){
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
	    if(testHand.size() == 5) break;
	}
	if (testHand.size() >= 5) {
	    return testHand;
	}
	return null;
    }

    private static List<Card> getNumOfAKind(Iterable<List<Card>> sameValueCards, List<Card> cards, int num){
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

    private static List<Card> getNumOfAKind(Iterable<List<Card>> sameValueCards, List<Card> cards, int num1, int num2){
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

    public static void sortHand(List<Card> cards){
	Collections.sort(cards, new CardComparator());
    }

    private static void sortSameKind(List<List<Card>> sameValueCards){ Collections.sort(sameValueCards, new SameKindComparator()); }

    private static List<Card> getBestFive(List<Card> cards){
	while (cards.size() > 5){
	    cards.remove(0);
	}
	return cards;
    }
}
