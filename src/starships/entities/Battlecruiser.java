package starships.entities;

import starships.equipment.Autocannon;
import starships.equipment.HeavyCannon;
import starships.utility.Vector;

import java.awt.*;

public class Battlecruiser extends Ship {
    public Battlecruiser(Point startingPosition, double initialFacing) {
        this.forwardAcceleration = 0.02;
        this.turningSpeed = 2;
        this.velocity = new Vector();

        this.setSize(20); //actual model is 2 times bigger, this is the hitbox radius
        this.pos.setLocation(startingPosition.getLocation());
        this.setFacing(initialFacing);
        this.imgPos.setLocation(this.pos.getX() - this.getSize(), this.pos.getY() - this.getSize());

        this.setStartingHullIntegrity(3000);
        this.setHullIntegrity(getStartingHullIntegrity());

        this.primaryWeapon = new HeavyCannon(this.getFacing());   //Weapon(this.getFacing(), 90, 10, 45, 5, 200, 30);
        this.primaryWeapon.updateFiringArc();
        this.secondaryWeapon = new Autocannon(this.getFacing());
        this.secondaryWeapon.updateFiringArc();

        this.normalModel = tryLoadImage("resources/images/battlecruiser3.png"); //set up all the model states
        this.damagedModel = tryLoadImage("resources/images/battlecruiser3.png");
        this.heavilyDamagedModel = tryLoadImage("resources/images/battlecruiser3.png");
        this.wreckedModel = tryLoadImage("resources/images/collision.png");
        this.model = normalModel; //by default, start undamaged
    }
}
