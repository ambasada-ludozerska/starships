package starships.equipment;

public class CoaxialMassDriver extends Weapon {
    public CoaxialMassDriver(double facing) {
        super(facing, 5, 20, 60, 8, 800, 75);
        this.type = weaponType.PRIMARY;
    }
}
