package internet_test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class TestFrame extends JFrame implements ActionListener{

    private JTextArea textArea;
    private ServerExempel server;
    private JMenuItem shutdown;


    public TestFrame(ServerExempel server) throws HeadlessException {
	super("Test server");
	this.server = server;

	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	JMenuBar menuBar = new JMenuBar();
    	JMenu menu = new JMenu("Options");
	shutdown = new JMenuItem("Shutdown server");
	shutdown.addActionListener(this);

	textArea = new JTextArea(2*10, 2*10);
	textArea.setEditable(false);

	menu.add(shutdown);
	menuBar.add(menu);
	panel.add(textArea);

	setJMenuBar(menuBar);
	this.add(panel);
    }

    public synchronized void updateTextSquare(String text){
	textArea.append(text + "\n");
    }


    @Override public void actionPerformed(final ActionEvent e) {
    	Object source = e.getSource();

    	if (source.equals(shutdown)){
	    server.shutdown();
        }
    }
}



