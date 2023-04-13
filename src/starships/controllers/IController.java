package starships.controllers;

import starships.entities.Ship;
import starships.ui.ActionHandler;

import java.util.HashMap;

public interface IController {
    HashMap<ActionHandler.Action, Boolean> activeActions = new HashMap<>();
    int getTeam();
    Ship getShip();
    void setShip(Ship ship);

    default void selectAction(ActionHandler.Action action) {
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
    void performActions();
}
