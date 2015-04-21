package poker_client;

import java.util.ArrayList;
import java.util.List;

public class GameInfo{
    private List<Player> players;
    private Board board;
    private Player currentPlayer;
    private Player you;
    private int pot;
    private BettingRules bettingRules;


    public GameInfo() {
	board = new Board();
	players = new ArrayList<>();
	bettingRules = new BettingRules();
    }

    public void addPlayer(String name, int chips){
	if (getPlayerFromName(name) == null) {
	    players.add(new Player(name, chips));
	}
    }

    public void addYou(String name, int chips){
	Player player = getPlayerFromName(name);
	if (player == null) {
	    you = new Player(name, chips);
	    players.add(you);
	} else {
	    you = player;
	}

    }

    public void addCardToBoard(Card card){
	board.addCard(card);
    }



    public Player getPlayerFromName(String name){
	for (Player player : players) {
	    if (player.getName().equals(name)) return player;
	}
	return null;
    }

    public PlayerPosition getPositionFromString(String pos){
	switch (pos){
	    case "DEALER":
		return PlayerPosition.DEALER;
	    case "SMALLBLIND":
		return PlayerPosition.SMALLBLIND;
	    case "BIGBLIND":
		return PlayerPosition.BIGBLIND;
	    case "STANDARD":
		return PlayerPosition.STANDARD;
	}
	return null;
    }

    public Card getCardFromString(String color, int value){
	switch (color){
	    case "HEARTS":
		return new Card(value, CardColor.HEARTS);
	    case "SPADES":
	    	return new Card(value, CardColor.SPADES);
	    case "DIAMONDS":
	    	return new Card(value, CardColor.DIAMONDS);
	    case "CLUBS":
	    	return new Card(value, CardColor.CLUBS);
	}
	return null;
    }

    public boolean getStatusFromText(String status){
	if (status.equals("FALSE")){
	    return false;
	}
	return true;
    }

    public void updatePlayerChips(Player player, int chips){
	if (player != null && chips > 0) player.setChips(chips);
    }

    public void updatePlayerActiveBet(Player player, int bet){
    	if (player != null && bet > 0) player.setActiveBet(bet);
    }

    public void updatePlayerPosition(Player player, PlayerPosition pos){
	if (player != null && pos != null) player.setPosition(pos);
    }

    public void updateCurrentPlayer(Player player){
	if (player != null) currentPlayer = player;
    }

    public void updatePlayerHand(Player player, List<Card> hand) {
	if (player != null) player.setHand(hand);
    }

    public void updatePlayerStatus(Player player, boolean status) {
    	if (player != null) player.setStatus(status);
    }


    public List<Player> getPlayers() {
	return players;
    }

    public Board getBoard() {
	return board;
    }

    public Player getCurrentPlayer() {
	return currentPlayer;
    }

    public Player getYou() {
	return you;
    }

    public int getPot() {
	return pot;
    }

    public BettingRules getBettingRules() {
	return bettingRules;
    }
}
