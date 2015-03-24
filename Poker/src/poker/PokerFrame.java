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

    public PokerFrame(final Holdem holdem) throws HeadlessException {
	super("Pokr sweg, holdum YÅLÅ");
	this.holdem = holdem;
	this.setLayout(new BorderLayout(10,10));
	createButtons();
	createMenu();
    }

    private void createButtons(){
	nextStreet = new JButton("next street");
	check = new JButton("check");
	fold = new JButton("fold");

	nextStreet.addActionListener(this);
	check.addActionListener(this);
	fold.addActionListener(this);

	this.add(nextStreet, BorderLayout.CENTER);
	this.add(check, BorderLayout.LINE_START);
	this.add(fold, BorderLayout.LINE_END);
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


    public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(item)) {
	    System.exit(0);
	} else if (e.getSource().equals(nextStreet)) {
	    holdem.nextStreet();
	} else if (e.getSource().equals(check)){
	    holdem.getCurrentPlayer().check();
	    holdem.nextPlayer();
	}else if(e.getSource().equals(fold)){
	    holdem.getCurrentPlayer().fold();
	    if(!holdem.checkForWinner()) {
		holdem.nextPlayer();
	    }
	}
    }


}
