package poker;

/**
 * This enum class lists all possible colors that a card can have. The integer value of each
 * color is here to make the drawing of the cards easier, no color is more "valuable" in the game,
 * they are all equal.
 *
 * @author Johannes Palm Myllylä, RIchard Wigren
 * @version 1.0
 */
@SuppressWarnings("JavaDoc") //Could not find a good explanation. Explains itself for the most part.
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
