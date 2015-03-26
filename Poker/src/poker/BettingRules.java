package poker;

import java.util.List;

public class BettingRules
{
    private int latestBet;

    public BettingRules() {
	latestBet = 0;
    }

    public boolean isLegalRaise(int raise) {
	return raise >= 2 * latestBet;
    }

    public boolean hasUnresolvedRaise(Player[] players){
        for (Player player : players) {
            if(!player.hasCalled()) return true;
        }
        return false;
    }

    public void setLatestBet(final int latestBet) {
	this.latestBet = latestBet;
    }

    public int getLatestBet() {
	return latestBet;
    }
}
