package starships.controllers;

import starships.entities.Ship;
import starships.ui.ActionHandler;

import java.awt.*;
import java.util.HashMap;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static starships.entities.IMovable.turningDirections.LEFT;
import static starships.entities.IMovable.turningDirections.RIGHT;

public class PlayerController implements IController {
    private Ship playerShip;
    private final HashMap<ActionHandler.Action, Boolean> activeActions = new HashMap<>(); //stores all actions that should be performed next tick

    private Point targetPos;
    double targetAngle;

    @Override
    public Ship getShip() {
        return this.playerShip;
    }
    @Override
    public void setShip(Ship ship) {
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
        if(getShip().isOperational()) {
            for (ActionHandler.Action a : activeActions.keySet()) {
                switch (a) {
                    case MOVE_FORWARD -> getShip().moveForward();
                    case TURN_LEFT -> getShip().turn(LEFT);
                    case TURN_RIGHT -> getShip().turn(RIGHT);
                    case FIRE_PRIMARY -> {
                        targetAngle = calculateTargetAngle(targetPos);
                        getShip().fire(targetAngle, 1);
                    }
                    case FIRE_SECONDARY -> {
                        targetAngle = calculateTargetAngle(targetPos);
                        getShip().fire(targetAngle, 3);
                    }
                }
            }
        }
    }
    public void selectAction(int button, boolean released) { //buttons: 1 - LMB, 2 - Scroll, 3 - RMB
        if(!released) {
            switch (button) {
                case 1 -> this.activeActions.put(ActionHandler.Action.FIRE_PRIMARY, true);
                case 3 -> this.activeActions.put(ActionHandler.Action.FIRE_SECONDARY, true);
            }
        } else {
            switch (button) {
                case 1 -> this.activeActions.remove(ActionHandler.Action.FIRE_PRIMARY);
                case 3 -> this.activeActions.remove(ActionHandler.Action.FIRE_SECONDARY);
            }
        }
    }

    public PlayerController(Ship ship) {
        this.setShip(ship);
    }

    public void updateTargetPosition(Point targetPos) {
        this.targetPos = targetPos;
    }
    public double calculateTargetAngle(Point targetPos) {
        double angle = Math.toDegrees(atan2(getShip().getCenter().getX() - targetPos.getX(), getShip().getCenter().getY() - targetPos.getY()));
        if(angle < 0) {
            return abs(angle);
        } else if (angle > 0) {
            return 360 - angle;
        } else {
            return angle;
        }
    }
}
