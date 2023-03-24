package warriors.ui;

import javax.swing.*;

public class GameWindow extends JFrame {


    public GameWindow() {
        this.setSize(1600, 1000);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(new GamePanel());
        this.add(new StatPanel());
        this.add(new ActionPanel());
        this.setVisible(true);
    }
}
