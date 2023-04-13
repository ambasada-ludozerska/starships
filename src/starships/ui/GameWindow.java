package starships.ui;

import starships.entities.GameMap;

import javax.swing.*;

public class GameWindow extends JFrame {


    public GameWindow(GameMap map) {
        //this.setSize(1920, 1080);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(new GamePanel(map));
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setUndecorated(false);
        this.setVisible(true);
    }
}
