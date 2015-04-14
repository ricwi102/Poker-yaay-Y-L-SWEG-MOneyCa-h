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
        Player[] newPlayers = new Player[players.length];
        newPlayers[players.length - 1] = players[0];
        for(int i = 0; i < players.length - 1; i++){
            newPlayers[i] = players[i + 1];
        }
        players = newPlayers;
        if(players.length == 2){
            players[0].setPosition(PlayerPosition.DEALER);
            players[1].setPosition(PlayerPosition.SMALLBLIND);
            currentPlayer = this.players[1];
        }else{
            players[0].setPosition(PlayerPosition.SMALLBLIND);
            players[1].setPosition(PlayerPosition.BIGBLIND);
            for(int i = 2; i < players.length; i++){
                if (i == players.length - 1) players[i].setPosition(PlayerPosition.DEALER);
                else players[i].setPosition(PlayerPosition.STANDARD);
            }
            currentPlayer = this.players[2];
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

