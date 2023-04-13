package starships.entities;

import starships.controllers.AIController;
import starships.controllers.IController;
import starships.equipment.Weapon;
import starships.controllers.PlayerController;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class GameMap {
    private final String name; //Currently unused, will wait until I'm doing menu
    private final int width;
    private final int height;

    private PlayerController localPlayer; //TODO - potentially add multiplayer
    private final ArrayList<AIController> AIs = new ArrayList<>();
    private final HashMap<MapObject, Point> mapObjectPositions = new HashMap<>();
    private final HashMap<Ship, IController> activeShips = new HashMap<>();
    private final ArrayList<Projectile> activeProjectiles = new ArrayList<>();

    public String getName() {return this.name;} //Currently unused
    public int getWidth() {return this.width;}
    public int getHeight() {return this.height;}

    public PlayerController getLocalPlayer() {
        return this.localPlayer;
    }
    public void findAllAIs() {
        for (IController c : activeShips.values()) {
            if(c.getClass() == AIController.class) {
                this.AIs.add((AIController) c);
            }
        }
    }

    public void addMapObject(MapObject mapObject) {
        mapObjectPositions.put(mapObject, mapObject.getCenter());
    }
    public void addShip(IController shipController) {
        activeShips.put(shipController.getShip(), shipController);
    }
    public void setLocalPlayer(PlayerController player) {
        this.localPlayer = player;
    }

    public void spawnProjectiles(ArrayList<Projectile> projectiles) {
        activeProjectiles.addAll(projectiles);
    }

    public HashMap<MapObject, Point> getStaticObjects() {
        return this.mapObjectPositions;
    }
    public HashMap<Ship, IController> getAllShips() {
        return this.activeShips;
    }
    public  ArrayList<Projectile> getActiveProjectiles() {
        return this.activeProjectiles;
    }

    public GameMap(String mapName, int mapWidth, int mapHeight) {
        this.name = mapName;
        this.width = mapWidth;
        this.height = mapHeight;
    }

    public void updateAIs() {
        for (AIController ai : AIs) {
            ai.planAction(findClosestEligibleTarget(ai.getShip()));
            ai.performActions();
        }

    }
    public Ship findClosestEligibleTarget(Ship reference) {
        double shortestDistance = getWidth();
        double currentDistance;
        Ship closest = null;
        for (Ship s : activeShips.keySet()) {
            if (s != reference) {
                if (activeShips.get(s).getTeam() != activeShips.get(reference).getTeam() && s.isOperational()) {
                    currentDistance = reference.getCenter().distance(s.getCenter());
                    if (currentDistance < shortestDistance) {
                        shortestDistance = currentDistance;
                        closest = s;
                    }
                }
            }
        }
        if(closest != null) {
            return closest;
        } else {
            return reference;
        }
    }

    public void updateProjectiles() {
        for (Ship s : getAllShips().keySet()) {
            spawnProjectiles(s.getLaunchedProjectiles()); //register all the projectiles generated since last tick
            s.wipeStoredProjectiles(); //clear up the queue so it doesn't generate an infinite amount of projectiles and explode
            for (Weapon w : s.getWeapons()) {
                w.reduceCooldown();
            }
        }
        for(Iterator<Projectile> i = activeProjectiles.iterator(); i.hasNext();) {
            Projectile p = i.next();
            if(p.getTimeToLive() <= 0 || !p.isOperational()) { //hopefully kill expired and successful projectiles
                i.remove();
            }
            p.moveForward();
            p.ttl--; //track lifetime of every projectile
        }
    }

    public void checkCollisions() { //extremely unoptimized, TODO - optimize collisions, not yet sure how
        for (Ship s : activeShips.keySet()) {
            for (MapObject m : mapObjectPositions.keySet()) {
                if (calculateDistance(s, m) <= calculateCollisionDistance(s, m)) {
                    s.collide(m);
                }
            }
            for (Ship s2 : activeShips.keySet()) {
                if (s != s2) {
                    if (calculateDistance(s, s2) <= calculateCollisionDistance(s, s2)) {
                        s.collide(s2); //TODO - prevent processing the same collision twice
                    }
                }
            }
            for (Projectile p : activeProjectiles) {
                if (calculateDistance(s, p) <= calculateCollisionDistance(s, p) && p.getLifetime() - p.getTimeToLive() > p.getArmingDelay()) {
                    p.collide(s);
                }
            }
            /*if(!s.isOperational()) {
                i.remove(); //Hopefully kill wrecked ships. Probably not the best place for it, but let's just leave it here for now. TODO - track kills for score
            }*/
        }
    }

    public void checkOutOfBounds() { //TODO - better OoB handling than "just die", preferably forced return
        for (Ship s : activeShips.keySet()) {
            if(s.getCenter().getX() < 0 || s.getCenter().getY() < 0 || s.getCenter().getX() > this.getWidth() || s.getCenter().getY() > this.getHeight()) {
                s.takeDamage(10);
            }
        }
    }

    public int calculateCollisionDistance(Entity e1, Entity e2) { //sum of hitbox radii, cubed to avoid sqrt
        return (int) (pow((e1.getSize() + e2.getSize()), 2));
    }
    public int calculateDistance(Entity e1, Entity e2) { //no sqrt to try to improve performance, comparing to cubed hitbox instead
        return (int) (pow(abs(e1.getCenter().getX() - e2.getCenter().getX()), 2) + pow(abs(e1.getCenter().getY() - e2.getCenter().getY()), 2));
    }
}
