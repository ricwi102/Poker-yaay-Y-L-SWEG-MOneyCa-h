package server;

/**
 * Type of hand with value assigned to it,
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
