package client_test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class TestFrame extends JFrame implements ActionListener{
    private JButton button;
    private JTextArea receive;
    private JTextArea send;
    private ClientExempel client;


    public TestFrame(final ClientExempel client) throws HeadlessException {
	super("Test client");
	this.client = client;

	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


	receive = new JTextArea(2*10, 2*10);
	send = new JTextArea(1, 2*10);
	button = new JButton("Send text");

	button.addActionListener(this);

	panel.add(receive);
	panel.add(send);
	panel.add(button);

	this.add(panel);
    }

    public synchronized void updateTextSquare(String text){
    	receive.append(text + "\n");
    }


    @Override public void actionPerformed(final ActionEvent e) {
	Object source = e.getSource();

	if (source.equals(button)){
	    String text = send.getText();
	    client.getOut().println(text);
	    send.setText("");
	}
	/*try{
	    String line = client.getIn().readLine();
	    receive.append(line + "\n");
	    System.out.println("Text received: " + line);
	} catch (IOException exception){
	    System.out.println("Read failed");
	    System.exit(1);
	}*/
    }
}
