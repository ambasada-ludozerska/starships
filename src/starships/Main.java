package starships;

import starships.controllers.AIController;
import starships.entities.Battlecruiser;
import starships.entities.GameMap;
import starships.ui.GameWindow;
import starships.controllers.PlayerController;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
        GameMap map = new GameMap("default", 1920, 1080);
        Battlecruiser battlecruiser = new Battlecruiser(new Point(100, 300), 90);
        Battlecruiser enemy = new Battlecruiser(new Point(1200, 300), 270);
        Battlecruiser enemy2 = new Battlecruiser(new Point(1200, 700), 270);
        Battlecruiser friendly = new Battlecruiser(new Point(700, 700), 90);
        //TODO - Actually fix the ghost rotation instead of creating an additional ship and sacrificing it to the bugs DONE?

        PlayerController player = new PlayerController(battlecruiser, 0);
        AIController ai = new AIController(enemy, 1);
        AIController ai2 = new AIController(enemy2, 1);
        AIController ai3 = new AIController(friendly, 0);

        map.addShip(player);
        map.addShip(ai);
        map.addShip(ai2);
        map.addShip(ai3);

        map.setLocalPlayer(player);
        map.findAllAIs();

        GameWindow gameWindow = new GameWindow(map);

        Timer mainGameLoop = new Timer();
        mainGameLoop.schedule(new TimerTask() {
            @Override
            public void run() {
                map.updateProjectiles();
                map.getLocalPlayer().performActions();
                map.updateAIs();
                map.checkOutOfBounds();
                map.checkCollisions();
                gameWindow.repaint();

            }}, 1000, 33); //around 30 ticks per second assuming it doesn't slow down
    }
}
