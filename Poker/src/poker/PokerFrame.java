package poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PokerFrame extends JFrame implements ActionListener
{
    private Holdem holdem;
    private JMenuItem item;
    private JButton check;
    private JButton fold;
    private JButton raise;
    private JButton call;
    private JButton allIn;
    private JLabel communityCards;
    private JLabel currentPlayerChips;
    private JLabel currentPlayerCards;
    private JLabel pot;
    private PokerComponent component;

    public PokerFrame(final Holdem holdem) throws HeadlessException{
	super("Pokr sweg, holdum YÅLÅ");
	this.holdem = holdem;
	component = new PokerComponent(holdem);
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	this.setLayout(new BorderLayout());
	createButtons();
	createMenu();
	displayBoard();
	holdem.setFrame(this);
	updateUi();
    }

    private void displayBoard(){
	component.setPreferredSize(component.getPreferedSize());
	this.add(component,BorderLayout.PAGE_START);
    }

    private void createButtons(){
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	check = new JButton("Check");
	fold = new JButton("Fold");
	call = new JButton("Call: 0");
	raise = new JButton("Raise");
	allIn = new JButton("All in");

	StringBuilder openCards = new StringBuilder();
	for(Card card : holdem.getBoard().getOpenCards()){
	    openCards.append(card);
	    openCards.append(" ");}
	communityCards = new JLabel("Community Cards: " + openCards );
	currentPlayerChips = new JLabel(holdem.getCurrentPlayer().getName() + ", Chips: " + holdem.getCurrentPlayer().getChips());
	currentPlayerCards = new JLabel(holdem.getCurrentPlayer().getName() + " Cards: " + holdem.getCurrentPlayer().getHand().get(0)
					+ " " + holdem.getCurrentPlayer().getHand().get(1));
	pot = new JLabel("Pot: 0");

	check.addActionListener(this);
	fold.addActionListener(this);
	call.addActionListener(this);
	raise.addActionListener(this);
	allIn.addActionListener(this);

	call.setEnabled(false);

	panel.add(check);
	panel.add(fold);
	panel.add(call);
	panel.add(raise);
	panel.add(allIn);
	panel.add(communityCards);
	panel.add(currentPlayerChips);
	panel.add(currentPlayerCards);
	panel.add(pot);
	this.add(panel, BorderLayout.PAGE_END);
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

    public void updateUi(){
	StringBuilder openCards = new StringBuilder();
	for(Card card : holdem.getBoard().getOpenCards()){
	    openCards.append(card);
	    openCards.append(" ");}
	communityCards.setText("Community Cards: " + openCards );
	currentPlayerChips.setText(holdem.getCurrentPlayer().getName() + ", Chips: " + holdem.getCurrentPlayer().getChips());
	currentPlayerCards.setText(holdem.getCurrentPlayer().getName() + " Cards: " + holdem.getCurrentPlayer().getHand().get(0)
				   + " " + holdem.getCurrentPlayer().getHand().get(1));
	pot.setText("pot: " + holdem.getPot());
	if(holdem.getBettingRules().someoneRaised() && holdem.getCurrentPlayer().getActiveBet() != holdem.bettingRules.getLatestBet()){
	    call.setEnabled(true);
	    raise.setEnabled(true);
	    allIn.setEnabled(true);
	}else{
	    call.setEnabled(false);
	    raise.setEnabled(true);
	    allIn.setEnabled(true);
	}
	if(holdem.getCurrentPlayer().getActiveBet() == holdem.getBettingRules().getLatestBet()){
	    check.setEnabled(true);
	    fold.setEnabled(false);
	    raise.setEnabled(true);
	    allIn.setEnabled(true);
	}else{
	    check.setEnabled(false);
	    fold.setEnabled(true);
	    raise.setEnabled(true);
	    allIn.setEnabled(true);
	}
	call.setText("call: " + (holdem.getBettingRules().getLatestBet() - holdem.getCurrentPlayer().getActiveBet()));
	if(holdem.getCurrentPlayer().getController().equals("ai")){
	    check.setEnabled(false);
	    fold.setEnabled(false);
	    raise.setEnabled(false);
	    call.setEnabled(false);
	    allIn.setEnabled(false);
	}
	component.repaint();
	System.out.println("UPDATED UI");
    }


    public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(item)) {
	    System.exit(0);
	} else if (e.getSource().equals(check)){
	    holdem.getCurrentPlayer().check();
	    holdem.advanceGame();
	    updateUi();
	}else if(e.getSource().equals(fold)){
	    holdem.getCurrentPlayer().fold();
	    holdem.advanceGame();
	    updateUi();
	}else if(e.getSource().equals(raise)) {
	    int amount;
	    do {
		String input = JOptionPane.showInputDialog("Ammount to bet: ");
		amount = Integer.parseInt(input);
	    } while (holdem.getCurrentPlayer().getChips() - amount < 0);
	    if (holdem.getBettingRules().isLegalRaise(amount + holdem.getCurrentPlayer().getActiveBet())){
		int bet = holdem.getCurrentPlayer().bet(amount);
		holdem.raise(bet);
		holdem.advanceGame();
		updateUi();
	    }else{
		JOptionPane.showMessageDialog(this,"Invalid ammount");
	    }
	}else if(e.getSource().equals(call)){
	    int amount = holdem.getCurrentPlayer().call(holdem.getBettingRules().getLatestBet());
	    holdem.addToPot(amount);
	    holdem.advanceGame();
	    updateUi();
	}else if(e.getSource().equals(allIn)){
	    int amount = holdem.getCurrentPlayer().bet(holdem.getCurrentPlayer().getChips());
	    holdem.raise(amount);
	    holdem.advanceGame();
	    updateUi();
	}
    }
}
