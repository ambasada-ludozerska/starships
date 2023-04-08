package starships.entities;

import java.awt.*;

public class Battlecruiser extends Ship {
    public Battlecruiser(Point startingPosition) {
        this.forwardSpeed = 4;
        this.turningSpeed = 2;

        this.setSize(40); //actual model is 2 times bigger, this is the hitbox radius
        this.setPos(startingPosition.x, startingPosition.y);
        this.setCenter((int) Math.round(this.getPos().getX() + this.getSize()), (int) Math.round(this.getPos().getY() + this.getSize())); //maybe I should rename center to pos and pos to imagePos, it's starting to be confusing

        this.setStartingHullIntegrity(3000);
        this.setHullIntegrity(getStartingHullIntegrity());

        this.primaryWeapon = new Weapon(this.getFacing(), 90, 10, 45, 5, 200);
        this.primaryWeapon.updateFiringArc();
        this.secondaryWeapon = new Weapon(this.getFacing(), 120,20, 15, 2, 40);
        this.secondaryWeapon.updateFiringArc();

        this.normalModel = tryLoadImage("resources/images/battlecruiser.png"); //set up all the model states
        this.damagedModel = tryLoadImage("resources/images/collision.png");
        this.model = normalModel; //by default, start undamaged
    }
}
