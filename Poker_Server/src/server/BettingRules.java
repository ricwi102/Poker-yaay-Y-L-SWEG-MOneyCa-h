package server;

public interface BettingRules
{
    public boolean isLegalRaise(int raise, int activeBet);

    public boolean isLegalAllIn();

    public void setLatestBet(int latestBet);

    public int getLatestBet();

    public void setRaised(boolean raised);

    public void setPot(int pot);

    public void setMinimumBet(int minimumBet);
}
