package starships.entities;

import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public abstract class Ship extends Entity implements IMovable {
    protected int forwardSpeed; //pixels per tick, sum of the separate X and Y changes
    protected int turningSpeed; //degrees per tick, to get degrees per second multiply by 30
    protected int facing = 0; //in degrees, value range: 0 - 359, might change coordinate system to double for higher precision?

    protected BufferedImage damagedModel; //currently not really showing damage, just notifies of a collision
    protected BufferedImage normalModel; //that's how it should look out of the box

    enum models {NORMAL, DAMAGED} //model states

    protected void changeModel(models newModel) { //switches between model states
        switch(newModel) {
            case NORMAL -> this.model = this.normalModel;
            case DAMAGED -> this.model = this.damagedModel;
        }
    }

    @Override
    public int getFacing() { //self-explanatory
        return this.facing;
    }

    @Override
    public void setFacing(int newFacing) { //probably overcomplicated but it works so I'll just leave it
        if(newFacing < 0) {
            this.facing = newFacing + 360;
        } else if(newFacing > 360) {
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
                this.getPos().getLocation().x + (int) (this.forwardSpeed * sin(Math.toRadians(this.getFacing()))),
                this.getPos().getLocation().y - (int) (this.forwardSpeed * cos(Math.toRadians(this.getFacing())))
        );
        this.setCenter(this.getPos().x + this.getSize(), this.getPos().y + this.getSize()); //sync actual pos and image corner
    }

    @Override
    public void turn(turningDirections direction) { //should be self-explanatory
        switch(direction) {
            case LEFT -> this.setFacing(this.getFacing() - turningSpeed);
            case RIGHT -> this.setFacing(this.getFacing() + turningSpeed);
        }
    }

    public void collide(MapObject m) { //handling collisions with static objects, affects only the ship

    }

    public void collide(Ship s) { //handling collisions with other ships, affects both ships
        this.changeModel(models.DAMAGED);
        s.changeModel(models.DAMAGED);
    }
}
