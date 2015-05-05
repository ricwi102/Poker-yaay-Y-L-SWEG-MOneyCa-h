package server;

/**
 * Type of hand with value assigned to it,
 */
public enum HandType{
    /**
     * Lowest value, no combination of cards.
     */
    HIGHCARD(0),
    /**
     * 2 cards with the same value.
     */
    PAIR(1),
    /**
     * 2 pairs of different values
     */
    TWOPAIR(2),
    /**
     * 3 cards with the same value
     */
    THREEOFAKIND(3),
    /**
     * 5 cards in numerical order
     */
    STRAIGHT(4),
    /**
     * 5 cards of the same color
     */
    FLUSH(5),
    /**
     * 3 cards with the same value, as well as a pair of a different value
     */
    FULLHOUSE(6),
    /**
     * 4 cards of the same value
     */
    FOUROFAKIND(7),
    /**
     * A straight where all cards are of the same value
     */
    STRAIGHTFLUSH(8);
    private final int value;

        HandType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
