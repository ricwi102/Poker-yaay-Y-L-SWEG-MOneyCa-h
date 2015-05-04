package server;

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
            return (raise >= 2 * latestBet) && (raise <= pot + (2 * latestBet) - activeBet);
        }else{
            return raise >= minimumBet && (raise <= pot * 2);
        }
    }

    public boolean isLegalAllIn(){ return false; }

    public void setLatestBet(final int latestBet) { this.latestBet = latestBet;}

    public int getLatestBet() {
    	return latestBet;
        }

    public void setRaised(boolean raised) {
            this.raised = raised;
        }

    public void setPot(final int pot) { this.pot = pot; }

    public void setMinimumBet(final int minimumBet) {
        this.minimumBet = minimumBet;
    }
}
