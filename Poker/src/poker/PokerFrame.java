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
    private JButton fold;

    public PokerFrame(final Holdem holdem) throws HeadlessException {
	super("Pokr sweg, holdum YÅLÅ");
	this.holdem = holdem;
	this.setLayout(new BorderLayout());
	createButtons();
	createMenu();
    }

    private void createButtons(){
	nextStreet = new JButton("next street");
	nextStreet.addActionListener(this);
	fold = new JButton("fold");
	fold.addActionListener(this);
	this.add(fold);
	this.add(nextStreet);
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
    	if (e.getSource().equals(item)){
	    System.exit(0);
	}else if(e.getSource().equals(nextStreet)){
	    holdem.nextStreet();
	}
    }


}
