package starships.entities;

import java.awt.*;

public class MapObject extends Entity {
    private final String name;

    public String getName() {
        return this.name;
    }

    public MapObject(String name, Point pos, String modelPath) {
        this.name = name;
        this.pos = pos;
        this.tryLoadImage(modelPath);
    }
}
