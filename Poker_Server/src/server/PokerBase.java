package server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Poker base that include the shared rules of all poker games available with this program
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

    private List<Pot> calculatePots(){
        List<Player> sortedByLowestBet = new ArrayList<>();
        players.forEach(sortedByLowestBet::add);

        System.out.println("pkb, p: " + players);
        System.out.println("pkb, sblb1: " + sortedByLowestBet);
        Collections.sort(sortedByLowestBet, new TotalBetComparator());
        System.out.println("pkb, sblb2: " + sortedByLowestBet);

        List<Pot> pots = new ArrayList<>();

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

    //------ Functions Driving The Game Forward ---------

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
            resolveWinners();
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


    //--------- Player Actions -----------------
    //Such ass raising and calling

    public boolean raise(int chips) {
        checkingForAction = false;
        if (bettingRules.isLegalRaise(chips + currentPlayer.getActiveBet(), currentPlayer.getActiveBet())) {
            addToPot(chips);
            currentPlayer.bet(chips);
            latestBettingPlayer = currentPlayer;
            bettingRules.setRaised(true);
            bettingRules.setLatestBet(currentPlayer.getActiveBet());
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
        if (amount > bettingRules.getLatestBet()){
            bettingRules.setLatestBet(amount);
        }
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

    //---------- Senders -------------------------
    //Sends information to the clients

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
                client.sendMessageToOut("UPDATE&CARDS&PLAYER&" + player.getName());
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
            client.sendMessageToOut("UPDATE&POT&" + bettingRules.getPot() + "&" + bettingRules.getLatestBet());
        }
    }

    private void sendUpdateBoardCards(){
        for (ClientWorker client : clients) {
            client.sendMessageToOut("UPDATE&CARDS&BOARD&" + board);
        }
    }

    private void sendPlayerCards(){
        clients.forEach(ClientWorker::sendPlayerCards);
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

    private void sendLosingPlayers(Iterable<Player> losers){
        if (clients != null) {
            for (ClientWorker client : clients) {
                for (Player loser : losers) {
                    client.sendMessageToOut("REMOVEPLAYER&" + loser.getName());
                }
            }
        }
    }

    //----------------------------------------------------------------------------




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
                resolveWinners();
                newRound();
        }
        sendUpdateBoardCards();
        if (dealCounter != 1) newStreet();

    }

    public void gameOver(){
        System.out.println("Winner: " + players.get(0).getName() + ",  Yaaaaaaaaaay!! you won the whole game!! you are absolutely the best player");
        Collections.reverse(lostPlayers);
    }


    protected void resetGame(){
        deck.shuffleDeck();
        board.resetBoard();
        bettingRules.resetPot();
        dealCounter = 1;
        for (Player player : players) {
            player.resetHand();
            player.activate();
            player.newRound();
        }
    }

    protected void resetPlayerBets(){
        players.forEach(Player::resetActiveBet);
        bettingRules.setRaised(false);
    }


    protected void updatePlayerPositions(){
        System.out.println("HEJ");
        Collection<Player> losers = players.stream().filter(player -> player.getChips() <= 0).collect(Collectors.toList());

        sendLosingPlayers(losers);

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

    //-------- Getters -----------------------------------

    public Player getCurrentPlayer() {
           return currentPlayer;
    }

    public BettingRules getBettingRules() {
           return bettingRules;
    }

    public boolean isCheckingForAction() { return checkingForAction; }


    private Player getFirstPlayer() {
        return nextActivePlayer(players.get(players.size() - 1));
    }


    //-------- Setters and Adders --------------------------
    public void addClients(List<ClientWorker> clients){
        this.clients = clients;
        sendTablePositions();
    }

    private boolean checkCurrentPlayerForChips(){
        return currentPlayer.getChips() > 0;
    }

    public void addToPot(int chips) { bettingRules.addToPot(chips); }



}

