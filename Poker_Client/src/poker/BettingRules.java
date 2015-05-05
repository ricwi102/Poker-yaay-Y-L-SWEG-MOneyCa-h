package poker;

/**
 * This is the interface for the rules regarding betting in the game. Classes PotLimit and NoLimit
 * implement this interface and they make up the 2 different betting structures available in the game
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public interface BettingRules
{
    public boolean isLegalRaise(int raise, int activeBet);

    public boolean isLegalAllIn();

    public void setLatestBet(int latestBet);

    public int getLatestBet();

    public int getPot();

    public void setRaised(boolean raised);

    public void addToPot(int amount);

    public void setMinimumBet(int minimumBet);

    public void setPot(int amount);

    public void resetPot();

}
