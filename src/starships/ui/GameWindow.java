package starships.ui;

import javax.swing.*;

public class GameWindow extends JFrame {


    public GameWindow(PlayerController player) {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(new GamePanel(player));
        this.setVisible(true);
    }
}
