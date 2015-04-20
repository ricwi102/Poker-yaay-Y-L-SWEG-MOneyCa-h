package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hold'em rules
 */

public class Holdem extends PokerBase{

    public Holdem(final List<Player> players, final Board board) {
	super(players, board);
	bettingRules.setLatestBet(bigBlind);

	dealCards();
    }

    public static void main(String[] args) {}

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
	    }else if(player.getPosition() == PlayerPosition.SMALLBLIND){
		player.bet(smallBlind);
		pot += smallBlind;
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
	List<PokerHand> bestHands = new ArrayList<>();
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

    public void newRound() {
	resetGame();
	updatePlayerPositions();
	dealCards();
    }

    private void checkForAction(){


    }


    private void newStreet() {
	resetPlayerBets();


	Player nextPlayer = nextActivePlayerWithChips(currentPlayer);
	if (nextPlayer.equals( nextActivePlayerWithChips(nextPlayer))) {
	    nextStreet();
	} else {
	    currentPlayer = getFirstPlayer();
	    latestBettingPlayer = currentPlayer;
	    if (!(currentPlayer.getChips() > 0)) advanceGame();
	    else{
		System.out.println("Current Player: " + currentPlayer.getName());
		checkForAction();
	    }
	}
    }


    public void advanceGame(){

	Player nextPlayer = nextActivePlayer(currentPlayer);

	if (nextPlayer.equals(nextActivePlayer(nextPlayer))) {
	    awardWinner(nextPlayer);
	    newRound();
	}else if (nextPlayer.equals(latestBettingPlayer)) {
	    nextStreet();
	}else {
	    currentPlayer = nextPlayer;
	    if (currentPlayer.getChips() > 0) {
		System.out.println("Current Player: " + currentPlayer.getName());

 		checkForAction();
	    }else {
		advanceGame();
	    }
	}
    }

    private Player getFirstPlayer() {
	return nextActivePlayer(players.get(players.size() - 1));
    }

    private Player nextActivePlayer(Player player){
	int startIndex = players.indexOf(player);
	int playersSize = players.size();

	for (int i = startIndex + 1; i < startIndex + 1 + playersSize; i++) {
	    if (players.get(i%playersSize).isActive()){
		return players.get(i%playersSize);
	    }
	}
	return player;
    }

    private Player nextActivePlayerWithChips(Player player){
    	int startIndex = players.indexOf(player);
    	int playersSize = players.size();

    	for (int i = startIndex + 1; i < startIndex + 1 + playersSize; i++) {
    	    if (players.get(i%playersSize).isActive() && players.get(i%playersSize).getChips() > 0){
    		return players.get(i%playersSize);
    	    }
    	}
    	return player;
        }


    public void raise(int chips) {
	addToPot(chips);
	latestBettingPlayer = currentPlayer;
	bettingRules.setLatestBet(currentPlayer.getActiveBet());
    }

    public void addToPot(int chips) {
	pot += chips;
    }

    public void nextStreet(){
	bettingRules.setLatestBet(0);
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
		newRound();
	}
	if (dealCounter != 1) newStreet();

    }

    public Player getCurrentPlayer() {
	return currentPlayer;
    }




}
