package poker;

/**
 * This enum class lists all possible types of poker hands. The value integer shows how strong the
 * hand type is relative to others.
 *
 * @author Johannes Palm Myllylä, RIchard Wigren
 * @version 1.0
 */
public enum HandType{
    HIGHCARD(0), PAIR(1), TWOPAIR(2), THREEOFAKIND(3), STRAIGHT(4), FLUSH(5), FULLHOUSE(6), FOUROFAKIND(7), STRAIGHTFLUSH(8);
    private final int value;

        private HandType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
