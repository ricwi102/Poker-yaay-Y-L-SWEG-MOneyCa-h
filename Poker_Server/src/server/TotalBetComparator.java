package server;

import java.util.Comparator;

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
