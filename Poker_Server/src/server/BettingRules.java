package server;

public interface BettingRules
{
    public boolean isLegalRaise(int raise);

    public boolean isLegalAllIn();

    public void setLatestBet(int latestBet);

    public int getLatestBet();

    public boolean someoneRaised();

    public void setRaised(boolean raised);

    public void setPot(int pot);

    public void setMinimumBet(int minimumBet);
}
