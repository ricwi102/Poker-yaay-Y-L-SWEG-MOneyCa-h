package poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * This is the frame that is shown to players when they are eliminated in a network game, or when a winner has been
 * decided in a local game.
 * It shows a list of the players eliminated so far and in which order they were eliminated.
 *
 * @author Johannes Palm Myllylä, RIchard Wigren
 * @version 1.0
 */

public class GameOverFrame extends JFrame implements ActionListener
{
    private List<Player> players;
    private JButton mainMenu;

    public GameOverFrame(final List<Player> players) throws HeadlessException {
	super("Game over");
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	this.players = players;
	mainMenu = new JButton("Main menu");
	mainMenu.addActionListener(this);
	setLayout(new BorderLayout());
	showPlayerList();
	add(mainMenu, BorderLayout.PAGE_END);
    }

    private void showPlayerList(){
	final int panelHeight = 400;
	final int panelWidth = 400;
	final int headlineFontSize = 24;
	final int topListFontSize = 16;
	JLabel topList = new JLabel("<html>");
	JLabel headline = new JLabel("Player Placings");
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	panel.setPreferredSize(new Dimension(panelWidth,panelHeight));
	int playerPlacement = 1;
	for (Player player : players) {
	    topList.setText(topList.getText()  + "<br>" + playerPlacement + ": " + player.getName());
	    playerPlacement++;
	}
	topList.setText(topList.getText()  + "</html>");
	headline.setFont(new Font("Arial", Font.BOLD, headlineFontSize));
	topList.setFont(new Font("Arial", Font.BOLD, topListFontSize));
	headline.setAlignmentX(CENTER_ALIGNMENT);
	topList.setAlignmentX(CENTER_ALIGNMENT);
	panel.add(headline);
	panel.add(topList);
	add(panel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
	if(e.getSource().equals(mainMenu)){
	    StartMenuFrame frame = new StartMenuFrame();
	    frame.pack();
	    frame.setVisible(true);
	    setVisible(false);
	    dispose();
	}
    }

}


