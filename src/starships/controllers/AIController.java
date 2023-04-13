package starships.controllers;

import starships.entities.Ship;
import starships.equipment.Weapon;
import starships.ui.ActionHandler;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.*;
import static starships.entities.IMovable.turningDirections.LEFT;
import static starships.entities.IMovable.turningDirections.RIGHT;

public class AIController implements IController {
    private final int team;
    private Ship ship;
    private Ship target;
    private final ArrayList<Weapon> weaponsInRange = new ArrayList<>();
    private final HashMap<ActionHandler.Action, Boolean> activeActions = new HashMap<>();

    @Override
    public int getTeam() {
        return this.team;
    }

    @Override
    public Ship getShip() {
        return this.ship;
    }

    @Override
    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void designateTarget(Ship target) {
        if(target != this.getShip()) {
            this.target = target;
        }
    }
    public void updateWeaponsInRange() {
        for (Weapon w : getShip().getWeapons()) {
            if(getShip().getCenter().distance(target.getCenter()) <= (double) w.getWeaponRange()) {
                if(!weaponsInRange.contains(w)) {
                    weaponsInRange.add(w);
                }
            }
        }
    }

    private double getAngleToTarget() {
        double angle = Math.toDegrees(atan2(getShip().getCenter().getX() - target.getCenter().getX(), getShip().getCenter().getY() - target.getCenter().getY()));
        if(angle < 0) {
            return abs(angle);
        } else if (angle > 0) {
            return 360 - angle;
        } else {
            return angle;
        }
    }
    private void engageTarget() {
        activeActions.remove(ActionHandler.Action.FIRE_PRIMARY);
        activeActions.remove(ActionHandler.Action.FIRE_SECONDARY);
        for (Weapon w : weaponsInRange) {
            if(w.getWeaponType() == Weapon.weaponType.PRIMARY) {
                activeActions.put(ActionHandler.Action.FIRE_PRIMARY, true);
            } else if(w.getWeaponType() == Weapon.weaponType.SECONDARY) {
                activeActions.put(ActionHandler.Action.FIRE_SECONDARY, true);
            }
        }
        weaponsInRange.clear();
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
                //System.out.println(a);
                switch (a) {
                    case MOVE_FORWARD -> getShip().moveForward();
                    case TURN_LEFT -> getShip().turn(LEFT);
                    case TURN_RIGHT -> getShip().turn(RIGHT);
                    case FIRE_PRIMARY -> getShip().fire(getAngleToTarget(), Weapon.weaponType.PRIMARY);
                    case FIRE_SECONDARY -> getShip().fire(getAngleToTarget(), Weapon.weaponType.SECONDARY);
                }
            }
        }
    }

    public void planAction(Ship target) {
        designateTarget(target);
        updateWeaponsInRange();
        engageTarget();
        selectAction(ActionHandler.Action.MOVE_FORWARD);
        selectAction(ActionHandler.Action.TURN_LEFT);
    }

    public AIController(Ship ship, int team) {
        this.setShip(ship);
        this.team = team;
    }
}
