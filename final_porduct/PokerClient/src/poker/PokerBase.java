package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Poker base that include the shared rules of all pokergames available with this program
 */

public class PokerBase
{
    protected List<Player> players;
    protected List<Player> lostPlayers;
    protected Board board;
    protected Deck deck;
    protected Player currentPlayer;
    protected Player latestBettingPlayer;
    protected int dealCounter;
    protected int pot;
    protected int smallBlind;
    protected int bigBlind;
    protected BettingRules bettingRules;
    protected PokerFrame frame;
    protected Ai ai;
    protected boolean isGameOver;
    protected boolean multiplayer;


    protected PokerBase(final List<Player> players, final Board board, final BettingRules bettingRules) {
        this.players = players;
        this.board = board;
        this.bettingRules = bettingRules;
        lostPlayers = new ArrayList<>();
        isGameOver = false;
        multiplayer = false;
        smallBlind = 10;
        bigBlind = 20;
        pot = 0;
        dealCounter = 1;
        bettingRules.setMinimumBet(bigBlind);
        deck = new Deck();
        ai = new Ai(this);
    }

    public void startSingleplayer(){
        setTablePositions();
        updatePlayerPositions();
        payBlinds();
    }

    public void startMultiplayer(){
        multiplayer = true;
    }

    private void setTablePositions(){
        int currentPosition = 0;
        for (Player player : players) {
            player.setTablePosition(currentPosition);
            currentPosition++;
        }
    }

    protected void dealCards(){}

    protected void payBlinds(){
        for (Player player : players) {
            if(player.getPosition() == PlayerPosition.BIGBLIND){
                if(player.getChips() > bigBlind) {
                    player.bet(bigBlind);
                    addToPot(bigBlind);
                }else{
                    player.bet(player.getChips());
                    addToPot(player.getChips());
                }
                bettingRules.setLatestBet(bigBlind);
                bettingRules.setRaised(true);
            }else if(player.getPosition() == PlayerPosition.SMALLBLIND){
                if(player.getChips() > smallBlind) {
                    player.bet(smallBlind);
                    addToPot(smallBlind);
                }else{
                    player.bet(player.getChips());
                    addToPot(player.getChips());
                }

            }
        }
        bettingRules.setPot(pot);
    }

    private void dealFlop(){
        for (int i = 0; i < 3; i++) {
            board.addCard(deck.drawCard());
        }
    }



    private void dealOneCard(){
        board.addCard(deck.drawCard());
    }

    private void compareHands(){
        List<PokerHand> bestHands = new ArrayList<>();
        boolean mustSplitPot = false;
        for (Player player : players) {
            if(player.isActive()) {
                System.out.println(player);
                if(player.getHand().size() == 4) {
                    bestHands.add(PokerHandCalc.getBestOmahaHand(player, board));
                }else{
                    bestHands.add(PokerHandCalc.getBestHoldemHand(player, board));
                }
            }
        }
        System.out.println(board);

        Collections.sort(bestHands, new HandComparator());
        PokerHand bestHand = bestHands.get(0);
        for(int i = 1; i < bestHands.size(); i++){
            if(bestHands.get(i).getPlayer().getTotalBetThisRound() > bestHand.getPlayer().getTotalBetThisRound()){
                mustSplitPot = true;
            }
        }

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
        if(mustSplitPot){
            calculateSidePots(bestHands,players);
        }else {
            awardWinner(bestHand.getPlayer(), pot);
        }
    }

