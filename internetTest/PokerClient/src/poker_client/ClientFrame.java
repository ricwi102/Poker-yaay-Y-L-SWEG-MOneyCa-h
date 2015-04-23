package poker_client;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class ClientFrame extends JFrame implements ActionListener
{
    private Client client;
    private ClientKeyAdapter keyAdapter;
    private GameInfo gameInfo;
    private JMenuItem item;
    private JButton check;
    private JButton fold;
    private JButton raise;
    private JButton call;
    private JButton allIn;
    private JButton connect;
    private JButton startGame;
    private JLabel communityCards;
    private JLabel pot;
    private JTextField ip;
    private JTextField port;
    private ClientComponent component;

    public ClientFrame(final GameInfo gameInfo, Client client) throws HeadlessException, IOException{
	super("Pokr sweg, holdum YÅLÅ");
	this.gameInfo = gameInfo;
	this.client = client;
	component = new ClientComponent(gameInfo);
	keyAdapter = new ClientKeyAdapter(this);
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	this.setLayout(new BorderLayout());
	createButtons();
	createMenu();
	//displayBoard();
    }
/*
    private void displayBoard(){
	component.setPreferredSize(component.getPreferedSize());
	this.add(component,BorderLayout.PAGE_START);
    }*/

    private void createButtons(){


	check = new JButton();
	fold = new JButton();
	call = new JButton();
	raise = new JButton();
	allIn = new JButton();
	connect = new JButton("Connect to server");
	startGame = new JButton("Start Game");


	check.addActionListener(this);
	fold.addActionListener(this);
	call.addActionListener(this);
	raise.addActionListener(this);
	allIn.addActionListener(this);
	connect.addActionListener(this);
	startGame.addActionListener(this);

	communityCards = new JLabel();

	ip = new JTextField(2*10);
	port = new JTextField(10);

	ip.addKeyListener(keyAdapter);
	port.addKeyListener(keyAdapter);

	pot = new JLabel();

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



    public void startGame(){
	this.removeAll();

   	JPanel panel = new JPanel();
   	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


   	StringBuilder openCards = new StringBuilder();
   	for(Card card : gameInfo.getBoard().getOpenCards()){
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

    public void lobbyFrame(){
	Container cont = getContentPane();
	cont.removeAll();

	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	//if (client.isHost()){
	panel.add(startGame);
	//}

	cont.add(panel, BorderLayout.PAGE_END);



	cont.doLayout();
	cont.update(getGraphics());



    }

    public void connectToServerFrame(){
	Container cont = getContentPane();
	cont.removeAll();

	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	panel.add(ip);
	panel.add(Box.createRigidArea(new Dimension(5,5)));
	panel.add(port);
	panel.add(Box.createRigidArea(new Dimension(5,5)));
	panel.add(connect);

	cont.add(panel, BorderLayout.PAGE_END);


	cont.doLayout();
	cont.update(getGraphics());

    }


    private void connectToServer() {
	int serverPort = Integer.parseInt(port.getText());

	try {
	    InetAddress address = InetAddress.getByName(ip.getText());
	    client.listenSocket(serverPort, address);
	    lobbyFrame();
	    client.read(this);

	} catch (UnknownHostException exc){
	    System.err.println("UnknownHostException: " + exc.getMessage());
	    JOptionPane.showMessageDialog(null, "Unknown Host");
	}

    }

    private void updateUi(){
	StringBuilder openCards = new StringBuilder();
	for(Card card : gameInfo.getBoard().getOpenCards()){
	    openCards.append(card);
	    openCards.append(" ");
	}


	communityCards.setText("Community Cards: " + openCards );
	pot.setText("pot: " + gameInfo.getPot());
	if(gameInfo.getBettingRules().someoneRaised() && gameInfo.getCurrentPlayer().getActiveBet() != gameInfo.getBettingRules().getLatestBet()){
	    call.setEnabled(true);
	}else{
	    call.setEnabled(false);
	}
	if(gameInfo.getCurrentPlayer().getActiveBet() == gameInfo.getBettingRules().getLatestBet()){
	    check.setEnabled(true);
	    fold.setEnabled(false);
	}else{
	    check.setEnabled(false);
	    fold.setEnabled(true);
	}
	call.setText("call: " + (gameInfo.getBettingRules().getLatestBet() - gameInfo.getCurrentPlayer().getActiveBet()));

	component.repaint();
    }

    public void nextTextSquare(){
	Component focus = getFocusOwner();
	if (focus.equals(ip)){
	    port.requestFocus();
	}else if(focus.equals(port)){
	    connectToServer();
	}
    }


    public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();
	if (source.equals(item)) {
	    System.exit(0);
	} else if (source.equals(check)){

	    updateUi();
	}else if(source.equals(fold)){

	}else if(source.equals(raise)) {
	    int amount;
	    do {
		String input = JOptionPane.showInputDialog("Ammount to bet: ");
		amount = Integer.parseInt(input);
	    } while (gameInfo.getCurrentPlayer().getChips() - amount < 0);
	    if (gameInfo.getBettingRules().isLegalRaise(amount + gameInfo.getCurrentPlayer().getActiveBet())){

		updateUi();
	    }else{
		JOptionPane.showMessageDialog(this,"Invalid ammount");
	    }
	}else if(source.equals(call)){

	    updateUi();
	}else if(source.equals(allIn)){

	    updateUi();
	}else if(source.equals(connect)){
	    connectToServer();

	}else if(source.equals(startGame)){
	    client.getOut().println("STARTGAME");
	}
    }
}
