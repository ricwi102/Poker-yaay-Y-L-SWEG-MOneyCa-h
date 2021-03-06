package poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class is takes in chips from players and decides who should win the total amount of chips in the object.
 * Multiple objects of this type can be used for "split-pot" situatuations.
 *
 * @author Johannes Palm Myllylä, RIchard Wigren
 * @version 1.0
 */

public class Pot
{
    private int highestBet = 0;
    private int pot = 0;
    private Collection<Player> players = new ArrayList<>();


    public int addToPlayerBet(int amount, Player player){
        if (highestBet == 0){
            highestBet = amount;
            addToPot(amount);
            players.add(player);
            return 0;
        } else {
            addToPot(highestBet);
            players.add(player);
            return amount - highestBet;
        }
    }

    public void resolveWinners(){
        awardWinners(getWinners());
    }

    private Collection<Player> getWinners(){
        List<PokerHand> contenders = players.stream().filter(Player::isActive).map(Player::getBestHand).collect(Collectors.toList());

        Collection<Player> winners = new ArrayList<>();
        Comparator<PokerHand> comparator = new HandComparator();
        Collections.sort(contenders, comparator);

        winners.add(contenders.get(0).getPlayer());
        for (int i = 1; i < contenders.size(); i++) {
            if (comparator.compare(contenders.get(0), contenders.get(i)) == 0){
                winners.add(contenders.get(i).getPlayer());
            } else {
                break;
            }
        }
        return winners;
    }

    private void awardWinners(Collection<Player> winners){
        assert pot >= 0 : "Negative pot";
        winners.forEach(player -> player.addChips(pot / winners.size()));
    }

    private void addToPot(int amount){
        pot += amount;
    }
}
