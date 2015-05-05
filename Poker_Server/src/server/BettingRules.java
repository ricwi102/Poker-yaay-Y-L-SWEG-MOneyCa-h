package server;

/**
 * This is the interface for the rules regarding betting in the game. Classes PotLimit and NoLimit
 * implement this interface and they make up the 2 different betting structures available in the game
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public interface BettingRules
{

    boolean isLegalRaise(int raise, int activeBet);

    boolean isLegalAllIn();

    int getPot();

    int getLatestBet();

    void setLatestBet(int latestBet);

    void setRaised(boolean raised);

    void setMinimumBet(int minimumBet);

    void addToPot(int chips);

    void resetPot();

}
