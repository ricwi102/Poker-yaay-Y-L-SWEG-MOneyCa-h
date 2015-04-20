package poker;


public class Ai {
    private Holdem holdem;
    private BettingRules bettingRules;

    public Ai(Holdem holdem) {
        this.holdem = holdem;
        bettingRules = holdem.getBettingRules();
    }

    public String decide(Player player) {
        try {
            Thread.sleep(5000);
            if (player.getActiveBet() == bettingRules.getLatestBet()) {
                System.out.println(player.getName() + " checks!");
                return "check";
            } else {
                System.out.println(player.getName() + " calls!");
                return "call";
            }
        }catch(InterruptedException e){
            if (player.getActiveBet() == bettingRules.getLatestBet()) {
                return "check";
            } else {
                return "call";
            }
        }
    }
}
