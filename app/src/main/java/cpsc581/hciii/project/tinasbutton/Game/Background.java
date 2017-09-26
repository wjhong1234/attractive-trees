package cpsc581.hciii.project.tinasbutton.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by William Hong on 9/22/17.
 */

//Class representing the background of the game.
public class Background {

    //Constants
    private int TOP_SPEED = 13;
    private int BOT_SPEED = 30;

    //Variables
    private Bitmap map, topCloud, botCloud;
    private int yTopCloud, yBotCloud;

    /* Initializes the Bitmaps for the background and the top clouds where the score is.
    *  Instantiates the top cloud y-axis variable. */
    public Background(Bitmap map, Bitmap topCloud, Bitmap botCloud) {
        this.map = Bitmap.createScaledBitmap(map, 1080, 1920, false);
        this.topCloud = Bitmap.createScaledBitmap(topCloud, 1080, 1920, false);
        this.botCloud = Bitmap.createScaledBitmap(botCloud, 1080, 1920, false);
        yTopCloud = -208;
        yBotCloud = 384;
    }

    /* Renders all parts of the map.
     * If statement is for cheap animation of the clouds coming from the top during the initial map load. */
    public void render(Canvas stage) {
        stage.drawBitmap(map, 0, 0, null);
    }

    public void renderTopCloud(Canvas stage) {
        if (yTopCloud < -10) yTopCloud += TOP_SPEED;
        stage.drawBitmap(topCloud, 0, yTopCloud, null);
    }

    public void renderBotCloud(Canvas stage) {
        if (yBotCloud > 0) yBotCloud -= BOT_SPEED;
        stage.drawBitmap(botCloud, 0, yBotCloud, null);
    }

    public boolean reverseTopCloud(Canvas stage) {
        if (yTopCloud > -208) {
            yTopCloud -= TOP_SPEED / 3;
            Log.d("TOPCLOUD", "TOPCLOUD: " + yTopCloud);
            stage.drawBitmap(topCloud, 0, yTopCloud, null);
            return false;
        } else {
            return true;
        }
    }

    public boolean reverseBotCloud(Canvas stage) {
        if (yBotCloud <= 384) {
            yBotCloud += BOT_SPEED / 3;
            Log.d("BOTCLOUD", "BOTCLOUD: " + yBotCloud);
            stage.drawBitmap(botCloud, 0, yBotCloud, null);
            return false;
        } else {
            return true;
        }
    }

    public int getYCloud() {
        return yTopCloud;
    }
}
