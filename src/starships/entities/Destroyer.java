package starships.entities;

import starships.equipment.Autocannon;
import starships.equipment.CoaxialMassDriver;
import starships.utility.Vector;

import java.awt.*;

public class Destroyer extends Ship {
    public Destroyer(Point startingPosition, double initialFacing) {
        this.forwardAcceleration = 0.1;
        this.turningSpeed = 2.5;
        this.velocity = new Vector();

        this.setSize(15); //actual model is 2 times bigger, this is the hitbox radius
        this.pos.setLocation(startingPosition.getLocation());
        this.setFacing(initialFacing);
        this.imgPos.setLocation(this.pos.getX() - this.getSize(), this.pos.getY() - this.getSize());

        this.setStartingHullIntegrity(1800);
        this.setHullIntegrity(getStartingHullIntegrity());

        this.primaryWeapon = new CoaxialMassDriver(this.getFacing());   //Weapon(this.getFacing(), 90, 10, 45, 5, 200, 30);
        this.primaryWeapon.updateFiringArc();
        this.secondaryWeapon = new Autocannon(this.getFacing());
        this.secondaryWeapon.updateFiringArc();

        this.normalModel = tryLoadImage("resources/images/destroyer.png"); //set up all the model states
        this.damagedModel = tryLoadImage("resources/images/destroyer.png");
        this.heavilyDamagedModel = tryLoadImage("resources/images/destroyer.png");
        this.wreckedModel = tryLoadImage("resources/images/collision.png");
        this.model = normalModel; //by default, start undamaged
    }
}
