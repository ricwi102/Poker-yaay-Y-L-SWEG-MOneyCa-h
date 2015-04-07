package poker;

import java.util.Arrays;

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
		dealCounter = 0;
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
	}

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
			dealCounter = 0;
			currentPlayer = players[0];
			awardWinner(winningPlayer);
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
				if (players[i].isActive() && !players[i].hasRaised()){
					currentPlayer = players[i];
					foundPlayer = true;
				}
			}
			if (!foundPlayer) {
				if(!bettingRules.hasUnresolvedRaise(players)) nextStreet();
				for (Player player : players) {
					if (player.isActive() && !player.hasRaised()) {
						currentPlayer = player;
						break;
					}
				}

			}
		} else if (currentPlayerPos == players.length - 1) {
			if(!bettingRules.hasUnresolvedRaise(players)) nextStreet();
			for (Player player : players) {
				if (player.isActive() && !player.hasRaised()) {
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
				dealCounter = 0;
		}
		resetPlayerStatus();
		bettingRules.setLatestBet(0);
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}




}
