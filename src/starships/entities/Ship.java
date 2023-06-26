package starships.entities;

import starships.equipment.Weapon;
import starships.utility.Vector;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.*;

public abstract class Ship extends Entity implements IMovable {

    //MOVEMENT-RELATED

    protected double turningSpeed; //degrees per tick, to get degrees per second multiply by 30

    protected Vector velocity;
    protected double forwardAcceleration;
    protected double facing = 0; //in degrees, value range: 0 - 359

    //COMBAT-RELATED

    protected int currentHullIntegrity;
    protected int startingHullIntegrity;
    protected boolean isOperational = true;

    protected Weapon primaryWeapon;
    protected Weapon secondaryWeapon;

    protected ArrayList<Projectile> launchedProjectiles = new ArrayList<>();

    //MODELS

    protected BufferedImage normalModel; //that's how it should look out of the box
    protected BufferedImage damagedModel; //currently not really showing damage, just notifies of a collision
    protected BufferedImage heavilyDamagedModel;
    protected BufferedImage wreckedModel;

    enum models {NORMAL, DAMAGED, HEAVILY_DAMAGED, DESTROYED} //model states

    protected void changeModel(models newModel) { //switches between model states
        switch(newModel) {
            case NORMAL -> this.model = this.normalModel;
            case DAMAGED -> this.model = this.damagedModel;
            case HEAVILY_DAMAGED -> this.model = this.heavilyDamagedModel;
            case DESTROYED -> this.model = this.wreckedModel;
        }
    }


    //DAMAGE HANDLING

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

    protected void takeDamage(int damage) {
        if(isOperational()) {
            this.setHullIntegrity(getRemainingHullIntegrity() - damage);
            if (getRemainingHullIntegrity() <= startingHullIntegrity * 0.75) {
                this.changeModel(models.DAMAGED); //visual representation of damage
            }
            if (getRemainingHullIntegrity() <= startingHullIntegrity * 0.25) {
                this.changeModel(models.HEAVILY_DAMAGED);
            }
            if (getRemainingHullIntegrity() == 0) {
                this.destroy();
            }
        }
    }

    protected void destroy() {
        this.isOperational = false;
        this.changeModel(models.DESTROYED);
    }


    //FIRING WEAPONS
    public ArrayList<Weapon> getWeapons() {
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(primaryWeapon);
        weapons.add(secondaryWeapon);
        return weapons;
    }

    public void fire(double angleToTarget, Weapon.weaponType weapon) {
        switch(weapon) {
            case PRIMARY -> {
                if(primaryWeapon.readyToFire()) {
                    if (primaryWeapon.isTargetInFiringArc(angleToTarget)) {
                        this.launchedProjectiles.add(primaryWeapon.fire(this.getPos(), this.velocity, angleToTarget));
                    }
                }
            }
            case SECONDARY -> {
                if(secondaryWeapon.readyToFire()) {
                    if (secondaryWeapon.isTargetInFiringArc(angleToTarget)) {
                        this.launchedProjectiles.add(secondaryWeapon.fire(this.getPos(), this.velocity, angleToTarget));
                    }
                }
            }
        }
    }

    public ArrayList<Projectile> getLaunchedProjectiles() {
        return this.launchedProjectiles;
    }
    public void wipeStoredProjectiles() {
        this.launchedProjectiles.clear();
    }


    //ROTATION
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

    //MOVEMENT
    @Override
    public void move() { //assumes facing 0 is straight up
        /*this.setPos(
                (int) (this.getPos().getLocation().getX() +  this.forwardSpeed * sin(Math.toRadians(this.getFacing()))),
                (int) (this.getPos().getLocation().getY() - this.forwardSpeed * cos(Math.toRadians(this.getFacing())))
        ); */
        this.pos.setLocation(this.pos.getX() + this.velocity.getX(), this.pos.getY() + this.velocity.getY());
        this.imgPos.setLocation(this.pos.getX() - this.size / 2, this.pos.getY() - this.size / 2); //sync actual position and image corner
        //this.setCenter((int) (this.getPos().getX() + this.getSize()), (int) (this.getPos().getY() + this.getSize()));

    }

    public void accelerate() {
        this.velocity.update(this.forwardAcceleration * sin(Math.toRadians(this.getFacing())), this.forwardAcceleration * cos(Math.toRadians(this.getFacing())));
    }

    //COLLISIONS

    public void collide(MapObject m) { //handling collisions with static objects, affects only the ship
        this.destroy();
    }

    public void collide(Ship s) { //handling collisions with other ships, affects both ships
            this.destroy();
            s.destroy();
    }

}
