package starships.entities;

import java.awt.*;

public class Weapon {
    protected double weaponFacing;
    protected double firingArc;
    protected double maxArcLeft;
    protected double maxArcRight;

    protected int projectileSpeed;
    protected int projectileLife;
    protected int projectileSize;
    protected int projectileDamage;


    public void updateFiringArc() {
        if(this.weaponFacing - firingArc < 0) {
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
        if(maxArcLeft < maxArcRight) {
            if(angle >= maxArcLeft && angle <= maxArcRight) {
                return true;
            } else {
                return false;
            }
        } else if(maxArcLeft > maxArcRight) {
            if(angle >= maxArcLeft || angle <= maxArcRight) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public Projectile fire(Point origin, double angleToTarget) {
        return new Projectile(origin, angleToTarget, projectileSpeed, projectileSize, projectileLife, projectileDamage);
    }

    public Weapon(double facing, double firingArc, int pSpeed, int pLife, int pSize, int pDamage) {
        this.weaponFacing = facing;
        this.firingArc = firingArc;
        this.maxArcLeft = weaponFacing - firingArc;
        this.maxArcRight = weaponFacing + firingArc;

        this.projectileSpeed = pSpeed;
        this.projectileLife = pLife;
        this.projectileSize = pSize;
        this.projectileDamage = pDamage;
    }
}
