package server;


/**
 * Contains the poker betting rules of "No Limit"
 */


public class NoLimit implements BettingRules
{
    private int latestBet = 0;
    private int minimumBet = 0;
    private int pot = 0;
    private boolean raised = false;


    public boolean isLegalRaise(int raise, int activeBet) {
	if(raised) {
            return raise >= 2 * latestBet;
        }else{
            return raise >= minimumBet;
        }
    }

    public boolean isLegalAllIn(){ return true;}

    public int getLatestBet() { return latestBet; }

    public int getPot() { return pot; }

    public void setLatestBet(final int latestBet) { this.latestBet = latestBet;}

    public void setRaised(boolean raised) { this.raised = raised; }

    public void setMinimumBet(final int minimumBet) {
        this.minimumBet = minimumBet;
    }

    public void addToPot(int chips) { pot += chips; }

    public void resetPot() {pot = 0;}
}
