package poker_client;


import poker.BettingRules;
import poker.Board;
import poker.NoLimit;
import poker.GameType;
import poker.Player;
import poker.PokerBase;
import poker.PokerFrame;
import poker.PotLimit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


/**
 * A frame containing all the Components relevant to connecting to  a server
 * and methods for switching between thos components.
 *
 * Contains ConnectToServerComponent which lets a client connect to a server.
 * Also contains LobbyComponent which represents a game lobby before the game has started.
 *
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public class ClientFrame extends JFrame
{

    private Client client;
    private ClientListener clientListener = new ClientListener(this);
    private JMenuItem item;
    private JPanel layouts;
    private ConnectToServerComponent connectComponent;
    private LobbyComponent lobbyComponent;
    private CardLayout cardLayout = new CardLayout();


    private static final String CONNECT = "CONNECT";
    private static final String LOBBY = "LOBBY";

    public ClientFrame(Client client) throws HeadlessException {
        super("Pokr sweg, holdum YÅLÅ");
        this.client = client;
        connectComponent = new ConnectToServerComponent(clientListener, new ClientKeyAdapter(this));
        lobbyComponent = new LobbyComponent(clientListener);

        layouts = new JPanel(cardLayout);

        layouts.add(connectComponent, CONNECT);
        layouts.add(lobbyComponent, LOBBY);


        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.setLayout(new BorderLayout());
        this.add(layouts/*, BorderLayout.PAGE_END*/);
        createMenu();
        //displayBoard();
    }

    /*
    private void displayBoard(){
	component.setPreferredSize(component.getPreferedSize());
	this.add(component,BorderLayout.PAGE_START);
    }*/


    private void createMenu() {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu menu = new JMenu("Options");
        item = new JMenuItem("Quit Game");
        item.addActionListener(clientListener);
        menu.add(item);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }


    public void startGame(String gameMode, String betRules, List<Player> players) {
        BettingRules bettingRules;
        if (betRules.equals("POTLIMIT")) {
            bettingRules = new PotLimit();
        } else {
            bettingRules = new NoLimit();
        }

        PokerBase pokerRules;
        Board board = new Board();
        if (gameMode.equals("OMAHA")) {
            pokerRules = new PokerBase(players, board, bettingRules, GameType.OMAHA);
        } else {
            pokerRules = new PokerBase(players, board, bettingRules, GameType.HOLDEM);
        }
        pokerRules.startMultiplayer();
        client.addPokerRules(pokerRules);
        PokerFrame frame = new PokerFrame(pokerRules, client);
        frame.pack();
        frame.setVisible(true);
        setVisible(false);
        dispose();
    }

    public void lobbyFrame() {
        if (client.isHost()) {
            lobbyComponent.startComponentHost();
        } else {
            lobbyComponent.startComponent();
        }
        cardLayout.show(layouts, LOBBY);
    }


    public void connectToServerFrame() {
        connectComponent.startComponent();

        cardLayout.show(layouts, CONNECT);
    }


    public void connectToServer() {
        int serverPort = Integer.parseInt(connectComponent.getPortText());

        try {
            InetAddress address = InetAddress.getByName(connectComponent.getIpText());
            client.listenSocket(serverPort, address);
            Thread t = new Thread(client);
            t.start();
            client.sendMessageToOut("NEWPLAYER&" + connectComponent.getPlayerNameText());

        } catch (UnknownHostException exc) {
            System.err.println("UnknownHostException: " + exc.getMessage());
            JOptionPane.showMessageDialog(null, "Unknown Host");
        } catch (IOException exc) {
            System.err.println("IOException: " + exc.getMessage());
            JOptionPane.showMessageDialog(null, exc.getMessage());
        }
    }


    public void nextTextSquare() {
        Component focus = getFocusOwner();
        if (focus.equals(connectComponent.getIp())) {
            connectComponent.getPort().requestFocus();
        } else if (focus.equals(connectComponent.getPort())) {
            connectComponent.getNameField().requestFocus();
        } else if (focus.equals(connectComponent.getNameField())) {
            connectToServer();
        }
    }


    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(item)) {
            System.exit(0);
        } else if (source.equals(connectComponent.getConnect())){
            connectToServer();

        } else if (source.equals(lobbyComponent.getStartGame())){
            client.sendMessageToOut("STARTGAME");

        } else if (source.equals(lobbyComponent.getGameOptions())){
            String option = (String) lobbyComponent.getGameOptions().getSelectedItem();
            if (option.equals("Omaha")){
                client.sendMessageToOut("GAMERULES&OMAHA");
            } else {
                client.sendMessageToOut("GAMERULES&HOLDEM");
            }

        } else if (source.equals(lobbyComponent.getBetOptions())){
            String option = (String) lobbyComponent.getBetOptions().getSelectedItem();
            if (option.equals("Pot Limit")){
                client.sendMessageToOut("BETRULES&POTLIMIT");
            } else {
                client.sendMessageToOut("BETRULES&NOLIMIT");
            }

        }
    }



    public LobbyComponent getLobbyComponent() {
        return lobbyComponent;
    }
}


