package cpsc581.hciii.project.tinasbutton.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by William Hong on 9/22/17.
 */

/* Class representing Cookies, the power-up or similar in this game. */
public class Cookie extends Entity {

    //Constants
    private final int SPEED = 15;

    //Variables
    private Bitmap cookie;

    /* Constructor receives initial location of cookie. */
    public Cookie(Bitmap cookie, int width, int height) {
        super(540, 0, width, height);
        this.cookie = Bitmap.createScaledBitmap(cookie, width, height, false);
    }

    /* Updates the cookie's position. */
    public void tick() {
        this.setY(this.getY() + SPEED);
    }

    /* Draws the cookie asset. */
    public void render(Canvas stage) {
        stage.drawBitmap(cookie, getX(), getY(), null);
    }
}
