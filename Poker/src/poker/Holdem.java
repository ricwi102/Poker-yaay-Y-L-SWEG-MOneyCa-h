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
		bettingRules.setLatestBet(bigBlind);

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
		payBlinds();
	}



	private void payBlinds(){
		for (Player player : players) {
			if(player.getPosition() == PlayerPosition.BIGBLIND){
				player.bet(bigBlind);
				pot += bigBlind;
				bettingRules.setLatestBet(bigBlind);
				bettingRules.setRaised(true);
			    	player.setRaised(false);
			    	player.setCalled(false);
			}else if(player.getPosition() == PlayerPosition.SMALLBLIND){
				player.bet(smallBlind);
				pot += smallBlind;
				player.setCalled(false);
				player.setRaised(false);
			}else{
				player.setCalled(false);
				player.setRaised(false);
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


	public void nextPlayer() {
	    int currentPlayerPos = Arrays.asList(players).indexOf(currentPlayer);
	    boolean foundPlayer = false;
	    if (currentPlayerPos < players.length - 1) {
		for (int i = currentPlayerPos + 1; i < players.length; i++) {
		    if (bettingRules.someoneRaised() && players[i].isActive() && !players[i].hasRaised() &&
			!players[i].hasCalled()) {
			currentPlayer = players[i];
			foundPlayer = true;
		    } else if (!bettingRules.someoneRaised() && players[i].isActive()) {
			currentPlayer = players[i];
			foundPlayer = true;
		    }
		    if (foundPlayer) break;
		}
		if (!foundPlayer) {
		    nextPlayerHelp();
		}
	    } else if (currentPlayerPos == players.length - 1) {
		nextPlayerHelp();
	    }
	    System.out.println("Current Player: " + currentPlayer.getName());

	}

    private void nextPlayerHelp() {
	boolean didNextStreet = false;
	if (!bettingRules.hasUnresolvedRaise(players)){
	    nextStreet();
	    didNextStreet = true;
	}
	for (Player player : players) {
	    if (bettingRules.someoneRaised() && player.isActive() && !player.hasRaised() && !player.hasCalled()) {
		currentPlayer = player;
		break;
	    } else if (!bettingRules.someoneRaised() && player.isActive()) {
		currentPlayer = player;
		break;
	    }
	}
	if(dealCounter == 1 && didNextStreet) updatePlayerPositions();
    }
    public void addRaiseToPot(int chips){
	pot += chips;
	bettingRules.setLatestBet(currentPlayer.getActiveBet());
	bettingRules.setRaised(true);
	for (Player player : players) {
	    if(player.isActive() && !player.equals(currentPlayer)){
		player.setCalled(false);
		player.setRaised(false);
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
		}
		resetPlayerStatus();
		bettingRules.setLatestBet(0);
		if(dealCounter == 1){
			dealCards();
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}




}
