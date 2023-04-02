package starships.entities;

import static java.lang.Math.*;

public abstract class Ship extends Entity implements IMovable {
    protected int forwardSpeed;
    protected int turningSpeed;
    protected int facing = 0;

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
        System.out.println("Current pos: x=" + this.getPos().x + "; y=" + this.getPos().y);
        System.out.println("Current center: x=" + this.getCenter().x + "; y=" + this.getCenter().y);
    }

    @Override
    public void turn(turningDirections direction) {
        switch(direction) {
            case LEFT -> this.setFacing(this.getFacing() - turningSpeed);
            case RIGHT -> this.setFacing(this.getFacing() + turningSpeed);
        }
    }
}
