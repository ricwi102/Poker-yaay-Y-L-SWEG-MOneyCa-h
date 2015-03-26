package poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PokerFrame extends JFrame implements ActionListener
{
    private Holdem holdem;
    private JMenuItem item;
    private JButton nextStreet;
    private JButton check;
    private JButton fold;
    private JButton raise;
    private JButton call;
    private JLabel currentPlayerChips;
    private JLabel pot;

    public PokerFrame(final Holdem holdem) throws HeadlessException {
	super("Pokr sweg, holdum YÅLÅ");
	this.holdem = holdem;
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	createButtons();
	createMenu();
    }

    private void createButtons(){
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	nextStreet = new JButton("next street");
	check = new JButton("check");
	fold = new JButton("fold");
	call = new JButton("call: 0");
	raise = new JButton("Raise");
	currentPlayerChips = new JLabel(holdem.getCurrentPlayer().getName() + ", Chips: " + holdem.getCurrentPlayer().getChips());
	pot = new JLabel("Pot: 0");

	nextStreet.addActionListener(this);
	check.addActionListener(this);
	fold.addActionListener(this);
	call.addActionListener(this);
	raise.addActionListener(this);

	panel.add(nextStreet);
	panel.add(check);
	panel.add(fold);
	panel.add(call);
	panel.add(raise);
	panel.add(currentPlayerChips);
	panel.add(pot);
	this.add(panel);
    }

    private void createMenu(){
	final JMenuBar menuBar = new JMenuBar();
	final JMenu menu = new JMenu("Options");
	item = new JMenuItem("Quit Game");
	item.addActionListener(this);
	menu.add(item);
	menuBar.add(menu);
	setJMenuBar(menuBar);

    }

    private void updateChips(){
	currentPlayerChips.setText(holdem.getCurrentPlayer().getName() + ", Chips: " + holdem.getCurrentPlayer().getChips());
	pot.setText("pot: " + holdem.getPot());
	call.setText("call: " + holdem.getBettingRules().getLatestBet());
    }


    public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(item)) {
	    System.exit(0);
	} else if (e.getSource().equals(nextStreet)) {
	    holdem.nextStreet();
	} else if (e.getSource().equals(check)){
	    holdem.getCurrentPlayer().check();
	    holdem.nextPlayer();
	    updateChips();
	}else if(e.getSource().equals(fold)){
	    holdem.getCurrentPlayer().fold();
	    if(holdem.checkForWinner() == null) {
		holdem.nextPlayer();
	    }
	    updateChips();
	}else if(e.getSource().equals(raise)) {
	    int bet = holdem.getCurrentPlayer().bet();
	    if (holdem.getBettingRules().isLegalRaise(bet)){
		holdem.addRaiseToPot(bet);
	    	holdem.nextPlayer();
	    	updateChips();
	    }else{
		JOptionPane.showMessageDialog(this,"Invalid ammount");
	    }
	}else if(e.getSource().equals(call)){
	    int ammount = holdem.getCurrentPlayer().call(holdem.getBettingRules().getLatestBet());
	    holdem.addCallToPot(ammount);
	    holdem.nextPlayer();
	    updateChips();
	}
    }
}
