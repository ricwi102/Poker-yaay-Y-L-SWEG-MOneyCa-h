package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PokerListener implements ActionListener
{
    private PokerBase pokerBase;

    public PokerListener(PokerBase pokerBase) {
	this.pokerBase = pokerBase;
    }

    public void actionPerformed(ActionEvent e) {
   	pokerBase.nextStreet();
       }
}
