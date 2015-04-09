package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Hold'em rules
 */

public class Holdem extends PokerBase
{
	/*private Player[] players;
    private Board board;
    private Deck deck;*/
	private int dealCounter;
    /*private Player currentPlayer;
    private int pot;*/

	public Holdem(final Player[] players, final Board board) {
		super(players, board);
		dealCounter = 1;
		dealCards();
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

	private void compareHands(){
		List<PokerHand> bestHands = new ArrayList<PokerHand>();
		for (Player player : players) {
			if(player.isActive()) {
				System.out.println(player);
				bestHands.add(PokerHandCalc.getBestHand(player,board));
			}
		}
		System.out.println(board);

		Collections.sort(bestHands,new HandComparator());
		PokerHand bestHand = bestHands.get(0);

		for (PokerHand hand : bestHands) {
			System.out.println("Best hand for " + hand.getPlayer().getName());
			for(Card card : hand.getCards()){
				System.out.println(card);
			}
		}

		System.out.println("Best hand overall: ");
		for (Card card : bestHand.getCards()) {
			System.out.println(card);
		}
		System.out.println("Winner: " + bestHand.getPlayer().getName());
		awardWinner(bestHand.getPlayer());

	}

/*	private void compareHands(){
		for (Player player : players) {
			System.out.println(player);
		}
		System.out.println(board);
		PokerHand bestHand = new PokerHand();
		PokerHand bestHand1 = PokerHandCalc.getBestHand(players[0], board);
		PokerHand bestHand2 = PokerHandCalc.getBestHand(players[1], board);
		if(bestHand1.getHandStrength() < bestHand2.getHandStrength()){
			bestHand = bestHand2;
			awardWinner(players[1]);
		}else if(bestHand1.getHandStrength() > bestHand2.getHandStrength()) {
			bestHand = bestHand1;
			awardWinner(players[0]);
		}else {
			for (int i = 4; i >= 0; i--) {
				if (bestHand1.getCards().get(i).getValue() < bestHand2.getCards().get(i).getValue()) {
					bestHand = bestHand2;
					awardWinner(players[1]);
					break;
				} else if (bestHand1.getCards().get(i).getValue() > bestHand2.getCards().get(i).getValue()) {
					bestHand = bestHand1;
					awardWinner(players[0]);
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
	}*/

	public Player checkForWinner(){
		int activePlayers = 0;
		Player winningPlayer = null;
		for (Player player : players) {
			if(player.isActive()) {
				activePlayers++;
				winningPlayer = player;
			}
			if(activePlayers > 1) break;
		}
		if(activePlayers == 1){
			System.out.println("Winner: " + winningPlayer.getName());
			resetGame();
			for (Player player : players) {
				player.activate();
			}
			dealCounter = 1;
			currentPlayer = players[0];
			awardWinner(winningPlayer);
			dealCards();
			return winningPlayer;
		}
		return null;
	}


	//Working on implementing the betting in the game flow. not quite working as intended yet
	public void nextPlayer(){
		int currentPlayerPos = Arrays.asList(players).indexOf(currentPlayer);
		boolean foundPlayer = false;
		if (currentPlayerPos < players.length - 1) {
			for (int i = currentPlayerPos + 1; i < players.length; i++) {
				if (bettingRules.someoneRaised() && players[i].isActive() && !players[i].hasRaised() && !players[i].hasCalled()){
					currentPlayer = players[i];
					foundPlayer = true;
				}else if(!bettingRules.someoneRaised() && players[i].isActive()){
					currentPlayer = players[i];
					foundPlayer = true;
				}
				if(foundPlayer) break;
			}
			if (!foundPlayer) {
				if(!bettingRules.hasUnresolvedRaise(players)) nextStreet();
				for (Player player : players) {
					if (bettingRules.someoneRaised() && player.isActive() && !player.hasRaised() && !player.hasCalled()) {
						currentPlayer = player;
						break;
					}else if(!bettingRules.someoneRaised() && player.isActive()){
						currentPlayer = player;
						break;
					}
				}

			}
		} else if (currentPlayerPos == players.length - 1) {
			if(!bettingRules.hasUnresolvedRaise(players)) nextStreet();
			for (Player player : players) {
				if (bettingRules.someoneRaised() && player.isActive() && !player.hasRaised() && !player.hasCalled()) {
					currentPlayer = player;
					break;
				}else if(!bettingRules.someoneRaised() && player.isActive()){
					currentPlayer = player;
					break;
				}
			}
		}
		System.out.println("Current Player: " + currentPlayer.getName());
	}

	public void addRaiseToPot(int chips){
		if(bettingRules.isLegalRaise(chips)) {
			pot += chips;
			bettingRules.setLatestBet(chips);
			bettingRules.setRaised(true);
			for (Player player : players) {
				if(player.isActive() && !player.equals(currentPlayer)){
					player.setCalled(false);
					player.setRaised(false);
				}
			}
		}
	}

	public void addCallToPot(int chips){
		pot += chips;
	}

	private void resetPlayerStatus(){
		for (Player player : players) {
			player.newRound();
		}
		bettingRules.setRaised(false);
	}

	public void nextStreet(){
		switch(dealCounter){
			case 0:
				dealCards();
				System.out.println(board);
				dealCounter++;
				break;
			case 1:
				dealFlop();
				System.out.println(board);
				dealCounter++;
				break;
			case 2:
				dealTurn();
				System.out.println(board);
				dealCounter++;
				break;
			case 3:
				dealRiver();
				System.out.println(board);
				dealCounter++;
				break;
			case 4:
				compareHands();
				resetGame();
				for (Player player : players) {
					player.activate();
				}
				dealCounter = 1;
				dealCards();
		}
		resetPlayerStatus();
		bettingRules.setLatestBet(0);
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}




}
