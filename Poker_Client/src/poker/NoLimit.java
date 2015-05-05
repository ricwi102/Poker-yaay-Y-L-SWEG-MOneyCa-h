package poker;

/**
 * This class is used to keep track of all betting in a No-Limit game. Like keeping track of the latest bet,
 * checking if a certain bet is legal and so on.
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public class NoLimit implements BettingRules
{
    private int latestBet;
    private boolean raised;
    private int pot;
    private int minimumBet;

    public NoLimit() {
        latestBet = 0;
        raised = false;
        pot = 0;
        minimumBet = 0;
    }

    public boolean isLegalRaise(int raise, int activeBet) {
	if(raised) {
            return raise >= 2 * latestBet;
        }else{
            return raise >= minimumBet;
        }
    }

    public boolean isLegalAllIn(){ return true; }

    public int getPot() { return pot; }

    public void setLatestBet(final int latestBet) { this.latestBet = latestBet;}

    public int getLatestBet() {
	return latestBet;
    }

    public void setRaised(boolean raised) {
        this.raised = raised;
    }

    public void setMinimumBet(final int minimumBet) {
        this.minimumBet = minimumBet;
    }

    public void setPot(int amount) { pot = amount; }

    public void addToPot(int amount) { pot += amount; }

    public void resetPot() { pot = 0; }
}
