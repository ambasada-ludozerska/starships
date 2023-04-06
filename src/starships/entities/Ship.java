package starships.entities;

import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public abstract class Ship extends Entity implements IMovable {
    protected int forwardSpeed;
    protected int turningSpeed;
    protected int facing = 0;

    protected BufferedImage damagedModel;
    protected BufferedImage normalModel;

    enum models {NORMAL, DAMAGED}

    protected void changeModel(models newModel) {
        switch(newModel) {
            case NORMAL -> this.model = this.normalModel;
            case DAMAGED -> this.model = this.damagedModel;
        }
    }

    @Override
    public int getFacing() {
        return this.facing;
    }

    @Override
    public void setFacing(int newFacing) {
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
    public void moveForward() {
        this.setPos(
                this.getPos().getLocation().x + (int) (this.forwardSpeed * sin(Math.toRadians(this.getFacing()))),
                this.getPos().getLocation().y - (int) (this.forwardSpeed * cos(Math.toRadians(this.getFacing())))
        );
        this.setCenter(this.getPos().x + this.getSize(), this.getPos().y + this.getSize());
    }

    @Override
    public void turn(turningDirections direction) {
        switch(direction) {
            case LEFT -> this.setFacing(this.getFacing() - turningSpeed);
            case RIGHT -> this.setFacing(this.getFacing() + turningSpeed);
        }
    }

    public void collide(MapObject m) {

    }

    public void collide(Ship s) {
        this.changeModel(models.DAMAGED);
        s.changeModel(models.DAMAGED);
    }
}