    private void calculateSidePots(List<PokerHand> bestHands, List<Player> players){
        int currentPot = 0;
        Player currentBestPlayer = bestHands.get(0).getPlayer();
        List<PokerHand> higherBettingPlayerHands = new ArrayList<>();
        List<Player> higherBettingPlayers = new ArrayList<>();
        for(Player player : players){
            if(player.getTotalBetThisRound() <= currentBestPlayer.getTotalBetThisRound()){
                currentPot += player.getTotalBetThisRound();
            }else{
                currentPot += currentBestPlayer.getTotalBetThisRound();
                player.setTotalBetThisRound(player.getTotalBetThisRound() - currentBestPlayer.getTotalBetThisRound());
                higherBettingPlayers.add(player);
            }
        }
        for(PokerHand bestHand : bestHands){
            if(higherBettingPlayers.contains(bestHand.getPlayer())){
                higherBettingPlayerHands.add(bestHand);
            }
        }
        awardWinner(currentBestPlayer,currentPot);
        if(!higherBettingPlayerHands.isEmpty()){
            System.out.println("Higher betting players: " + higherBettingPlayers);
            Collections.sort(higherBettingPlayerHands, new HandComparator());
            calculateSidePots(higherBettingPlayerHands,higherBettingPlayers);
        }
    }

    public void newRound() {
        resetGame();
        updatePlayerPositions();
        if(!isGameOver) {
            dealCards();
            payBlinds();
            checkForAction();
        }
    }


    public void checkForAction(){
        frame.updateUi();
        if(currentPlayer.getController().equals("ai")){
            String decision = ai.decide(currentPlayer);
            switch(decision) {
                case "check":
                    currentPlayer.check();
                    advanceGame();
                    break;
                case "call":
                    int amount = currentPlayer.call(bettingRules.getLatestBet());
                    addToPot(amount);
                    advanceGame();
                    break;
                default:
                    System.out.println("NOT A VALID PLAYER IN AI");
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
                System.out.println("Current Player: " + currentPlayer.getName());
                checkForAction();
            }
        }
    }


    public void advanceGame(){
        Player nextPlayer = nextActivePlayer(currentPlayer);

        if (nextPlayer.equals(nextActivePlayer(nextPlayer))) {
            awardWinner(nextPlayer, pot);
            newRound();
        }else if (nextPlayer.equals(latestBettingPlayer)) {
            nextStreet();
        }else {
            if(!latestBettingPlayer.isActive()){ latestBettingPlayer = nextActivePlayer(latestBettingPlayer); }
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
        bettingRules.setRaised(true);
        bettingRules.setLatestBet(currentPlayer.getActiveBet());
        bettingRules.setPot(pot);
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
                compareHands();
                resetGame();
                newRound();
        }
        if (dealCounter != 1) newStreet();

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    protected void resetGame(){
        deck.shuffleDeck();
        board.resetBoard();
        dealCounter = 1;
        for (Player player : players) {
            player.resetHand();
            player.setActive(true);
            player.newRound();
            player.resetTotalBet();
        }
    }

    public void resetPlayerBets(){
        for (Player player : players) {
            player.newRound();
        }
        bettingRules.setRaised(false);
    }


    protected void updatePlayerPositions(){
        List<Player> losers = new ArrayList<>();
        for (Player player : players) {
            if(player.getChips() <= 0) losers.add(player);
        }

        lostPlayers.addAll(losers);
        players.removeAll(losers);

        System.out.println(players);
        players.add(players.remove(0));
        System.out.println(players);

        if(players.size() == 1){
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

    public void gameOver(){
        System.out.println("Winner: " + players.get(0).getName() + ",  Yaaaaaaaaaay!! you won the whole game!! you are absolutely the best player");
        Collections.reverse(lostPlayers);
        GameOverFrame newFrame = new GameOverFrame(lostPlayers);
        newFrame.pack();
        newFrame.setVisible(true);
        frame.setVisible(false);
        frame.dispose();
    }


    public void awardWinner(Player winner, int amount){
        System.out.println(winner.getName() + " wins " + amount + " chips");
        winner.addChips(amount);
        pot -= amount;
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

    public PokerFrame getFrame() { return frame; }

    public boolean isMultiplayer() { return multiplayer; }

    public void setFrame(final PokerFrame frame) { this.frame = frame; }

    public void setMultiplayer(final boolean multiplayer) { this.multiplayer = multiplayer; }

    public void setCurrentPlayer(final Player currentPlayer) { this.currentPlayer = currentPlayer; }

    public void setPot(final int pot) { this.pot = pot; }
}

