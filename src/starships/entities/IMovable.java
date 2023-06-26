package starships.entities;

public interface IMovable {
    enum turningDirections {LEFT, RIGHT}

    double getFacing();
    void setFacing(double newFacing);
    void move();
     void turn(turningDirections direction);
}
