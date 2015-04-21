package poker;


import java.awt.event.*;

import javax.swing.*;

public class Ai {
    private Holdem holdem;
    private BettingRules bettingRules;



    public Ai(Holdem holdem) {
        this.holdem = holdem;
        bettingRules = holdem.getBettingRules();
    }

    public String decide(Player player) {
        if(player == null){
            return "";
        }else if (player.getActiveBet() == bettingRules.getLatestBet()) {
            System.out.println(player.getName() + " checks!");
            return "check";
        } else {
            System.out.println(player.getName() + " calls!");
            return "call";
        }
    }


}
