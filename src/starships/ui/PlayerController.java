package starships.ui;

import starships.entities.Ship;

import java.awt.*;

import static starships.entities.IMovable.turningDirections.LEFT;
import static starships.entities.IMovable.turningDirections.RIGHT;

public class PlayerController {
    private Ship playerShip;

    public Ship getPlayerShip() {
        return this.playerShip;
    }
    public void setPlayerShip(Ship ship) {
        this.playerShip = ship;
    }

    /*public void selectAction(int keycode) {
        switch (keycode) { //codes: 87 - w, 65 - a, 68 - d
            case 65 -> getPlayerShip().turn(LEFT);
            case 68 -> getPlayerShip().turn(RIGHT);
            case 87 -> getPlayerShip().moveForward();
        }
    }*/
    public void selectAction(ActionHandler.Action action) {
        switch(action) {
            case MOVE_FORWARD -> getPlayerShip().moveForward();
            case TURN_LEFT -> getPlayerShip().turn(LEFT);
            case TURN_RIGHT -> getPlayerShip().turn(RIGHT);
        }
    }
    public void selectAction(Point location, int button) { //buttons: 1 - LMB, 2 - Scroll, 3 - RMB
        System.out.println("Clicked location: " + location + ", button: " + button);
    }

    public PlayerController(Ship ship) {
        this.setPlayerShip(ship);
    }
}
