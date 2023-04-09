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
        Battlecruiser battlecruiser = new Battlecruiser(new Point(100, 300));
        Battlecruiser npc = new Battlecruiser(new Point(600, 600));
        Battlecruiser npc2 = new Battlecruiser(new Point(400, 100)); //for some reason rotation of the last created ship is synchronized with player's and around player's center???
        Battlecruiser dummy = new Battlecruiser(new Point(-100, -100)); //TODO - Actually fix the ghost rotation instead of creating an additional ship and sacrificing it to the bugs
        PlayerController player = new PlayerController(battlecruiser);
        map.addShip(battlecruiser);
        map.addShip(npc);
        map.addShip(npc2);
        map.addPlayer(player);
        GameWindow gameWindow = new GameWindow(map);

        Timer mainGameLoop = new Timer();
        mainGameLoop.schedule(new TimerTask() {
            @Override
            public void run() {
                map.updateProjectiles();
                map.checkOutOfBounds();
                map.checkCollisions();
                map.getPlayer().performActions();
                gameWindow.repaint();
                System.out.println(map.getAllShips());

            }}, 1000, 33); //around 30 ticks per second assuming it doesn't slow down
    }
}
