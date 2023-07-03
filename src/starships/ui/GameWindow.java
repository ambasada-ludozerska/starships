package starships.ui;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {


    public GameWindow() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        CardLayout layout = new CardLayout();
        this.setLayout(layout);
        SetupPanel setup = new SetupPanel();
        this.add(setup);
        setup.init();
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setUndecorated(false);
        this.setVisible(true);
    }
}
