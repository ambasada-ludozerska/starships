package starships.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Entity {
    protected Point pos = new Point(); //upper left corner of the image
    protected Point center = new Point(); //actual position on the map and center of the hitbox
    protected int size;
    protected BufferedImage model; //actually, it probably should be activeModel for clarity

    public Point getPos() {return this.pos;}
    public Point getCenter() {return this.center;}
    public int getSize() {return this.size;} //radius of the circular hitbox
    public BufferedImage getModel() {return this.model;} //should probably be named getActiveModel
    public void setPos(int x, int y) {this.pos.setLocation(x, y);}
    protected void setCenter(int x, int y) {this.center.setLocation(x, y);}
    protected void setSize(int size) {this.size = size;} //it's actually half of the size as it's the radius of the hitbox

    protected BufferedImage tryLoadImage(String path) { //path template: "resources/<subfolder>/<filename>.png
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Couldn't load image");
        }
    }

}
