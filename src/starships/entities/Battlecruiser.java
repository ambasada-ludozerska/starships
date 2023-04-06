package starships.entities;

import java.awt.*;

public class Battlecruiser extends Ship {
    public Battlecruiser(Point startingPosition) {
        this.forwardSpeed = 4;
        this.turningSpeed = 3;

        this.setSize(40);
        this.setPos(startingPosition.x, startingPosition.y);
        this.setCenter(this.getPos().x + this.getSize(), this.getPos().y + this.getSize());

        this.normalModel = tryLoadImage("resources/images/tempMarker.png");
        this.damagedModel = tryLoadImage("resources/images/collision.png");
        this.model = normalModel;
    }
}
