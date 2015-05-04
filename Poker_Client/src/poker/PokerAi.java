package poker;

/**
 * This is a VERY basic AI that can be used in local games. The main focus of this game is the network
 * play (since poker is not very fun to play local on the same computer) so this class was mostly used for testing.
 * But it can still be used in game if one wants to.
 *
 *
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
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
