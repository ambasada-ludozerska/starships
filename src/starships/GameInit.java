package starships;

import starships.controllers.AIController;
import starships.controllers.PlayerController;
import starships.entities.Battlecruiser;
import starships.entities.Destroyer;
import starships.entities.GameMap;
import starships.ui.GamePanel;
import starships.ui.GameWindow;
import starships.ui.ScorePanel;
import starships.utility.gamemodes.GameModeSkirmish;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class GameInit {
    private final GameWindow window;

    private final PlayerController player = new PlayerController(0); //create local player

    public enum shipClasses {BC, DD}

    private shipClasses playerShipClass;

    public void selectPlayerShip(shipClasses shipClass) { //pre-game ship selection
        switch (shipClass) {
            case BC -> this.playerShipClass = shipClasses.BC;
            case DD -> this.playerShipClass = shipClasses.DD;
        }
    }

    public void launch() {
        if(this.playerShipClass != null) { //create and assign the chosen ship
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
        } else { //in case player didn't select anything, TODO - just make one of the options selected by default and lock the button
            Battlecruiser playerBattlecruiser = new Battlecruiser(new Point(200, 200), 90);
            player.setShip(playerBattlecruiser);
        }

        GameMap map = new GameMap("default", 1920, 1080);
        GameModeSkirmish gamemode = new GameModeSkirmish();
        Battlecruiser enemy = new Battlecruiser(new Point(1700, 800), 270);
        //Battlecruiser enemy2 = new Battlecruiser(new Point(700, 700), 270);
        //Battlecruiser friendly = new Battlecruiser(new Point(200, 700), 90);

        AIController ai = new AIController(enemy, 1);
        //AIController ai2 = new AIController(enemy2, 1);
        //AIController ai3 = new AIController(friendly, 0);

        map.addShip(player); //register the player ship on the map
        map.addShip(ai); //register the NPC ships on the map
        //map.addShip(ai2);
        //map.addShip(ai3);

        map.setLocalPlayer(player); //specify the local player
        map.findAllAIs(); //register all AIs for logic updates

        //System.out.println(window.getContentPane().getLayout());
        GamePanel gamePanel = new GamePanel(map); //set up the actual game environment
        window.add(gamePanel);
        CardLayout windowLayout = (CardLayout) window.getContentPane().getLayout();
        windowLayout.addLayoutComponent(gamePanel, "game");
        //windowLayout.last(window.getContentPane()); //switch the view from menu to game, TODO - improve the switching to something more reliable
        windowLayout.show(window.getContentPane(), "game");
        gamePanel.requestFocusInWindow(); //allow input handling

        Timer mainGameLoop = new Timer(); //start the main game loop
        mainGameLoop.schedule(new TimerTask() {
            @Override
            public void run() {
                //map.updateProjectiles();
                map.getLocalPlayer().performActions();
                map.updateAIs();
                map.updatePositions();
                map.checkOutOfBounds();
                map.checkCollisions();
                if(gamemode.checkWinCondition(map)) {
                    System.out.println("end");
                    ScorePanel scorePanel = gamemode.endGame();
                    window.add(scorePanel);
                    windowLayout.addLayoutComponent(scorePanel, "score");
                    windowLayout.show(window.getContentPane(), "score");
                    window.repaint();
                    cancel();
                }
                window.repaint();

            }}, 1000, 33); //around 30 ticks per second assuming it doesn't slow down
    }

    //CONSTRUCTOR
    public GameInit(GameWindow window) {
        this.window = window;
    }
}
