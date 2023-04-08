package starships.entities;

import java.awt.*;

public class Projectile extends Ship {
    int lifetime; //in ticks
    int ttl; //in ticks
    int armingDelay; //in ticks
    int damage;

    public int getLifetime() {return this.lifetime;}
    public int getTimeToLive() {return this.ttl;}
    public int getArmingDelay() {return this.armingDelay;}

    @Override
    public void collide(MapObject m) {
        this.destroy();
    }
    @Override
    public void collide(Ship s) {
        this.destroy();
        s.takeDamage(damage);
    }
    @Override
    public void takeDamage(int damage) {
        this.destroy();
    }

    public Projectile(Point origin, double facing, int speed, int size, int lifetime, int damage) {
        this.forwardSpeed = speed;

        this.size = size;
        this.setFacing(facing);
        this.setCenter(origin.x, origin.y);
        this.setPos((int) (this.getCenter().getX() - this.getSize()), (int) (this.getCenter().getY() - this.getSize()));

        this.setStartingHullIntegrity(1); //shouldn't be necessary but I don't want to risk exceptions if I missed something
        this.setHullIntegrity(getStartingHullIntegrity());

        this.lifetime = lifetime;
        this.ttl = lifetime;
        this.armingDelay = (80 / speed); //number of ticks required to gain safe clearance from the launching ship
        this.damage = damage;
    }
}
