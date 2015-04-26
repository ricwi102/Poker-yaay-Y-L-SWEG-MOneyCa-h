package poker;

import java.util.ArrayList;
import java.util.List;

/**
 * Poker base that include the shared rules of all pokergames available with this program
 */

public class PokerBase
{
    protected List<Player> players;
    protected Board board;
    protected Deck deck;
    protected Player currentPlayer;
    protected Player latestBettingPlayer;
    protected int dealCounter;
    protected int pot;
    protected int smallBlind;
    protected int bigBlind;
    protected BettingRules bettingRules;


    protected PokerBase(final Board board, final BettingRules bettingRules, final List<Player> players) {
        this.board = board;
        this.bettingRules = bettingRules;
        this.players = players;
        smallBlind = 10;
        bigBlind = 20;
        pot = 0;
        dealCounter = 1;
        deck = new Deck();

    }

    protected void startGame(){
        setTablePositions();
        updatePlayerPositions();
    }

    private void setTablePositions(){
        int currentPosition = 0;
        for (Player player : players) {
            player.setTablePosition(currentPosition);
            currentPosition++;
        }
    }

    protected void resetGame(){
    	deck.shuffleDeck();
    	board.resetBoard();
        dealCounter = 1;
    	for (Player player : players) {
    	    player.resetHand();
            player.activate();
            player.newRound();
    	}
    }

    protected void resetPlayerBets(){
        for (Player player : players) {
            player.newRound();
        }
    }


    protected void updatePlayerPositions(){
        List<Player> losers = new ArrayList<>();
        for (Player player : players) {
            if(player.getChips() <= 0) losers.add(player);
        }

        players.removeAll(losers);

        System.out.println(players);
        players.add(players.remove(0));
        System.out.println(players);

        if(players.size() == 1){
            gameOver();
        }else if(players.size() == 2){
            players.get(0).setPosition(PlayerPosition.BIGBLIND);
            players.get(1).setPosition(PlayerPosition.SMALLBLIND);
            currentPlayer = this.players.get(1);
        }else{
            players.get(0).setPosition(PlayerPosition.SMALLBLIND);
            players.get(1).setPosition(PlayerPosition.BIGBLIND);
            for(int i = 2; i < players.size(); i++){
                if (i == players.size() - 1) players.get(i).setPosition(PlayerPosition.DEALER);
                else players.get(i).setPosition(PlayerPosition.STANDARD);
            }
            currentPlayer = this.players.get(2);
        }
        latestBettingPlayer = currentPlayer;
    }

    public void gameOver(){
        System.out.println("Winner: " + players.get(0).getName() + ",  Yaaaaaaaaaay!! you won the whole game!! you are absolutely the best player");
        System.exit(0);
    }

    public void awardWinner(Player winner){
        winner.addChips(pot);
        pot = 0;
    }

    public boolean raise(int chips) {
        if (bettingRules.isLegalRaise(chips)) {
            currentPlayer.bet(chips);
            addToPot(chips);
            latestBettingPlayer = currentPlayer;
            bettingRules.setLatestBet(currentPlayer.getActiveBet());
            return true;
        }
        return false;
    }

    public boolean call(){
        if (bettingRules.isLegalCall(currentPlayer)) {
            int chips = currentPlayer.call(bettingRules.getLatestBet());
            addToPot(chips);
            return true;
        }
        return false;
    }

    public boolean check(){
        if(bettingRules.isLegalCheck(currentPlayer)){
            currentPlayer.check();
            return true;
        }
        return false;
    }

    public void fold(){
        currentPlayer.fold();
    }

    private void addToPot(int chips) {
    	pot += chips;
        }

    public int getPot(){
        return pot;
    }

    public Board getBoard() {
        return board;
    }

    public BettingRules getBettingRules() {
        return bettingRules;
    }

    public List<Player> getPlayers() { return players; }

    public Player getCurrentPlayer() {
   	return currentPlayer;
       }
}

