package starships.entities;

public interface IMovable {
    enum turningDirections {LEFT, RIGHT}

    int getFacing();
    void setFacing(int newFacing);
    void moveForward();
     void turn(turningDirections direction);
}
