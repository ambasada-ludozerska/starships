package starships.controllers;

import starships.entities.Entity;
import starships.entities.Ship;
import starships.equipment.Weapon;
import starships.ui.ActionHandler;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.*;
import static starships.entities.IMovable.turningDirections.LEFT;
import static starships.entities.IMovable.turningDirections.RIGHT;

public class AIController implements IController {
    //IFF
    private final int team;
    //for now always the closest non-friendly ship, static objects are currently indestructible and untargetable (and not fully implemented)
    private Ship target;

    //controlled ship
    private Ship ship;


    //stores all ships close enough to pose a collision hazard, not yet completely sure how effective this is with multiple ships though UPDATE: it's not.
    private final ArrayList<Entity> onCollisionCourseWith = new ArrayList<>();

    private final ArrayList<Weapon> weaponsInRange = new ArrayList<>(); //stores weapons that can effectively fire at the target
    private final HashMap<ActionHandler.Action, Boolean> activeActions = new HashMap<>(); //stores all actions queued to be performed, why is this even a hashmap instead of an array?

    //IFF AND TARGETING
    @Override
    public int getTeam() {
        return this.team;
    } //IFF

    @Override
    public Ship getShip() {
        return this.ship;
    }

    @Override
    public void setShip(Ship ship) {
        this.ship = ship;
    } //setup, assign the AI to a ship on the map

    public void designateTarget(Ship target) { //self-explanatory
        if(target != this.getShip()) {
            this.target = target;
        }
    }
    private double getAngleTo(Entity target) { //return an angle from 0 to 360 between the Y axis and the line drawn to target's center
        double angle = Math.toDegrees(atan2(this.ship.getPos().getX() - target.getPos().getX(), this.ship.getPos().getY() - target.getPos().getY()));
        if(angle < 0) {
            return abs(angle);
        } else if (angle > 0) {
            return 360 - angle;
        } else {
            return angle;
        }
    }

    //MOVEMENT

    private void intercept() { //turn to face target and move closer until in collision avoidance range
        double angle = getAngleTo(target);
        if(abs(getShip().getFacing() - angle) > 3 && abs(angle - getShip().getFacing()) < 357) {
            if (angle > getShip().getFacing()) {
                if (angle - ship.getFacing() > 180) {
                    selectAction(ActionHandler.Action.TURN_LEFT);
                } else {
                    selectAction(ActionHandler.Action.TURN_RIGHT);
                }
            } else if (angle < getShip().getFacing()) {
                if (getShip().getFacing() - angle > 180) {
                    selectAction(ActionHandler.Action.TURN_RIGHT);
                } else {
                    selectAction(ActionHandler.Action.TURN_LEFT);
                }
            }
        }
        selectAction(ActionHandler.Action.MOVE_FORWARD);
    }

    //COLLISION AVOIDANCE
    public void onCollisionCourse(Entity hazard) { //self-explanatory, exists to allow map's collision detection loop to interact with the AI
        onCollisionCourseWith.add(hazard);
    }
    private void avoidCollision(Entity hazard) { //turn away from the collision course, stops when the ships won't get any closer on current trajectories
        double angle = getAngleTo(hazard);
        if(angle >= getShip().getFacing()) {
            if(angle - getShip().getFacing() < 90) {
                selectAction(ActionHandler.Action.TURN_LEFT);
            } else if(angle - getShip().getFacing() > 270) {
                selectAction(ActionHandler.Action.TURN_RIGHT);
            }
        } else if(angle < getShip().getFacing()) {
            if(getShip().getFacing() - angle < 90) {
                selectAction(ActionHandler.Action.TURN_RIGHT);
            } else if(getShip().getFacing() > 270) {
                selectAction(ActionHandler.Action.TURN_LEFT);
            }
        }
    }


    //FIRING
    public void updateWeaponsInRange() { //check which weapons can fire at the target and have a chance of hitting
        for (Weapon w : getShip().getWeapons()) {
            if(getShip().getPos().distance(target.getPos()) <= (double) w.getWeaponRange()) {
                if(!weaponsInRange.contains(w)) {
                    weaponsInRange.add(w);
                }
            }
        }
    }
    private void engageTarget() { //FIRE ALL GUNS!
        activeActions.remove(ActionHandler.Action.FIRE_PRIMARY);
        activeActions.remove(ActionHandler.Action.FIRE_SECONDARY);
        for (Weapon w : weaponsInRange) {
            if(w.getWeaponType() == Weapon.weaponType.PRIMARY) {
                activeActions.put(ActionHandler.Action.FIRE_PRIMARY, true);
            } else if(w.getWeaponType() == Weapon.weaponType.SECONDARY) {
                activeActions.put(ActionHandler.Action.FIRE_SECONDARY, true);
            }
        }
        weaponsInRange.clear(); //but don't keep them firing unnecessarily when they get out of range
    }


    //LOGIC
    public void selectAction(ActionHandler.Action action) { //not really necessary in the AI as the logic is being computed in planActions(), leftover from the interface
        activeActions.put(action, true);
    }

    @Override
    public void performActions() { //perform all the actions queued for this tick
        if(getShip().isOperational()) {
            for (ActionHandler.Action a : activeActions.keySet()) {
                switch (a) {
                    case MOVE_FORWARD -> getShip().accelerate();
                    case TURN_LEFT -> getShip().turn(LEFT);
                    case TURN_RIGHT -> getShip().turn(RIGHT);
                    case FIRE_PRIMARY -> getShip().fire(getAngleTo(target), Weapon.weaponType.PRIMARY);
                    case FIRE_SECONDARY -> getShip().fire(getAngleTo(target), Weapon.weaponType.SECONDARY);
                }
            }
            activeActions.clear(); //and clear the queue when done to not interfere with the following decisions
        }
    }

    public void planAction(Ship target) {
        designateTarget(target); //targets the closest non-friendly ship
        if(!onCollisionCourseWith.isEmpty()) { //if there's no collision hazard, chase the target
            for(Entity e : onCollisionCourseWith) {
                avoidCollision(e);
                selectAction(ActionHandler.Action.MOVE_FORWARD);
            }
            onCollisionCourseWith.clear();
        } else {
            intercept();
        }
        if(target.isOperational()) { //fire at the target, shouldn't fire at wrecks but still happens if there are no more operational ships left due to how targeting logic works
            updateWeaponsInRange();
            engageTarget();
        }
    }


    //CONSTRUCTOR
    public AIController(Ship ship, int team) {
        this.setShip(ship);
        this.team = team;
    }
}
