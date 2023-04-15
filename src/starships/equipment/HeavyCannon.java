package starships.equipment;

public class HeavyCannon extends Weapon {
    public HeavyCannon(double facing) {
        super(facing, 135, 10, 60, 5, 200, 30);
        this.type = weaponType.PRIMARY;
    }
}
