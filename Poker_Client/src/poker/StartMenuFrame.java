package poker;

import poker_client.Client;
import poker_client.ClientFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the frame that you see when you start up a game. It handles all options for a single player game
 * and creates a ClientFrame if a player wants to play multiplayer
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public class StartMenuFrame extends JFrame implements ActionListener
{
    private JButton setUpGame;
    private JButton multiplayer;
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
	final int fontSize = 16;
	gameType = GameType.HOLDEM;
	players = new ArrayList<>();
	bettingRules = new NoLimit();
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	this.setLayout(new BorderLayout());
	hasNonAiPlayer = false;
	gameRules = new JLabel("Game type: " + "Texas Holem");
	gameRules.setFont(new Font("Arial", Font.BOLD, fontSize));
	bettingStructure = new JLabel("Betting structure: " + "No Limit");
	bettingStructure.setFont(new Font("Arial", Font.BOLD, fontSize));
	playerLabel = new JLabel("Players: " + 0);
	playerLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
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
	multiplayer = new JButton("Play Multiplayer");
	quit = new JButton("Quit");


	setUpGame.addActionListener(this);
	multiplayer.addActionListener(this);
	quit.addActionListener(this);

	setUpGame.setAlignmentX(CENTER_ALIGNMENT);
	multiplayer.setAlignmentX(CENTER_ALIGNMENT);
	quit.setAlignmentX(CENTER_ALIGNMENT);
	setUpGame.setAlignmentY(CENTER_ALIGNMENT);
	quit.setAlignmentY(BOTTOM_ALIGNMENT);
	panel.add(setUpGame);
	panel.add(spacing);
	panel.add(multiplayer);
	panel.add(spacing);
	panel.add(quit);
	panel.setPreferredSize(new Dimension(frameWidth,frameHeight));

	this.add(panel, BorderLayout.CENTER);
    }

    private void createGameOptionsMenu(){
	getContentPane().removeAll();
	final int spacingSize = 16;
	Component spacing = Box.createRigidArea(new Dimension(0,spacingSize));
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
	menuActions(e);
	startGameActions(e);
    }

    private void startGameActions(ActionEvent e) {
	if (e.getSource().equals(startGame)) {
	    if (players.size() >= 2 && hasNonAiPlayer) {

		Board board = new Board();
		PokerBase pokerBase = new PokerBase(players, board, bettingRules, gameType);
		PokerFrame frame = new PokerFrame(pokerBase);
		frame.pack();
		frame.setVisible(true);
		pokerBase.startSingleplayer();
		setVisible(false);
		dispose();

	    } else {
		JOptionPane.showMessageDialog(this, "Must have at least 2 players, and at least 1 non AI player");
	    }
	}
    }


    private void menuActions(ActionEvent e){
	if(e.getSource().equals(setUpGame)){
	    createGameOptionsMenu();
	}else if(e.getSource().equals(multiplayer)){
	    Client client = new Client();
	    ClientFrame clientFrame = new ClientFrame(client);
	    client.addFrame(clientFrame);
	    clientFrame.connectToServerFrame();

	    clientFrame.pack();
	    clientFrame.setVisible(true);
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
	    if (players.size() < 9) {
		String name = JOptionPane.showInputDialog("Player name: ");
		if (name != null) {
		    players.add(new Player(name));
		    playerLabel.setText("Players: " + players);
		    hasNonAiPlayer = true;
		}
	    } else {
		JOptionPane.showMessageDialog(null, "There are already nine Players");
	    }
	}else if(e.getSource().equals(addAiPlayer)) {
	    if (players.size() < 9) {
		String name = JOptionPane.showInputDialog("Player name: ");
		if (name != null) {
		    players.add(new Player(name,"ai"));
		    playerLabel.setText("Players: " + players);
		}
	    } else {
	 	JOptionPane.showMessageDialog(null, "There are already nine Players");
	    }
	}else if(e.getSource().equals(quit)){
	    dispose();
	    System.exit(0);
	}
    }
}
