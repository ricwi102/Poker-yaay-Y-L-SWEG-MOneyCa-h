package poker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PokerListener implements ActionListener
{
    private Holdem holdem;

    public PokerListener(Holdem holdem) {
	this.holdem = holdem;
    }

    public void actionPerformed(ActionEvent e) {
   	holdem.nextStreet();
       }
}
