package server;

import java.util.Comparator;
/**
 * This Comparator compares the amount a Player has bet this round
 *
 * returns -1 if player1 has bet less,
 * returns 1 if player1 has bet more
 * and returns 0 if player1 and player2 has bet the same amount.
 *
 * @author Johannes Palm Myllylä, RIchard Wigren
 * @version 1.0
 */


public class TotalBetComparator implements Comparator<Player>
{
    public int compare(Player player1, Player player2){
        if (player1.getTotalBetThisRound() < player2.getTotalBetThisRound()){
            return -1;
        }else if (player1.getTotalBetThisRound() > player1.getTotalBetThisRound()){
            return 1;
        }else{
            return 0;
        }
    }
}
