package starships.utility;

public class Vector {
    protected double x;
    protected double y;

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public void update(double x, double y) {
        this.x += x;
        this.y -= y;
    }

    public Vector() {
        this.x = 0;
        this.y = 0;
    }
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
