package starships.ui;

import starships.entities.GameMap;

import javax.swing.*;

public class GameWindow extends JFrame {


    public GameWindow(GameMap map) {
        this.setSize(1000, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(new GamePanel(map));
        this.setVisible(true);
    }
}
