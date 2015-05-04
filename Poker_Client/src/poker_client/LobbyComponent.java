package poker_client;

import poker.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * Created by Richard on 2015-04-23.
 */

public class LobbyComponent extends JComponent{

    private JLabel playersInLobby;
    private JLabel gameMode;
    private JLabel betRules;
    private JButton startGame;
    private JComboBox<String> gameOptions;
    private JComboBox<String> betOptions;
    private static final String[] GAME_MODES = {"Texas Hold'em", "Omaha"};
    private static final String[] BET_RULES = {"No Limit", "Pot Limit"};



    public LobbyComponent(final ActionListener listener) {

        playersInLobby = new JLabel();
        gameMode = new JLabel("Texas Hold'em");
        betRules = new JLabel("No Limit");
        startGame = new JButton("Start Game");
        gameOptions = new JComboBox<>(GAME_MODES);
        betOptions = new JComboBox<>(BET_RULES);

        startGame.addActionListener(listener);
        gameOptions.addActionListener(listener);
        betOptions.addActionListener(listener);

        setLayout(new FlowLayout());
        //setPreferredSize(new Dimension(500, 500));
    }

    public void startComponent() {
        createPanel();
        final int spacing = 40;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(gameMode);
        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        panel.add(betRules);

        this.add(Box.createRigidArea(new Dimension(spacing, spacing)));
        this.add(panel);

    }

    public void startComponentHost(){
        createPanel();
        final int spacing = 40;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(gameOptions);
        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        panel.add(betOptions);
        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        panel.add(startGame);

        this.add(Box.createRigidArea(new Dimension(spacing, spacing)));
        this.add(panel);

    }

    public void createPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(playersInLobby);
        this.add(panel);
    }

    public void updatePlayersInLobby(Iterable<Player> players){
        StringBuilder builder = new StringBuilder();
        for (Player player : players) {
            builder.append("<html>");
            builder.append(player.getName());
            builder.append("<br>");
        }
        builder.append("</htlm>");
        playersInLobby.setText(builder.toString());
    }

    public void setGameMode(String gameMode, boolean host){
        if (host){
            if (gameMode.equals("OMAHA")){
                gameOptions.setSelectedItem(1);
            } else {
                gameOptions.setSelectedItem(0);
            }
        } else {
            if (gameMode.equals("OMAHA")){
                this.gameMode.setText("Omaha");
            } else {
                this.gameMode.setText("Texas Hold'em");
            }
        }
    }

    public void setBetRules(String betRules, boolean host){
        if (host){
            if (betRules.equals("POTLIMIT")){
                betOptions.setSelectedItem(1);
            } else {
                betOptions.setSelectedItem(0);
            }
        } else {
            if (betRules.equals("POTLIMIT")){
                this.betRules.setText("Pot Limit");
            } else {
                this.betRules.setText("No Limit");
            }
        }
    }

    public JButton getStartGame() {
        return startGame;
    }

    public JComboBox<String> getGameOptions() {
        return gameOptions;
    }

    public JComboBox<String> getBetOptions() {
        return betOptions;
    }


}
