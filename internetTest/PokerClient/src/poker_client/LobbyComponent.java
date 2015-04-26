package poker_client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Richard on 2015-04-23.
 */

public class LobbyComponent extends JComponent{

    private JLabel playersInLobby;
    private JButton startGame;
    private JPanel panel;


    public LobbyComponent(ClientListener listener) {

        playersInLobby = new JLabel();
        startGame = new JButton("Start Game");

        startGame.addActionListener(listener);

        setLayout(new FlowLayout());
    }

    public void startComponent() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(playersInLobby);
        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        {

            this.add(panel);
        }
    }

    public void startComponentHost(){
        startComponent();
        panel.add(startGame);

    }

    public void updatePlayersInLobby(List<Player> players){
        StringBuilder builder = new StringBuilder();
        for (Player player : players) {
            builder.append("<html>");
            builder.append(player.getName());
            builder.append("<br>");
        }
        builder.append("</htlm>");
        playersInLobby.setText(builder.toString());
    }

    public JButton getStartGame() {
        return startGame;
    }
}
