package starships.entities;

import java.awt.*;

public class Battlecruiser extends Ship {
    public Battlecruiser(Point startingPosition) {
        this.forwardSpeed = 4;
        this.turningSpeed = 2;

        this.setSize(40); //actual model is 2 times bigger, this is the hitbox radius
        this.setPos(startingPosition.x, startingPosition.y);
        this.setCenter((int) (this.getPos().getX() + this.getSize()), (int) (this.getPos().getY() + this.getSize())); //maybe I should rename center to pos and pos to imagePos, it's starting to be confusing

        this.setStartingHullIntegrity(3000);
        this.setHullIntegrity(getStartingHullIntegrity());

        this.normalModel = tryLoadImage("resources/images/battlecruiser.png"); //set up all the model states
        this.damagedModel = tryLoadImage("resources/images/collision.png");
        this.model = normalModel; //by default, start undamaged
    }
}
