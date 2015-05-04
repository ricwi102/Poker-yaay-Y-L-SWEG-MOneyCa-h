package server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Poker base that include the shared rules of all pokergames available with this program
 */

public class PokerBase
{
    protected List<Player> players;
    protected List<Player> lostPlayers = new ArrayList<>();
    protected Board board;
    protected Deck deck = new Deck();
    protected Player currentPlayer = null;
    protected Player latestBettingPlayer = null;
    protected int dealCounter = 1;
    protected int pot = 0;

    protected BettingRules bettingRules;
    protected Ai ai = new Ai(this);
    protected boolean isGameOver = false;
    protected boolean checkingForAction = false;
    protected List<ClientWorker> clients = null;

    protected static final int SMALL_BLIND = 10;
    protected static final int BIG_BLIND = 20;


    protected PokerBase(final List<Player> players, final Board board, final BettingRules bettingRules) {
        this.players = players;
        this.board = board;
        this.bettingRules = bettingRules;
        bettingRules.setMinimumBet(BIG_BLIND);

        setTablePositions();
        updatePlayerPositions();
        payBlinds();
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
                if(player.getChips() > BIG_BLIND) {
                    player.bet(BIG_BLIND);
                    addToPot(BIG_BLIND);
                }else{
                    player.bet(player.getChips());
                    addToPot(player.getActiveBet());
                }
                bettingRules.setLatestBet(BIG_BLIND);
                bettingRules.setRaised(true);
            }else if(player.getPosition() == PlayerPosition.SMALLBLIND){
                if(player.getChips() > SMALL_BLIND) {
                    player.bet(SMALL_BLIND);
                    addToPot(SMALL_BLIND);
                }else{
                    player.bet(player.getChips());
                    addToPot(player.getActiveBet());
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
            Collection<Player> playersWithSameHand = new ArrayList<>();
            playersWithSameHand.add(bestHands.get(0).getPlayer());
            Comparator<PokerHand> comparator = new HandComparator();
            for (int i = 1; i < bestHands.size(); i++) {
                if (comparator.compare(bestHands.get(0), bestHands.get(i)) == 0){
                    playersWithSameHand.add(bestHands.get(i).getPlayer());
                }
            }
            awardWinner(playersWithSameHand, pot);
        }
    }

    private void calculateSidePots(List<PokerHand> bestHands, Iterable<Player> players){
        int currentPot = 0;
        Player currentBestPlayer = bestHands.get(0).getPlayer();
        List<PokerHand> higherBettingPlayerHands = new ArrayList<>();
        Collection<Player> higherBettingPlayers = new ArrayList<>();
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
        Collection<Player> winners = new ArrayList<>();
        winners.add(currentBestPlayer);
        awardWinner(winners,currentPot);
        if(!higherBettingPlayerHands.isEmpty()){
            System.out.println("Higher betting players: " + higherBettingPlayers);
            Collections.sort(higherBettingPlayerHands, new HandComparator());
            calculateSidePots(higherBettingPlayerHands,higherBettingPlayers);
        }
    }

    public void newRound() {
        sendOutAllCards();

        try {
            final int sleepTimeInSeconds = 6;
            Thread.sleep(sleepTimeInSeconds * 1000);
        }catch (InterruptedException e){
            e.printStackTrace();
            System.out.println("Interrupted action");
        }

        sendResetAllCards();

        resetGame();
        updatePlayerPositions();
        if(!isGameOver) {
            dealCards();
            sendPlayerCards();
            payBlinds();
            sendUpdateNewRound();
            if (!checkCurrentPlayerForChips()){
                 advanceGame();
             } else {
                 checkForAction();
            }

        }
    }


