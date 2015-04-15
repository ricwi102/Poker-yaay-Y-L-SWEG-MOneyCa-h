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
    protected int pot;
    protected int smallBlind;
    protected int bigBlind;
    protected BettingRules bettingRules;

    protected PokerBase(final List<Player> players, final Board board) {
        this.players = players;
        this.board = board;
        bettingRules = new BettingRules();
        updatePlayerPositions();
        smallBlind = 10;
        bigBlind = 20;
        pot = 0;
        deck = new Deck();
    }

    protected void resetGame(){
    	deck.shuffleDeck();
    	board.resetBoard();
    	for (Player player : players) {
    	    player.resetHand();
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
    }

    public void gameOver(){
        System.out.println("Winner: " + players.get(0).getName() + ",  Yaaaaaaaaaay!! you won the whole game!! you are absolutely the best player");
        System.exit(0);
    }

    public void awardWinner(Player winner){
        winner.addChips(pot);
        pot = 0;
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
}

