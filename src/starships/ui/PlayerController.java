package starships.ui;

import starships.entities.Ship;

import java.awt.*;
import java.util.HashMap;

import static starships.entities.IMovable.turningDirections.LEFT;
import static starships.entities.IMovable.turningDirections.RIGHT;

public class PlayerController {
    private Ship playerShip;
    private final HashMap<ActionHandler.Action, Boolean> activeActions = new HashMap<>(); //stores all actions that should be performed next tick

    public Ship getPlayerShip() {
        return this.playerShip;
    }
    public void setPlayerShip(Ship ship) {
        this.playerShip = ship;
    }

    public void selectAction(ActionHandler.Action action) {
        if(action == ActionHandler.Action.MOVE_FORWARD || action == ActionHandler.Action.TURN_LEFT || action == ActionHandler.Action.TURN_RIGHT) {
            activeActions.put(action, true); //when a key is pressed, add the associated action to the list of actions waiting to be performed, where it stays until released
        } else {
            switch(action) { //when the key is released, remove the associated action from the list of actions waiting to be performed
                case STOP_MOVE_FORWARD -> activeActions.remove(ActionHandler.Action.MOVE_FORWARD);
                case STOP_TURN_LEFT -> activeActions.remove(ActionHandler.Action.TURN_LEFT);
                case STOP_TURN_RIGHT -> activeActions.remove(ActionHandler.Action.TURN_RIGHT);
            }
        }
    }
    public void performActions() { //goes through all the actions and performs them every time it is called (every tick)
        for (ActionHandler.Action a : activeActions.keySet()) {
            switch(a) {
                case MOVE_FORWARD -> getPlayerShip().moveForward();
                case TURN_LEFT -> getPlayerShip().turn(LEFT);
                case TURN_RIGHT -> getPlayerShip().turn(RIGHT);
            }
        }
    }
    public void selectAction(Point location, int button) { //buttons: 1 - LMB, 2 - Scroll, 3 - RMB
        System.out.println("Clicked location: " + location + ", button: " + button);
    }

    public PlayerController(Ship ship) {
        this.setPlayerShip(ship);
    }
}