    public void checkForAction(){
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
                    System.out.println("NOT A VALID PLAYER IN AI");
                    advanceGame();
            }
        }else{
            checkingForAction = true;
        }
    }


    private void newStreet() {
        sendUpdateNewStreet();
        resetPlayerBets();
        Player nextPlayer = nextActivePlayerWithChips(currentPlayer);
        if (nextPlayer.equals( nextActivePlayerWithChips(nextPlayer))) {
            nextStreet();
        } else {
            currentPlayer = getFirstPlayer();
            latestBettingPlayer = currentPlayer;
            sendCurrentPlayer();
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
            Collection<Player> winners = new ArrayList<>();
            winners.add(nextPlayer);
            awardWinner(winners, pot);
            newRound();
        }else if (nextPlayer.equals(latestBettingPlayer)) {
            nextStreet();
        }else {
            if(!latestBettingPlayer.isActive()){ latestBettingPlayer = nextActivePlayer(latestBettingPlayer); }
            currentPlayer = nextPlayer;
            sendCurrentPlayer();
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


    public boolean raise(int chips) {
        checkingForAction = false;
        if (bettingRules.isLegalRaise(chips + currentPlayer.getActiveBet(), currentPlayer.getActiveBet())) {
            addToPot(chips);
            currentPlayer.bet(chips);
            latestBettingPlayer = currentPlayer;
            bettingRules.setRaised(true);
            bettingRules.setLatestBet(currentPlayer.getActiveBet());
            bettingRules.setPot(pot);
            sendUpdateCurrentPlayer();
            sendUpdatePot();
            advanceGame();
            return true;
        }
        checkingForAction = true;
        return false;
    }

    public void call(){
        checkingForAction = false;

        System.out.println("CALL");
        int call = currentPlayer.call(bettingRules.getLatestBet());
        addToPot(call);
        sendUpdateCurrentPlayer();
        sendUpdatePot();

        advanceGame();
    }

    public boolean check(){
        checkingForAction = false;
        if (currentPlayer.getActiveBet() >= bettingRules.getLatestBet()){
            sendUpdateCurrentPlayer();
            advanceGame();
            return true;
        }
        checkingForAction = true;
        return false;
    }

    public void allIn(){
        checkingForAction = false;
        int amount = currentPlayer.bet(currentPlayer.getChips());
        addToPot(amount);
        sendUpdateCurrentPlayer();
        sendUpdatePot();

        advanceGame();
    }

    public void fold(){
        checkingForAction = false;
        currentPlayer.fold();
        sendUpdateCurrentPlayer();
        advanceGame();
    }

    private void sendUpdateCurrentPlayer(){
        for (ClientWorker client : clients) {
            client.sendMessageToOut("UPDATE&PLAYER&" + currentPlayer.getName() + "&" + currentPlayer.getChips() + "&" +
                                    currentPlayer.getActiveBet() + "&" + currentPlayer.isActive() + "&" +
                                    currentPlayer.getPosition());
        }
    }

    private void sendOutAllCards(){
        for (ClientWorker client : clients) {
            for (Player player : players) {
                StringBuilder builder = new StringBuilder();
                builder.append("UPDATE&CARDS&PLAYER&");
                builder.append(player.getName());
                builder.append("&");
                for (Card card: player.getHand()){
                    builder.append(card);
                    builder.append("%");
                }
                client.sendMessageToOut(builder.toString());
            }
        }
    }

    private void sendResetAllCards(){
        for (ClientWorker client : clients) {
            for (Player player : players) {
                StringBuilder builder = new StringBuilder();
                builder.append("UPDATE&CARDS&PLAYER&");
                builder.append(player.getName());
                client.sendMessageToOut(builder.toString());
            }
        }
    }

    private void sendUpdateAllPlayers(){
        for (Player player : players) {
            for (ClientWorker client : clients) {
                client.sendMessageToOut(
                        "UPDATE&PLAYER&" + player.getName() + "&" + player.getChips() + "&" + player.getActiveBet() + "&" +
                        player.isActive() + "&" +
                        player.getPosition());
            }
        }
    }

    private void sendUpdatePot(){
        for (ClientWorker client : clients) {
            client.sendMessageToOut("UPDATE&POT&" + pot + "&" + bettingRules.getLatestBet());
        }
    }

    private void sendUpdateBoardCards(){
        for (ClientWorker client : clients) {
            client.sendMessageToOut("UPDATE&CARDS&BOARD&" + board);
        }
    }

    private void sendPlayerCards(){
        for (ClientWorker client : clients) {
            client.sendPlayerCards();
        }
    }

    private void sendCurrentPlayer(){
        for (ClientWorker client : clients) {
            client.sendMessageToOut("UPDATE&CURRENTPLAYER&" + currentPlayer.getName());
        }
    }

    private void sendUpdateNewStreet(){
        for (ClientWorker client : clients) {
            client.sendMessageToOut("UPDATE&NEWSTREET");
        }
    }


    public void sendUpdateNewRound(){
        sendPlayerCards();
        sendUpdateAllPlayers();
        sendCurrentPlayer();
        sendUpdatePot();
        sendUpdateBoardCards();
    }

    private void sendTablePositions(){
        StringBuilder builder = new StringBuilder();
        for (Player player : players) {
            builder.append(player.getName());
            builder.append("#");
            builder.append(player.getTablePosition());
            builder.append("%");
        }
        for (ClientWorker client : clients) {
            client.sendMessageToOut("UPDATE&TABLEPOSITIONS&" + builder);
        }
    }




    public void addToPot(int chips) {
        pot += chips;
    }

    public void nextStreet(){
        bettingRules.setLatestBet(0);
        switch(dealCounter){
            case 0:
                dealCards();
                sendPlayerCards();
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
                newRound();
        }
        sendUpdateBoardCards();
        if (dealCounter != 1) newStreet();

    }



    protected void resetGame(){
        deck.shuffleDeck();
        board.resetBoard();
        dealCounter = 1;
        for (Player player : players) {
            player.resetHand();
            player.activate();
            player.newRound();
            player.resetTotalBet();
        }
    }

    protected void resetPlayerBets(){
        for (Player player : players) {
            player.newRound();
        }
        bettingRules.setRaised(false);
    }


    protected void updatePlayerPositions(){
        System.out.println("HEJ");
        Collection<Player> losers = new ArrayList<>();
        for (Player player : players) {
            if(player.getChips() <= 0) losers.add(player);
        }

        if (clients != null) {
            for (ClientWorker client : clients) {
                for (Player loser : losers) {
                    client.sendMessageToOut("REMOVEPLAYER&" + loser.getName());
                }
            }
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
        System.out.println(currentPlayer);
        latestBettingPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
           return currentPlayer;
       }

    public boolean isCheckingForAction() { return checkingForAction; }

    public void gameOver(){
        System.out.println("Winner: " + players.get(0).getName() + ",  Yaaaaaaaaaay!! you won the whole game!! you are absolutely the best player");
        Collections.reverse(lostPlayers);
    }

    public void awardWinner(Collection<Player> winners, int amount){
        for (Player winner : winners) {
            System.out.println(winner.getName() + " wins " + amount/winners.size() + " chips");
            winner.addChips(amount/winners.size());
            pot -= amount/winners.size();
        }
    }

    public void addClients(List<ClientWorker> clients){
        this.clients = clients;
        sendTablePositions();
    }

    private boolean checkCurrentPlayerForChips(){
        return currentPlayer.getChips() > 0;
    }

    public BettingRules getBettingRules() {
        return bettingRules;
    }

}

