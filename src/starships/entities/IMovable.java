package starships.entities;

public interface IMovable {
    enum turningDirections {LEFT, RIGHT}

    double getFacing();
    void setFacing(double newFacing);
    void moveForward();
     void turn(turningDirections direction);
}
