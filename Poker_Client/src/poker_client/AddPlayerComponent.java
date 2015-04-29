package poker_client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Richard on 2015-04-26.
 */
public class AddPlayerComponent extends JComponent{

    private JLabel name;
    private JTextField playerName;
    private JButton addPlayer;

    public AddPlayerComponent(ClientListener listener) {
        name = new JLabel("Name");
        playerName = new JTextField(2*10);
        addPlayer = new JButton("Join");

        addPlayer.addActionListener(listener);

        setLayout(new FlowLayout());

    }

    public void startComponent(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(name);
        panel.add(playerName);
        panel.add(Box.createRigidArea(new Dimension(10,10)));
        panel.add(addPlayer);

        this.add(panel);

    }


    public JButton getAddPlayer() {
        return addPlayer;
    }

    public JTextField getPlayerName() { return playerName; }
}
