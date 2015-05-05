package server;

/**
 * Used to assign players with a special or standard role at the start of each round.
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
