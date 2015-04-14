package poker;

import java.util.List;

public class BettingRules
{
    private int latestBet;
    private boolean raised;

    public BettingRules() {
        latestBet = 0;
        raised = false;
    }

    public boolean isLegalRaise(int raise) {
	return raise >= 2 * latestBet;
    }

    public boolean hasUnresolvedRaise(Player[] players){
        for (Player player : players) {
            if(!player.hasCalled() && player.isActive()) return true;
        }
        return false;
    }

    public void setLatestBet(final int latestBet) {
	this.latestBet = latestBet;
    }

    public int getLatestBet() {
	return latestBet;
    }

    public boolean someoneRaised() {
        return raised;
    }

    public void setRaised(boolean raised) {
        this.raised = raised;
    }
}
