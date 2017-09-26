package cpsc581.hciii.project.tinasbutton.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by William Hong on 9/22/17.
 */

/* Class representing the player. */
public class Player extends Entity {

    //Variables
    private Bitmap[] icarus = new Bitmap[2];
    private int score, faceIndex;
    private boolean playing;

    /* Constructor with all the player frames. Too lazy to make sprite sheet pls understand.
    *  Int for width and height of the player. */
    public Player(Bitmap happy, Bitmap superHappy, int width, int height) {
        super(540, 1600, width, height);
        icarus[0] = Bitmap.createScaledBitmap(happy, width, height, false);
        icarus[1] = Bitmap.createScaledBitmap(superHappy, width, height, false);
        setX(540 - getxOffset());
        score = 0;
    }

    /* Determines which faces to use for the player emotions [2]. */
    public void tick() {
        if ((score % 10) < 3) {
            faceIndex = 1;
        } else {
            faceIndex = 0;
        }
    }

    /* Draws the player asset/ */
    public void render(Canvas stage) {
        stage.drawBitmap(icarus[faceIndex], getX(), getY(), null);
    }

    //Getters & Setters
    public int getScore() {
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

}
