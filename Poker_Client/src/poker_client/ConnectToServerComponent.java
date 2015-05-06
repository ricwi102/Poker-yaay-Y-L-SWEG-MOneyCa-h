package poker_client;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;


/**
 * Component which lets the user write in IP, port, and player name to connect to a server.
 *
 * @author Johannes Palm Myllylä, Richard Wigren
 * @version 1.0
 */

public class ConnectToServerComponent extends JComponent{

    private JButton connect = new JButton("Connect to server");
    private JTextField ip = new JTextField(10*2);
    private JTextField port = new JTextField(10);
    private JTextField nameField = new JTextField(2*10);

    public ConnectToServerComponent(ActionListener listener, KeyListener keyAdapter) {

        connect.addActionListener(listener);

        ip.addKeyListener(keyAdapter);
        port.addKeyListener(keyAdapter);
        nameField.addKeyListener(keyAdapter);

        setLayout(new FlowLayout());
    }

    public void startComponent(){
        final int ySpacing = 15;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("IP"));
        panel.add(ip);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(new JLabel("Port"));
        panel.add(port);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(new JLabel("Player Name"));
        panel.add(nameField);
        panel.add(Box.createRigidArea(new Dimension(5,ySpacing)));
        panel.add(connect);

        this.add(panel);
    }

    public String getIpText() {
        return ip.getText();
    }

    public String getPortText() {
        return port.getText();
    }

    public JTextField getIp() { return ip; }

    public JTextField getPort() { return port; }

    public JTextField getNameField() { return nameField; }

    public JButton getConnect() {
        return connect;
    }

    public String getPlayerNameText(){ return nameField.getText();}

}
