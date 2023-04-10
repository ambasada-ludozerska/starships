package starships.controllers;

import starships.entities.Ship;
import starships.ui.ActionHandler;

import java.util.HashMap;

import static starships.entities.IMovable.turningDirections.LEFT;
import static starships.entities.IMovable.turningDirections.RIGHT;

public class AIController implements IController {
    private Ship ship;
    private final HashMap<ActionHandler.Action, Boolean> activeActions = new HashMap<>();
    @Override
    public Ship getShip() {
        return this.ship;
    }

    @Override
    public void setShip(Ship ship) {
        this.ship = ship;
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

    @Override
    public void performActions() {
        if(getShip().isOperational()) {
            for (ActionHandler.Action a : activeActions.keySet()) {
                switch (a) {
                    case MOVE_FORWARD -> getShip().moveForward();
                    case TURN_LEFT -> getShip().turn(LEFT);
                    case TURN_RIGHT -> getShip().turn(RIGHT);
                }
            }
        }
    }

    public void planAction() {
        selectAction(ActionHandler.Action.MOVE_FORWARD);
        selectAction(ActionHandler.Action.TURN_LEFT);
    }

    public AIController(Ship ship) {
        this.setShip(ship);
    }
}
