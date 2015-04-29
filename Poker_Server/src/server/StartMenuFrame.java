package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StartMenuFrame extends JFrame implements ActionListener
{
    private JButton setUpGame;
    private JButton startGame;
    private JButton quit;
    private JButton potLimit;
    private JButton noLimit;
    private JButton omaha;
    private JButton holdem;
    private JButton addPlayer;
    private JButton addAiPlayer;
    private JLabel gameRules;
    private JLabel bettingStructure;
    private JLabel playerLabel;
    private GameType gameType;
    private BettingRules bettingRules;
    private List<Player> players;
    private boolean hasNonAiPlayer;

    public StartMenuFrame() throws HeadlessException {
	super("Main menu");
	gameType = GameType.HOLDEM;
	players = new ArrayList<>();
	bettingRules = new NoLimit();
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	this.setLayout(new BorderLayout());
	hasNonAiPlayer = false;
	gameRules = new JLabel("Game type: " + "Texas Holem");
	gameRules.setFont(new Font("Arial", Font.BOLD, 16));
	bettingStructure = new JLabel("Betting structure: " + "No Limit");
	bettingStructure.setFont(new Font("Arial", Font.BOLD, 16));
	playerLabel = new JLabel("Players: " + 0);
	playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
	potLimit = new JButton("Pot Limit");
	noLimit = new JButton("No Limit");
	omaha = new JButton("Omaha");
	holdem = new JButton("Texas Holdem");
	addPlayer = new JButton("Add Player");
	addAiPlayer = new JButton("Add AI player");
	startGame = new JButton("Start Game!");

	createStartButtons();
    }

    private void createStartButtons(){
	final int frameWidth = 400;
	final int frameHeight = 400;
	Component spacing = Box.createRigidArea(new Dimension(0,10));
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	setUpGame = new JButton("Set up a game of poker");
	quit = new JButton("Quit");

	setUpGame.addActionListener(this);
	quit.addActionListener(this);

	setUpGame.setAlignmentX(CENTER_ALIGNMENT);
	quit.setAlignmentX(CENTER_ALIGNMENT);
	setUpGame.setAlignmentY(CENTER_ALIGNMENT);
	quit.setAlignmentY(BOTTOM_ALIGNMENT);
	panel.add(setUpGame);
	panel.add(spacing);
	panel.add(quit);
	panel.setPreferredSize(new Dimension(frameWidth,frameHeight));

	this.add(panel, BorderLayout.CENTER);
    }

    private void createGameOptionsMenu(){
	getContentPane().removeAll();
	Component spacing = Box.createRigidArea(new Dimension(0,16));
	final int frameWidth = 400;
	final int frameHeight = 400;

	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


	potLimit.addActionListener(this);
	noLimit.addActionListener(this);
	omaha.addActionListener(this);
	holdem.addActionListener(this);
	addPlayer.addActionListener(this);
	addAiPlayer.addActionListener(this);
	startGame.addActionListener(this);

	panel.add(gameRules);
	panel.add(holdem);
	panel.add(omaha);
	panel.add(bettingStructure);
	panel.add(noLimit);
	panel.add(potLimit);
	panel.add(playerLabel);
	panel.add(addPlayer);
	panel.add(addAiPlayer);
	panel.add(spacing);
	panel.add(startGame);
	panel.setPreferredSize(new Dimension(frameWidth,frameHeight));

	getContentPane().add(panel, BorderLayout.CENTER);
	this.revalidate();
    }

    public void actionPerformed(ActionEvent e){
	if(e.getSource().equals(setUpGame)){
	    createGameOptionsMenu();
	}else if(e.getSource().equals(holdem)){
	    gameRules.setText("Game type: " + "Texas Holem");
	    gameType = GameType.HOLDEM;
	}else if(e.getSource().equals(omaha)){
	    gameRules.setText("Game type: " + "Omaha");
	    gameType = GameType.OMAHA;
	}else if(e.getSource().equals(noLimit)){
	    bettingStructure.setText("Betting structure: " + "No Limit");
	    bettingRules = new NoLimit();
	}else if(e.getSource().equals(potLimit)){
	    bettingRules = new PotLimit();
	    bettingStructure.setText("Betting structure: " + "Pot Limit");
	}else if(e.getSource().equals(addPlayer)){
	    String name = JOptionPane.showInputDialog("Player name: ");
	    if(name != null) {
		players.add(new Player(name));
		playerLabel.setText("Players: " + players);
		hasNonAiPlayer = true;
	    }
	}else if(e.getSource().equals(addAiPlayer)) {
	    String name = JOptionPane.showInputDialog("Player name: ");
	    if (name != null) {
		players.add(new Player(name,"ai"));
		playerLabel.setText("Players: " + players);
	    }
	}else if(e.getSource().equals(startGame)){
	    if(players.size() >= 2 && hasNonAiPlayer){
		if(gameType == GameType.HOLDEM){
		    Board board = new Board();
		    Holdem holdem = new Holdem(players,board,bettingRules);
		    PokerFrame frame = new PokerFrame(holdem);
		    frame.pack();
		    frame.setVisible(true);
		    holdem.checkForAction();
		    setVisible(false);
		    dispose();
		}else if(gameType == GameType.OMAHA){
		    Board board = new Board();
		    Omaha omaha = new Omaha(players,board,bettingRules);
		    PokerFrame frame = new PokerFrame(omaha);
		    frame.pack();
		    frame.setVisible(true);
		    omaha.checkForAction();
		    setVisible(false);
		    dispose();
		}
	    }else{
		JOptionPane.showMessageDialog(this, "Must have at least 2 players, and at least 1 non AI player");
	    }
	}
    }
}
