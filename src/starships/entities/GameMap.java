package starships.entities;

import starships.ui.PlayerController;

import java.awt.*;
import java.util.HashMap;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class GameMap {
    private final String name;
    private final int width;
    private final int height;

    private PlayerController player;
    private HashMap<MapObject, Point> mapObjectPositions = new HashMap<>();
    private HashMap<Ship, Point> shipPositions = new HashMap<>();

    public String getName() {return this.name;}
    public int getWidth() {return this.width;}
    public int getHeight() {return this.height;}

    public PlayerController getPlayer() {
        return this.player;
    }


    public void addMapObject(MapObject mapObject) {
        mapObjectPositions.put(mapObject, mapObject.getPos());
    }
    public void addShip(Ship ship) {
        shipPositions.put(ship, ship.getPos());
    }
    public void addPlayer(PlayerController player) {
        this.player = player;
    }

    public HashMap<Ship, Point> getAllShips() {
        return this.shipPositions;
    }

    public GameMap(String mapName, int mapWidth, int mapHeight) {
        this.name = mapName;
        this.width = mapWidth;
        this.height = mapHeight;
    }

    public void checkCollisions() {
        for (Ship s : shipPositions.keySet()) {
            for (MapObject m : mapObjectPositions.keySet()) {
                if(calculateDistance(s, m) <= calculateCollisionDistance(s, m)) {
                    s.collide(m);
                }
            }
            for (Ship s2 : shipPositions.keySet()) {
                if(s != s2) {
                    if(calculateDistance(s, s2) <= calculateCollisionDistance(s, s2)) {
                        s.collide(s2);
                    }
                }
            }
        }
    }

    public void checkOutOfBounds() {
        for (Ship s : shipPositions.keySet()) {
            if(s.getCenter().getX() < 0 || s.getCenter().getY() < 0 || s.getCenter().getX() > this.getWidth() || s.getCenter().getY() > this.getHeight()) {
                s.setPos(this.width / 2, this.height / 2);
            }
        }
    }

    public int calculateCollisionDistance(Entity e1, Entity e2) {
        return (int) (pow((e1.getSize() + e2.getSize()), 2));
    }
    public int calculateDistance(Entity e1, Entity e2) {
        return (int) (pow(abs(e1.getCenter().getX() - e2.getCenter().getX()), 2) + pow(abs(e1.getCenter().getY() - e2.getCenter().getY()), 2));
    }
}
