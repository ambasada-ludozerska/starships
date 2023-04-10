package starships.entities;

import starships.equipment.Weapon;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.*;

public abstract class Ship extends Entity implements IMovable {
    protected int forwardSpeed; //pixels per tick, sum of the separate X and Y changes
    protected double turningSpeed; //degrees per tick, to get degrees per second multiply by 30
    protected double facing = 0; //in degrees, value range: 0 - 359, might change coordinate system to double for higher precision?

    protected int currentHullIntegrity;
    protected int startingHullIntegrity;
    protected boolean isOperational = true;

    protected Weapon primaryWeapon;
    protected Weapon secondaryWeapon;

    protected ArrayList<Projectile> launchedProjectiles = new ArrayList<>();

    protected BufferedImage damagedModel; //currently not really showing damage, just notifies of a collision
    protected BufferedImage normalModel; //that's how it should look out of the box

    enum models {NORMAL, DAMAGED} //model states

    protected void changeModel(models newModel) { //switches between model states
        switch(newModel) {
            case NORMAL -> this.model = this.normalModel;
            case DAMAGED -> this.model = this.damagedModel;
        }
    }

    public int getRemainingHullIntegrity() {
        return this.currentHullIntegrity;
    }
    protected void setHullIntegrity(int newHullIntegrity) {
        this.currentHullIntegrity = Math.max(newHullIntegrity, 0);
    }
    public int getStartingHullIntegrity() {
        return this.startingHullIntegrity;
    }
    protected void setStartingHullIntegrity(int startingHullIntegrity) {
        if(startingHullIntegrity > 0) {
            this.startingHullIntegrity = startingHullIntegrity;
        } else {
            this.startingHullIntegrity = 1;
        }
    }
    public boolean isOperational() {
        return this.isOperational;
    }

    public ArrayList<Weapon> getWeapons() {
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(primaryWeapon);
        weapons.add(secondaryWeapon);
        return weapons;
    }
    public ArrayList<Projectile> getLaunchedProjectiles() {
        return this.launchedProjectiles;
    }
    public void wipeStoredProjectiles() {
        this.launchedProjectiles.clear();
    }

    @Override
    public double getFacing() { //self-explanatory
        return this.facing;
    }

    @Override
    public void setFacing(double newFacing) { //probably overcomplicated but it works so I'll just leave it
        if (newFacing < 0) { //what it actually does is make sure facing is always between 0 and 359 to avoid negative angles or whatever
            this.facing = newFacing + 360;
        } else if (newFacing > 360) {
            this.facing = newFacing - 360;
        } else if (newFacing == 360) {
            this.facing = 0;
        } else {
            this.facing = newFacing;
        }
    }

    @Override
    public void moveForward() { //assumes facing 0 is straight up
        this.setPos(
                (int) (this.getPos().getLocation().getX() +  this.forwardSpeed * sin(Math.toRadians(this.getFacing()))),
                (int) (this.getPos().getLocation().getY() - this.forwardSpeed * cos(Math.toRadians(this.getFacing())))
        );
        this.setCenter((int) (this.getPos().getX() + this.getSize()), (int) (this.getPos().getY() + this.getSize())); //sync actual pos and image corner
    }

    @Override
    public void turn(turningDirections direction) { //should be self-explanatory
        switch (direction) {
            case LEFT -> this.setFacing(this.getFacing() - turningSpeed);
            case RIGHT -> this.setFacing(this.getFacing() + turningSpeed);
        }
        this.primaryWeapon.setWeaponFacing(this.getFacing());
        this.primaryWeapon.updateFiringArc();
        this.secondaryWeapon.setWeaponFacing(this.getFacing());
        this.secondaryWeapon.updateFiringArc();
    }

    public void collide(MapObject m) { //handling collisions with static objects, affects only the ship
        this.destroy();
    }

    public void collide(Ship s) { //handling collisions with other ships, affects both ships
            this.destroy();
            s.destroy();
    }

    protected void takeDamage(int damage) {
        this.setHullIntegrity(getRemainingHullIntegrity() - damage);
        if (getRemainingHullIntegrity() <= startingHullIntegrity * 0.75) {
            this.changeModel(models.DAMAGED); //visual representation of damage
        }
        if (getRemainingHullIntegrity() == 0) {
            this.destroy();
        }
    }

    protected void destroy() {
        this.currentHullIntegrity = 0;
        this.isOperational = false;
    }

    public void fire(double angleToTarget, int weapon) {
        switch(weapon) {
            case 1 -> {
                if(primaryWeapon.readyToFire()) {
                    if (primaryWeapon.isTargetInFiringArc(angleToTarget)) {
                        this.launchedProjectiles.add(primaryWeapon.fire(this.getCenter(), angleToTarget));
                        System.out.println("PEW");
                    }
                }
            }
            case 3 -> {
                if(secondaryWeapon.readyToFire()) {
                    if (secondaryWeapon.isTargetInFiringArc(angleToTarget)) {
                        this.launchedProjectiles.add(secondaryWeapon.fire(this.getCenter(), angleToTarget));
                        System.out.println("pew");
                    }
                }
            }
        }
    }
}
