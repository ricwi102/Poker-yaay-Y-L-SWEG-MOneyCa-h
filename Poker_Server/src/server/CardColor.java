package server;

public enum CardColor
{
    CLUBS(0),SPADES(1),HEARTS(2),DIAMONDS(3);
    private final int value;

    private CardColor(int value) {
                this.value = value;
            }

    public int getValue() {
                return value;
            }
}
