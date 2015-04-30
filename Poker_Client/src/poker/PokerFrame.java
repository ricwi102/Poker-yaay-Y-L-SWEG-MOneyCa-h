package poker;

import poker_client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PokerFrame extends JFrame implements ActionListener
{
    private PokerBase pokerBase;
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
    private Client client;


    public PokerFrame(final PokerBase pokerBase) throws HeadlessException{
	super("Pokr sweg, holdum YÅLÅ");
	this.pokerBase = pokerBase;
	component = new PokerComponent(pokerBase);
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	this.setLayout(new BorderLayout());
	pokerBase.setFrame(this);
	createButtons();
	createMenu();
	displayBoard();
	updateUi();
    }

    public PokerFrame(final PokerBase pokerBase, final Client client ) throws HeadlessException{
   	super("Pokr sweg, holdum YÅLÅ");
   	this.pokerBase = pokerBase;
	this.client = client;
   	component = new PokerComponent(pokerBase);
	component.addClient(client);
   	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
   	this.setLayout(new BorderLayout());
   	pokerBase.setFrame(this);
   	createButtons();
   	createMenu();
   	displayBoard();
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

	communityCards = new JLabel();
	currentPlayerChips = new JLabel();
	currentPlayerCards = new JLabel();
	pot = new JLabel();

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
	if (pokerBase.getCurrentPlayer() != null) {

	    StringBuilder openCards = new StringBuilder();
	    for(Card card : pokerBase.getBoard().getOpenCards()){
	        openCards.append(card);
	        openCards.append(" ");
	    }


	    communityCards.setText("Community Cards: " + openCards);
	    currentPlayerChips
		    .setText(pokerBase.getCurrentPlayer().getName() + ", Chips: " + pokerBase.getCurrentPlayer().getChips());
	    pot.setText("pot: " + pokerBase.getPot());

	    if (pokerBase.isMultiplayer() && !pokerBase.getCurrentPlayer().equals(client.getYou())) {
		call.setEnabled(false);
		raise.setEnabled(false);
		allIn.setEnabled(false);
		check.setEnabled(false);
		fold.setEnabled(false);
	    } else {
		if (pokerBase.getCurrentPlayer().getActiveBet() < pokerBase.bettingRules.getLatestBet()) {
		    call.setEnabled(true);
		    raise.setEnabled(true);
		    allIn.setEnabled(true);
		    check.setEnabled(false);
		    fold.setEnabled(true);
		} else {
		    call.setEnabled(false);
		    raise.setEnabled(true);
		    allIn.setEnabled(true);
		    check.setEnabled(true);
		    fold.setEnabled(false);
		}
	    }

	    call.setText("call: " + (pokerBase.getBettingRules().getLatestBet() - pokerBase.getCurrentPlayer().getActiveBet()));
	    if (pokerBase.getCurrentPlayer().getController().equals("ai")) {
		check.setEnabled(false);
		fold.setEnabled(false);
		raise.setEnabled(false);
		call.setEnabled(false);
		allIn.setEnabled(false);
	    }
	    if (!pokerBase.getBettingRules().isLegalAllIn()) {
		allIn.setEnabled(false);
	    }
	    component.repaint();
	    System.out.println("UPDATED UI");
	}
    }


    public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(item)) {
	    System.exit(0);
	} else if (e.getSource().equals(check)){
	    if (pokerBase.isMultiplayer()){
		client.getOut().println("CHECK");
	    }else {
		pokerBase.getCurrentPlayer().check();
		pokerBase.advanceGame();
	    }
	    updateUi();
	}else if(e.getSource().equals(fold)){
	    if (pokerBase.isMultiplayer()){
	    	client.getOut().println("FOLD");
	    }else {
		pokerBase.getCurrentPlayer().fold();
		pokerBase.advanceGame();
	    }
	    updateUi();
	}else if(e.getSource().equals(raise)) {
	    int amount;
	    do {
		String input = JOptionPane.showInputDialog("Ammount to bet: ");
		amount = Integer.parseInt(input);
	    } while (pokerBase.getCurrentPlayer().getChips() - amount < 0);
	    if (pokerBase.isMultiplayer()){
	    	client.getOut().println("RAISE&" + amount);
	    }else {
	    	if (pokerBase.getBettingRules().isLegalRaise(amount + pokerBase.getCurrentPlayer().getActiveBet())){
			int bet = pokerBase.getCurrentPlayer().bet(amount);
			pokerBase.raise(bet);
			pokerBase.advanceGame();
			updateUi();
	    	}else {
		    if (pokerBase.getBettingRules() instanceof NoLimit) {
			JOptionPane.showMessageDialog(this, "Invalid ammount! Must be at least doulbe the latest bet" + "\n" +
							    "minimum amount: " +
							    pokerBase.getBettingRules().getLatestBet() * 2);
		    } else {
			JOptionPane.showMessageDialog(this,
						      "Invalid ammount! Must be at least double the latest bet but less than twice the pot" +
						      "\n" +
						      "Valid ammounts: " + pokerBase.getBettingRules().getLatestBet() * 2 +
						      " - " + pokerBase.getPot() * 2);
		    }
		}
	    }
	}else if(e.getSource().equals(call)){
	    if (pokerBase.isMultiplayer()){
	    		client.getOut().println("CALL");
	    }else {
		int amount = pokerBase.getCurrentPlayer().call(pokerBase.getBettingRules().getLatestBet());
		pokerBase.addToPot(amount);
		pokerBase.advanceGame();
	    }
	    updateUi();
	}else if(e.getSource().equals(allIn)) {
	    if (pokerBase.getBettingRules().isLegalAllIn()) {
		if (pokerBase.isMultiplayer()) {
		    client.getOut().println("ALLIN");
		} else {
		    int amount = pokerBase.getCurrentPlayer().bet(pokerBase.getCurrentPlayer().getChips());
		    pokerBase.raise(amount);
		    pokerBase.advanceGame();
		}
	    }
	    updateUi();
	}
    }


}
