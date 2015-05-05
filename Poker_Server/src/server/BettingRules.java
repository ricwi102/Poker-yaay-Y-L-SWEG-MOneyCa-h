package server;

/**
 * Interface for the implemented betting rules in the game.
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
