package starships.entities;

import java.awt.*;

public class Battlecruiser extends Ship {
    public Battlecruiser(Point startingPosition) {
        this.forwardSpeed = 4;
        this.turningSpeed = 3;

        this.setSize(40);
        this.setPos(startingPosition.x, startingPosition.y);

        this.tryLoadImage("resources/images/tempMarker.png");
    }
}
