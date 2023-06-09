package starships.entities;

import java.awt.*;

public class MapObject extends Entity {
    private final String name; //currently unused, should get some purpose when I get to HUD

    public String getName() { //look above
        return this.name;
    }

    public MapObject(String name, Point pos, int size, String modelPath) {
        this.name = name;
        this.pos = pos;
        this.size = size;
        this.model = tryLoadImage(modelPath);
    }
}
