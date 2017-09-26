package cpsc581.hciii.project.tinasbutton.Game;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by William Hong on 9/22/17.
 */

/* Abstract class representing all cookies. */
public abstract class Entity {

    //Variables
    private int x, y, width, height, xOffset, yOffset;

    /* Constructor. Other than the boring stuff, instantiates the offsets for a picture as well. */
    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        xOffset = width / 2;
        yOffset = height / 2;
    }

    //Various methods, mostly getters and setters.
    public abstract void tick();

    public abstract void render(Canvas stage);

    public Rect getHitbox() {
        return new Rect(x, y, x+width, y+height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getxOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }
}
