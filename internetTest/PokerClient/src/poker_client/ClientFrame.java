package poker_client;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class ClientFrame extends JFrame{

    private Client client;
    private ClientListener clientListener;
    private GameInfo gameInfo;
    private JMenuItem item;
    private JButton check;
    private JButton fold;
    private JButton raise;
    private JButton call;
    private JButton allIn;
    private JLabel communityCards;
    private JLabel pot;
    private JPanel layouts;
    private ClientComponent component;
    private ConnectToServerComponent connectComponent;
    private AddPlayerComponent addPlayerComponent;
    private LobbyComponent lobbyComponent;
    private CardLayout cardLayout = new CardLayout();

    private boolean connected = false;

    private static final String CONNECT = "CONNECT";
    private static final String LOBBY = "LOBBY";
    private static final String ADD_PLAYER = "ADD_PLAYER";

    public ClientFrame(final GameInfo gameInfo, Client client) throws HeadlessException, IOException {
        super("Pokr sweg, holdum YÅLÅ");
        this.gameInfo = gameInfo;
        this.client = client;
        clientListener = new ClientListener(this);
        ClientKeyAdapter keyAdapter = new ClientKeyAdapter(this);
        component = new ClientComponent(gameInfo);
        connectComponent = new ConnectToServerComponent(clientListener, keyAdapter);
        addPlayerComponent = new AddPlayerComponent(clientListener);
        lobbyComponent = new LobbyComponent(clientListener);



        layouts = new JPanel(cardLayout);

        layouts.add(connectComponent, CONNECT);
        layouts.add(addPlayerComponent, ADD_PLAYER);
        layouts.add(lobbyComponent, LOBBY);


        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.setLayout(new BorderLayout());
        this.add(layouts/*, BorderLayout.PAGE_END*/);
        createButtons();
        createMenu();
        //displayBoard();
    }
/*
    private void displayBoard(){
	component.setPreferredSize(component.getPreferedSize());
	this.add(component,BorderLayout.PAGE_START);
    }*/

    private void createButtons() {


        check = new JButton();
        fold = new JButton();
        call = new JButton();
        raise = new JButton();
        allIn = new JButton();


        check.addActionListener(clientListener);
        fold.addActionListener(clientListener);
        call.addActionListener(clientListener);
        raise.addActionListener(clientListener);
        allIn.addActionListener(clientListener);


        communityCards = new JLabel();

        pot = new JLabel();

    }


    private void createMenu() {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu menu = new JMenu("Options");
        item = new JMenuItem("Quit Game");
        item.addActionListener(clientListener);
        menu.add(item);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }


    public void startGame() {
        this.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        StringBuilder openCards = new StringBuilder();
        for (Card card : gameInfo.getBoard().getOpenCards()) {
            openCards.append(card);
            openCards.append(" ");
        }
        communityCards.setText("Community Cards: " + openCards);

        panel.add(check);
        panel.add(fold);
        panel.add(call);
        panel.add(raise);
        panel.add(allIn);
        panel.add(communityCards);
        panel.add(pot);
        this.add(panel, BorderLayout.PAGE_END);

        updateUi();
    }

    public void lobbyFrame() {
        if (client.isHost()){
            lobbyComponent.startComponentHost();
        }else {
            lobbyComponent.startComponent();
        }

        cardLayout.show(layouts, LOBBY);

    }

    public void addPlayerFrame() {
        //Container cont = getContentPane();
        //cont.removeAll();
        addPlayerComponent.startComponent();

        cardLayout.show(layouts, ADD_PLAYER);


        //cont.doLayout();
        //cont.update(getGraphics());

    }

    public void connectToServerFrame() {
        //Container cont = getContentPane();
        //cont.removeAll();
        connectComponent.startComponent();

        cardLayout.show(layouts, CONNECT);


        //cont.doLayout();
        //cont.update(getGraphics());

    }


    public void connectToServer() {
        int serverPort = Integer.parseInt(connectComponent.getPortText());

        try {
            InetAddress address = InetAddress.getByName(connectComponent.getIpText());
            client.listenSocket(serverPort, address);
            connected = true;
            Thread t = new Thread(client);
            t.start();
            client.getOut().println("NEWPLAYER&" + connectComponent.getPlayerNameText());
            lobbyFrame();

        } catch (UnknownHostException exc) {
            System.err.println("UnknownHostException: " + exc.getMessage());
            JOptionPane.showMessageDialog(null, "Unknown Host");
        }

    }

    private void updateUi() {
        StringBuilder openCards = new StringBuilder();
        for (Card card : gameInfo.getBoard().getOpenCards()) {
            openCards.append(card);
            openCards.append(" ");
        }


        communityCards.setText("Community Cards: " + openCards);
        pot.setText("pot: " + gameInfo.getPot());
        if (gameInfo.getBettingRules().someoneRaised() && gameInfo.getCurrentPlayer().getActiveBet() != gameInfo.getBettingRules().getLatestBet()) {
            call.setEnabled(true);
        } else {
            call.setEnabled(false);
        }
        if (gameInfo.getCurrentPlayer().getActiveBet() == gameInfo.getBettingRules().getLatestBet()) {
            check.setEnabled(true);
            fold.setEnabled(false);
        } else {
            check.setEnabled(false);
            fold.setEnabled(true);
        }
        call.setText("call: " + (gameInfo.getBettingRules().getLatestBet() - gameInfo.getCurrentPlayer().getActiveBet()));

        component.repaint();
    }

    /*
    public void nextTextSquare(){
        Component focus = getFocusOwner();
	    if (focus.equals(ip)){
	       port.requestFocus();
	    }else if(focus.equals(port)){
	        connectToServer();
	    }
    }*/


    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(item)) {
            System.exit(0);
        } else if (source.equals(check)) {

            updateUi();
        } else if (source.equals(fold)) {

        } else if (source.equals(raise)) {
            int amount;
            do {
                String input = JOptionPane.showInputDialog("Ammount to bet: ");
                amount = Integer.parseInt(input);
            } while (gameInfo.getCurrentPlayer().getChips() - amount < 0);
            if (gameInfo.getBettingRules().isLegalRaise(amount + gameInfo.getCurrentPlayer().getActiveBet())) {

                updateUi();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ammount");
            }
        } else if (source.equals(call)) {

            updateUi();
        } else if (source.equals(allIn)) {

            updateUi();
        } else if (source.equals(connectComponent.getConnect())){
            connectToServer();

        } else if (source.equals(addPlayerComponent.getAddPlayer())){
            client.getOut().println("NEWPLAYER&" + addPlayerComponent.getPlayerName().getText());

        } else if (source.equals(lobbyComponent.getStartGame())){
            client.getOut().println("STARTGAME");

        }/* else if (source.equals()){

        } else if (source.equals()){

        }*/
    }

    public boolean isConnected() {
        return connected;
    }

    public ConnectToServerComponent getConnectComponent() {
        return connectComponent;
    }

    public AddPlayerComponent getAddPlayerComponent() {
        return addPlayerComponent;
    }

    public LobbyComponent getLobbyComponent() {
        return lobbyComponent;
    }
}


