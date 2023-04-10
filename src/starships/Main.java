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
        GameMap map = new GameMap("default", 1000, 1000);
        Battlecruiser battlecruiser = new Battlecruiser(new Point(100, 300));
        Battlecruiser npc = new Battlecruiser(new Point(600, 600));
        //TODO - Actually fix the ghost rotation instead of creating an additional ship and sacrificing it to the bugs DONE?
        PlayerController player = new PlayerController(battlecruiser);
        AIController ai = new AIController(npc);
        map.addShip(player);
        map.addShip(ai);
        map.setLocalPlayer(player);
        map.findAllAIs();
        GameWindow gameWindow = new GameWindow(map);

        Timer mainGameLoop = new Timer();
        mainGameLoop.schedule(new TimerTask() {
            @Override
            public void run() {
                map.updateProjectiles();
                map.checkOutOfBounds();
                map.checkCollisions();
                map.updateAIs();
                map.getLocalPlayer().performActions();
                gameWindow.repaint();

            }}, 1000, 33); //around 30 ticks per second assuming it doesn't slow down
    }
}
