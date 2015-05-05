package poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class handles everything regarding the game flow. It moves the game forward in appropriate ways depending
 * on the game type, like updating player positions, deciding who is the winner of each round,
 * eliminating players when appropriate, dealing cards and so on.
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */
public class PokerBase
{
    private List<Player> players;
    private List<Player> lostPlayers = new ArrayList<>();
    private Board board;
    private Deck deck;
    private Player currentPlayer = null;
    private Player latestBettingPlayer = null;
    private int dealCounter;
    private final static int SMALL_BLIND = 10;
    private final static int BIG_BLIND = 20;
    private BettingRules bettingRules;
    private GameType gameType;
    private PokerFrame frame = null;
    private PokerAi ai;
    private boolean isGameOver = false;
    private boolean multiplayer = false;


    public PokerBase(final List<Player> players, final Board board, final BettingRules bettingRules, final GameType gameType) {
        this.players = players;
        this.board = board;
        this.gameType = gameType;
        this.bettingRules = bettingRules;

        dealCounter = 1;
        bettingRules.setMinimumBet(BIG_BLIND);
        deck = new Deck();
        ai = new PokerAi(this);
    }

    public void startSingleplayer(){
        setTablePositions();
        updatePlayerPositions();
        payBlinds();
        dealCards();
        checkForAction();
    }

    public void startMultiplayer(){
        multiplayer = true;
    }

    //This is used to decide where a players name and cards should appear on the playing board.
    private void setTablePositions(){
        int currentPosition = 0;
        for (Player player : players) {
            player.setTablePosition(currentPosition);
            currentPosition++;
        }
    }

    private void dealCards(){
        int numberOfCards;
        if(gameType == GameType.OMAHA){
            numberOfCards = 4;
        }else{
            numberOfCards = 2;
        }
        for(int i = 0; i < numberOfCards; i++) {
            for (Player player : players) {
                player.addCard(deck.drawCard());}
        }
    }


    private void payBlinds(){
        for (Player player : players) {
            if(player.getPosition() == PlayerPosition.BIGBLIND){
                if(player.getChips() > BIG_BLIND) {
                    player.bet(BIG_BLIND);
                    addToPot(BIG_BLIND);
                }else{
                    player.bet(player.getChips());
                    addToPot(player.getChips());
                }
                bettingRules.setLatestBet(BIG_BLIND);
                bettingRules.setRaised(true);
            }else if(player.getPosition() == PlayerPosition.SMALLBLIND){
                if(player.getChips() > SMALL_BLIND) {
                    player.bet(SMALL_BLIND);
                    addToPot(SMALL_BLIND);
                }else{
                    player.bet(player.getChips());
                    addToPot(player.getChips());
                }
            }
        }
    }

    private void dealFlop(){
        for (int i = 0; i < 3; i++) {
            board.addCard(deck.drawCard());
        }
    }

    private void dealOneCard(){
        board.addCard(deck.drawCard());
    }

    //------- Functions For Awarding Winners --------

        private void calculateBestHands(){
            players.forEach(player -> {
                if (player.getHand().size() == 4) {
                    player.setBestHand(PokerHandCalc.getBestOmahaHand(player, board));
                } else {
                    player.setBestHand(PokerHandCalc.getBestHoldemHand(player, board));
                }
            });
        }

        private void resolveWinners(){
            calculateBestHands();
            awardWinners(calculatePots());
        }

        private Iterable<Pot> calculatePots(){
            List<Player> sortedByLowestBet = new ArrayList<>();
            players.forEach(sortedByLowestBet::add);

            Collections.sort(sortedByLowestBet, new TotalBetComparator());

            Collection<Pot> pots = new ArrayList<>();

            for (Player player : sortedByLowestBet) {
                int chips = player.getTotalBetThisRound();
                for (Pot pot : pots){
                    if (chips > 0){
                        chips = pot.addToPlayerBet(chips, player);
                    }
                }
                if (chips > 0){
                    Pot pot = new Pot();
                    pots.add(pot);
                    pot.addToPlayerBet(chips, player);
                }
            }
            return pots;
        }

        private void awardWinners(Iterable<Pot> pots){
            pots.forEach(Pot::resolveWinners);
        }

    //-------------------------

