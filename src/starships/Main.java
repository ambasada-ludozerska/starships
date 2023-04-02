package starships;

import starships.entities.Battlecruiser;
import starships.ui.GameWindow;
import starships.ui.PlayerController;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        Battlecruiser battlecruiser = new Battlecruiser(new Point(100, 300));
        PlayerController player = new PlayerController(battlecruiser);
        GameWindow gameWindow = new GameWindow(player);
    }
}
