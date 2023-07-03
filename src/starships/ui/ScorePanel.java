package starships.ui;

import starships.utility.gamemodes.GameMode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScorePanel extends JPanel {

    public ScorePanel(int victoriousTeam, GameMode.winTypes winType) {

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setVisible(true);
        if(victoriousTeam != -1) {
            this.add(new JLabel("Team " + victoriousTeam + " wins!"));
            this.add(new JLabel("Cause of victory: " + winType));
        } else {
            this.add(new JLabel("Draw!"));
            this.add(new JLabel("Cause of game end: " + winType));
        }
        JButton exitButton = new JButton("Exit game");
        exitButton.setActionCommand("exit");
        exitButton.addActionListener(new ExitListener());
        this.add(exitButton);
    }

    static class ExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
