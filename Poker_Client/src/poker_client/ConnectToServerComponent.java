package poker_client;



import javax.swing.*;
import java.awt.*;

/**
 * Created by Richard on 2015-04-23.
 */
public class ConnectToServerComponent extends JComponent{

    private JButton connect;
    private JTextField ip;
    private JTextField port;
    private JTextField name;

    public ConnectToServerComponent(ClientListener listener, ClientKeyAdapter keyAdapter) {

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
        panel.add(Box.createRigidArea(new Dimension(5,15)));
        panel.add(connect);

        this.add(panel);

        //setPreferredSize(new Dimension(500, 500));
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

    public JTextField getIp() {
        return ip;
    }

    public JTextField getPort() {
        return port;
    }
}
