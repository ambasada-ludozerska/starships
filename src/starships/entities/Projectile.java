package starships.entities;

import starships.utility.Vector;

import java.awt.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Projectile extends Ship {
    int lifetime; //in ticks
    int ttl; //time-to-live in ticks
    int armingDelay; //in ticks
    int damage;

    public int getLifetime() {return this.lifetime;}
    public int getTimeToLive() {return this.ttl;}
    public int getArmingDelay() {return this.armingDelay;}

    //COLLISION HANDLING
    @Override
    public void collide(MapObject m) {
        this.destroy();
    }
    @Override
    public void collide(Ship s) {
        this.destroy();
        s.takeDamage(this.damage);
    }
    @Override
    public void takeDamage(int damage) {
        this.destroy();
    }

    public Projectile(Point origin, Vector originVelocity, double facing, int speed, int size, int lifetime, int damage) {
        this.velocity = new Vector(originVelocity.getX() + speed * sin(Math.toRadians(facing)), originVelocity.getY() + speed * -cos(Math.toRadians(facing)));

        this.size = size;
        this.setFacing(facing);
        this.pos.setLocation(origin.getLocation());
        this.imgPos.setLocation(this.pos.getX() - this.getSize(), this.pos.getY() - this.getSize());

        this.setStartingHullIntegrity(1); //shouldn't be necessary but I don't want to risk exceptions if I missed something
        this.setHullIntegrity(getStartingHullIntegrity());

        this.lifetime = lifetime;
        this.ttl = lifetime;
        this.armingDelay = (80 / speed); //number of ticks required to gain safe clearance from the launching ship
        this.damage = damage;
    }
}
