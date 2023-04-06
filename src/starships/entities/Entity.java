package starships.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Entity {
    protected Point pos = new Point();
    protected Point center = new Point();
    protected int size;
    protected BufferedImage model;

    public Point getPos() {return this.pos;}
    public Point getCenter() {return this.center;}
    public int getSize() {return this.size;}
    public BufferedImage getModel() {return this.model;}
    public void setPos(int x, int y) {this.pos.setLocation(x, y);}
    protected void setCenter(int x, int y) {this.center.setLocation(x, y);}
    protected void setSize(int size) {this.size = size;}

    protected BufferedImage tryLoadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Couldn't load image");
        }
    }

}
