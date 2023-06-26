package starships.equipment;

import starships.entities.Projectile;
import starships.utility.Vector;

import java.awt.*;

public class Weapon {
    public enum weaponType {PRIMARY, SECONDARY}
    protected weaponType type;

    protected double weaponFacing; //center of the firing arc
    protected double firingArc; //max rotation in each direction, full arc is 2 times larger
    protected double maxArcLeft;
    protected double maxArcRight;

    protected int projectileSpeed; //in pixels per tick
    protected int projectileLifetime; //in ticks
    protected int projectileSize; //in pixels
    protected int projectileDamage;

    protected int refire; //reload time in ticks, 30 ticks per second
    protected int cooldownRemaining; //in ticks

    public weaponType getWeaponType() {
        return this.type;
    }

    public boolean readyToFire() {
        return cooldownRemaining <= 0;
    }
    public void reduceCooldown() {
        this.cooldownRemaining--;
    }

    public void setWeaponFacing(double facing) {
        this.weaponFacing = facing;
    }
    public void updateFiringArc() {
        if(this.weaponFacing - firingArc < 0) { //limits the values to between 0 and 360 (full 360 not included)
            this.maxArcLeft = weaponFacing - firingArc + 360;
        } else {
            this.maxArcLeft = weaponFacing - firingArc;
        }
        if(this.weaponFacing + firingArc > 360) {
            this.maxArcRight = weaponFacing + firingArc - 360;
        } else if(this.weaponFacing + firingArc == 360) {
            this.maxArcRight = 0;
        } else {
            this.maxArcRight = weaponFacing + firingArc;
        }
    }
    @SuppressWarnings("RedundantIfStatement")
    public boolean isTargetInFiringArc(double angle) {
        if(maxArcLeft < maxArcRight) { //if firing arc is uninterrupted by Y axis
            if(angle >= maxArcLeft && angle <= maxArcRight) {
                return true;
            } else {
                return false;
            }
        } else if(maxArcLeft > maxArcRight) { //if firing arc is divided by Y axis
            if(angle >= maxArcLeft || angle <= maxArcRight) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public int getWeaponRange() {
        return projectileSpeed * projectileLifetime;
    }

    public Projectile fire(Point origin, Vector velocity, double angleToTarget) {
        cooldownRemaining = refire;
        return new Projectile(origin, velocity, angleToTarget, projectileSpeed, projectileSize, projectileLifetime, projectileDamage);
    }

    public Weapon(double facing, double firingArc, int pSpeed, int pLife, int pSize, int pDamage, int refire) {
        this.weaponFacing = facing;
        this.firingArc = firingArc;
        this.maxArcLeft = weaponFacing - firingArc;
        this.maxArcRight = weaponFacing + firingArc;

        this.projectileSpeed = pSpeed;
        this.projectileLifetime = pLife;
        this.projectileSize = pSize;
        this.projectileDamage = pDamage;
        this.refire = refire;
        this.cooldownRemaining = 0;
    }
}
