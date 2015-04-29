package poker;


public class Ai {
    private PokerBase pokerType;
    private BettingRules bettingRules;



    public Ai(PokerBase pokerType) {
        this.pokerType = pokerType;
        bettingRules = pokerType.getBettingRules();
    }

    public String decide(Player player) {
        if(player == null){
            return "";
        }else if (player.getActiveBet() == bettingRules.getLatestBet()) {
            System.out.println(player.getName() + " checks!");
            return "check";
        } else {
            System.out.println(player.getName() + " calls!");
            return "call";
        }
    }


}
