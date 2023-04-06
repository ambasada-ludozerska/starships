package starships;

import starships.entities.Battlecruiser;
import starships.entities.GameMap;
import starships.ui.GameWindow;
import starships.ui.PlayerController;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
        GameMap map = new GameMap("default", 1000, 1000);
        Battlecruiser npc = new Battlecruiser(new Point(600, 600));
        Battlecruiser battlecruiser = new Battlecruiser(new Point(100, 300));
        PlayerController player = new PlayerController(battlecruiser);
        map.addShip(battlecruiser);
        map.addShip(npc);
        map.addPlayer(player);
        GameWindow gameWindow = new GameWindow(map);

        Timer mainGameLoop = new Timer();
        mainGameLoop.schedule(new TimerTask() {
            @Override
            public void run() {
                map.checkOutOfBounds();
                map.checkCollisions();
                map.getPlayer().performActions();
                gameWindow.repaint();
            }}, 1000, 33); //around 30 ticks per second assuming it doesn't slow down
    }
}
