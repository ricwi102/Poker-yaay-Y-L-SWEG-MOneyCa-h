package poker_client;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

/**
 * Created by Richard on 2015-04-23.
 */
public class ConnectToServerComponent extends JComponent{

    private JButton connect;
    private JTextField ip;
    private JTextField port;
    private JTextField name;

    public ConnectToServerComponent(ActionListener listener, KeyListener keyAdapter) {

        connect = new JButton("Connect to server");
        ip = new JTextField(10*2);
        port = new JTextField(10);
        name = new JTextField(2*10);

        connect.addActionListener(listener);

        ip.addKeyListener(keyAdapter);
        port.addKeyListener(keyAdapter);

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
        panel.add(name);
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

    public JButton getConnect() {
        return connect;
    }

    public String getPlayerNameText(){ return name.getText();}

}
