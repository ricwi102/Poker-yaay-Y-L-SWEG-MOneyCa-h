package poker;

public class BettingRules
{
    private int latestBet;

    public BettingRules() {
	latestBet = 0;
    }

    public boolean isLegalRaise(int raise) {
	return raise >= 2 * latestBet;
    }

    public void setLatestBet(final int latestBet) {
	this.latestBet = latestBet;
    }

    public int getLatestBet() {
	return latestBet;
    }
}
