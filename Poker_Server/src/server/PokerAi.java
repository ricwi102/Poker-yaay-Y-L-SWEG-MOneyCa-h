package server;


/**
 * Primitive artificial intelligence mostly implemented for testing
 */

public class PokerAi
{
    private BettingRules bettingRules;



    public PokerAi(PokerBase pokerType) {
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
