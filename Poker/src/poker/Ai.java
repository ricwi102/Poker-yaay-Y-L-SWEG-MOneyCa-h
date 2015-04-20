package poker;


public class Ai {
    private Holdem holdem;
    private BettingRules bettingRules;

    public Ai(Holdem holdem) {
        this.holdem = holdem;
        bettingRules = holdem.getBettingRules();
    }

    public String decide(Player player){
        if(player.getActiveBet() == bettingRules.getLatestBet()){
            return "check";
        }else{
            return "call";
        }
    }
}
