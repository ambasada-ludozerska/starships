package starships;

import starships.controllers.AIController;
import starships.controllers.PlayerController;
import starships.entities.Battlecruiser;
import starships.entities.Destroyer;
import starships.entities.GameMap;
import starships.ui.GamePanel;
import starships.ui.GameWindow;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class GameInit {
    private final GameWindow window;

    private final PlayerController player = new PlayerController(0);

    public enum shipClasses {BC, DD}

    private shipClasses playerShipClass;

    public void selectPlayerShip(shipClasses shipClass) {
        switch (shipClass) {
            case BC -> this.playerShipClass = shipClasses.BC;
            case DD -> this.playerShipClass = shipClasses.DD;
        }
    }

    public void launch() {
        if(this.playerShipClass != null) {
            switch (this.playerShipClass) {
                case BC -> {
                    Battlecruiser playerBattlecruiser = new Battlecruiser(new Point(200, 200), 90);
                    player.setShip(playerBattlecruiser);
                }
                case DD -> {
                    Destroyer playerDestroyer = new Destroyer(new Point(200, 200), 90);
                    player.setShip(playerDestroyer);
                }
            }
        } else {
            Battlecruiser playerBattlecruiser = new Battlecruiser(new Point(200, 200), 90);
            player.setShip(playerBattlecruiser);
        }

        GameMap map = new GameMap("default", 1920, 1080);
        Battlecruiser enemy = new Battlecruiser(new Point(1700, 800), 270);
        //Battlecruiser enemy2 = new Battlecruiser(new Point(700, 700), 270);
        //Battlecruiser friendly = new Battlecruiser(new Point(200, 700), 90);

        AIController ai = new AIController(enemy, 1);
        //AIController ai2 = new AIController(enemy2, 1);
        //AIController ai3 = new AIController(friendly, 0);

        map.addShip(player);
        map.addShip(ai);
        //map.addShip(ai2);
        //map.addShip(ai3);

        map.setLocalPlayer(player);
        map.findAllAIs();

        System.out.println(window.getContentPane().getLayout());
        GamePanel gamePanel = new GamePanel(map);
        window.add(gamePanel);
        CardLayout windowLayout = (CardLayout) window.getContentPane().getLayout();
        windowLayout.last(window.getContentPane());
        gamePanel.requestFocusInWindow();

        Timer mainGameLoop = new Timer();
        mainGameLoop.schedule(new TimerTask() {
            @Override
            public void run() {
                //map.updateProjectiles();
                map.getLocalPlayer().performActions();
                map.updateAIs();
                map.updatePositions();
                map.checkOutOfBounds();
                map.checkCollisions();
                window.repaint();

            }}, 1000, 33); //around 30 ticks per second assuming it doesn't slow down
    }

    public GameInit(GameWindow window) {
        this.window = window;
    }
}
