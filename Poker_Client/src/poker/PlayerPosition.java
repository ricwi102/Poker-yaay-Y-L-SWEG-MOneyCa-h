package poker;

/**
 * This enum class contains all possible positions for a plaeyr in a game of poker.
 * Each player gets a position which is updated every round. A players position decides wheter he/she has to pay blinds
 * and in which order they get to act.
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public enum PlayerPosition {
    /**
     * Used only for visuals and as an anchor point.
     */
    DEALER,
    /**
     * Player has to pay small blind, half of the big blind (pay a set sum of chips at the start of the round).
     */
    SMALLBLIND,
    /**
     * Player has to pay big blind, double the small blind (pay a set sum of chips at the start of the round).
     */
    BIGBLIND,
    /**
     * Player doesn't have to do anything special.
     */
    STANDARD
}
