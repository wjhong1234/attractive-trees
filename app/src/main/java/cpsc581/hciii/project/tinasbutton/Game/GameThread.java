package cpsc581.hciii.project.tinasbutton.Game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by William Hong on 9/22/17.
 */

public class GameThread extends Thread {

    //Variables
    private SurfaceHolder holder;
    private GamePanel theatre;
    public static Canvas stage;

    //If game running flag
    private boolean inProgress;

    //Constructor
    public GameThread(SurfaceHolder holder, GamePanel theatre){
        //super();?
        this.holder = holder;
        this.theatre = theatre;
        Log.i("GAMETHREAD", ":::Thread created!");
    }

    /* Thread starts and creates the game loop. The loop is controlled by the various math things that happened.
     * The difficult to read math is in fact for determining the FPS of the game. Currently set to 60 FPS.
     * Releases the canvas once the game loop is over.
     */
    @Override
    public void run() {
        //FPS math
        long currentTime;
        int fps = 50;
        double timePerTick = 1000000000 / fps;
        double change = 0;
        long lastTime = System.nanoTime();

        long timer = 0;
        int ticks = 0;

        //Game loop
        while(inProgress) {
            try {
                currentTime = System.nanoTime();
                change += (currentTime - lastTime) / timePerTick;
                timer += currentTime - lastTime;
                lastTime = currentTime;

                //If a frame can be processed, then lock, tick and render.
                if (change >= 1) {
                    stage = holder.lockCanvas();
                    synchronized(holder) {
                        this.theatre.render(stage);
                        this.theatre.tick();
                    } ticks++;
                    change--;
                }

                // If the timer has been on for a second, then display the FPS to log.
                if (timer >= 1000000000) {
                    Log.i("GameThread", "FPS: " + ticks);
                    timer = 0;
                    ticks = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stage != null) {
                    try {
                        holder.unlockCanvasAndPost(stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } //game end
    }

    public boolean getRunning() {
        return inProgress;
    }

    //Game running flag for the game loop.
    public void setRunning(boolean inProgress) {
        this.inProgress = inProgress;
    }
}