    public void newRound() {
        resetGame();
        updatePlayerPositions();
        if(!isGameOver) {
            bettingRules.resetPot();
            dealCards();
            payBlinds();
            if (!checkCurrentPlayerForChips()){
                advanceGame();
            } else {
                checkForAction();
            }
        }
    }

    //This is used to check if the current player is an AI and let the AI decide what to do if that is the case
    public void checkForAction(){
        frame.updateUi();
        if(currentPlayer.getController().equals("ai")){
            String decision = ai.decide(currentPlayer);
            switch(decision) {
                case "check":
                    advanceGame();
                    break;
                case "call":
                    int amount = currentPlayer.call(bettingRules.getLatestBet());
                    addToPot(amount);
                    advanceGame();
                    break;
                default:
                    advanceGame();
            }
        }
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
                checkForAction();
            }
        }
    }

    /* This is used to decide who is next to act and to check if it´s time to move on to the next street
    (pre-flop, flop, turn and river in poker terms) */
    public void advanceGame(){
        Player nextPlayer = nextActivePlayer(currentPlayer);

        if (nextPlayer.equals(nextActivePlayer(nextPlayer))) {
            resolveWinners();
            newRound();
        }else if (nextPlayer.equals(latestBettingPlayer)) {
            nextStreet();
        }else {
            if(!latestBettingPlayer.isActive()){ latestBettingPlayer = nextActivePlayer(latestBettingPlayer); }
            currentPlayer = nextPlayer;
            if (currentPlayer.getChips() > 0) {
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
        bettingRules.setRaised(true);
        bettingRules.setLatestBet(currentPlayer.getActiveBet());
    }



    public void addToPot(int chips) {
        bettingRules.addToPot(chips);
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
                dealOneCard();
                System.out.println(board);
                dealCounter++;
                break;
            case 3:
                dealOneCard();
                System.out.println(board);
                dealCounter++;
                break;
            case 4:
                resolveWinners();
                newRound();
        }
        if (dealCounter != 1) newStreet();

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void resetGame(){
        deck.shuffleDeck();
        board.resetBoard();
        dealCounter = 1;
        for (Player player : players) {
            player.resetHand();
            player.setActive(true);
            player.newRound();
        }
    }

    public void resetPlayerBets(){
        for (Player player : players) {
            player.resetActiveBet();
        }
        bettingRules.setRaised(false);
    }

    // This removes players with no chips, moves around the blinds and dealer positions
    private void updatePlayerPositions(){
        Collection<Player> losers = new ArrayList<>();
        for (Player player : players) {
            if(player.getChips() <= 0) losers.add(player);
        }

        lostPlayers.addAll(losers);
        players.removeAll(losers);
        System.out.println(players);
        players.add(players.remove(0));
        System.out.println(players);

        if(players.size() == 1 || !hasNonAiPlayers()){
            isGameOver = true;
            lostPlayers.addAll(players);
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

    private boolean hasNonAiPlayers(){
        for (Player player : players) {
            if(player.getController().equals("player")){
                return true;
            }
        }
        return false;
    }

    public void gameOver(){
        System.out.println("Winner: " + players.get(0).getName() + ",  Yaaaaaaaaaay!! you won the whole game!! you are absolutely the best player");
        Collections.reverse(lostPlayers);
        GameOverFrame newFrame = new GameOverFrame(lostPlayers);
        newFrame.pack();
        newFrame.setVisible(true);
        frame.setVisible(false);
        frame.dispose();
    }

    private boolean checkCurrentPlayerForChips(){
        return currentPlayer.getChips() > 0;
    }

    public void addPlayerToLosers(Player player){ lostPlayers.add(player); }

    public Board getBoard() {
        return board;
    }

    public BettingRules getBettingRules() {
        return bettingRules;
    }

    public List<Player> getPlayers() { return players; }

    public PokerFrame getFrame() { return frame; }

    public GameType getGameType() { return gameType; }

    public int getPot() { return bettingRules.getPot();}

    public boolean isMultiplayer() { return multiplayer; }

    public void setFrame(final PokerFrame frame) { this.frame = frame; }

    public void setCurrentPlayer(final Player currentPlayer) { this.currentPlayer = currentPlayer; }

    public void setPot(int amount) { bettingRules.setPot(amount); }
}

