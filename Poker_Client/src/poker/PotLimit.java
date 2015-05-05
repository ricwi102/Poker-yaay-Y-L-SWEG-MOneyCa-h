package poker;

/**
 * This class is used to keep track of all betting in a Pot-Limit game. Like keeping track of the latest bet,
 * checking if a certain bet is legal and so on.
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public class PotLimit implements BettingRules
{
    private int latestBet;
    private boolean raised;
    private int pot;
    private int minimumBet;

    public PotLimit() {
        pot = 0;
	latestBet = 0;
        minimumBet = 0;
	raised = false;
    }

    public boolean isLegalRaise(int raise, int activeBet) {
        if(raised) {
            return (raise >= 2 * latestBet) && (raise <= (pot + (2*latestBet) -activeBet));
        }else{
            return (raise >= minimumBet) && (raise <= (pot + (2*latestBet) - activeBet));
        }
    }

    public boolean isLegalAllIn(){ return false;}

    public void setLatestBet(final int latestBet) { this.latestBet = latestBet;}

    public int getLatestBet() {
    	return latestBet;
        }

    public int getPot() { return pot;}

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
