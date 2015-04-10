package poker;

/**
 * Poker base that include the shared rules of all pokergames available with this program
 */

public class PokerBase
{
    protected Player[] players;
    protected Board board;
    protected Deck deck;
    protected Player currentPlayer;
    protected int pot;
    protected int smallBlind;
    protected int bigBlind;
    protected BettingRules bettingRules;

    protected PokerBase(final Player[] players, final Board board) {
        this.players = players;
        this.board = board;
        bettingRules = new BettingRules();
        currentPlayer = players[0];
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

