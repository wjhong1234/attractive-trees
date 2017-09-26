package cpsc581.hciii.project.tinasbutton.Game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import cpsc581.hciii.project.tinasbutton.EndGameActivity;
import cpsc581.hciii.project.tinasbutton.R;

/**
 * Created by William Hong on 9/22/17.
 */

/* Custom view for game stuff. Makes android games a lot like Java desktop games. */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    //Variables
    private GameThread play;
    private MotionEvent event;
    private Background bg;
    private boolean postRender=false, victory=false;

    private MediaPlayer backgroundMusic;
    private SoundPool pool;
    //private boolean sound=false, loaded=false;
    private int squeak, alphaP=0;

    private Player icarus;
    private ArrayList<Entity> mapObjs;

    /* Pass context to superclass.
     * Add callback to surface holder to get events like touch. */
    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);

        Log.i("GAMEPANEL", ":::GamePanel created!");
        play = new GameThread(getHolder(), this);
    }

    /* Initializes all the game stuff once the surface is created.
    *  Creates the assets for later use.
    *  sets the running flag to true, and starts the game thread.
    *  Cookies have 25% size reduction. */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mapObjs = new ArrayList<>();

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.track),
                BitmapFactory.decodeResource(getResources(), R.drawable.topcloud),
                BitmapFactory.decodeResource(getResources(), R.drawable.botclouds));
        icarus = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.cookie1),
                BitmapFactory.decodeResource(getResources(), R.drawable.cookie2), 284, 280);

        pool = new SoundPool(3, AudioManager.STREAM_MUSIC , 0);
        /* pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        }); */

        backgroundMusic = MediaPlayer.create(getContext(), R.raw.cute);
        squeak = pool.load(getContext(), R.raw.squeak,1);

        play.setRunning(true);
        play.start();
        backgroundMusic.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    /* If thread is alive when surface is destroyed, try to join thread, if not keeps trying. */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        pool.release();
        while(retry) {
            try {
                play.join(); //tries to destroy thread

            } catch (InterruptedException e) {
                //do nothing/ try to end thread again.
            } retry = false;
        }
    }

    /* On screen touch, sees if touch is within the hitbox of the player cookie and player moves to touch coordinate.
     * If not, player does not move. */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.event = event;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (icarus.getHitbox().contains((int) event.getX(), (int) event.getY())){
                icarus.setPlaying(true);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            icarus.setPlaying(false);
        } return true;
    }

    /* ticks all objects and determines what happens to them. */
    public void tick() {
        if (!postRender) {
            collisions();
            spawner();

            //Victory Condition
            if (icarus.getScore() >= 30) {
                postRender = true;
                victory = true;
            }

            //Tick Icarus
            if (icarus.getPlaying()) {
                if (!(((event.getX() - icarus.getxOffset()) <= 0) || (event.getX() + icarus.getxOffset()) > 1080))
                    icarus.setX(((int) event.getX()) - icarus.getxOffset());
                icarus.tick();
            }

            //Tick entities. Removes entity if it leaves the map.
            for (Entity item : mapObjs) {
                item.tick();
                if (item.getY() >= 1920) {
                    mapObjs.remove(item);
                }
            }
        }
    }

    /* Renders all objects to the canvas.
     * In order: Background, Map Items, Player, then Score Counter.
     * I rendered the score counter and clouds separately from the background for a nice animation effect.
     * I also didn't make a new class for the score counter because I was lazy. */
    public void render(Canvas stage) {
        boolean doneBot=false, doneTop=false;
        bg.render(stage);

        if (postRender) {
            doneBot = bg.reverseBotCloud(stage);
            Log.d("DONEBOT", "DONEBOT: " + doneBot);
        } else {
            bg.renderBotCloud(stage);
        }

        //Tick map objects
        for (Entity item : mapObjs) {
            item.render(stage);
        }

        if (postRender) {
            doneTop = bg.reverseTopCloud(stage);
            Log.d("DONETOP", "DONETOP: " + doneBot);
        } else {
            bg.renderTopCloud(stage);
        } icarus.render(stage);
        renderCounter(stage);

        if (postRender) {
            if (postRender) {
                Paint alpha = new Paint();
                alpha.setColor(getResources().getColor(R.color.white));
                alphaP += 15;
                if (alphaP > 255) alphaP = 255;
                alpha.setAlpha(alphaP);
                stage.drawRect(new Rect(0, 0, 1080, 1920), alpha);
            }
        }

        Log.d("DONEBOTH", "DONEBOTH: " + (doneTop && doneBot));
        if (doneBot && doneTop) {
            endGame();
        }
    }

    /* Checks for collisions on the map.
     * if item collision is a Cookie, then destroy the cookie and increment the score.
     * else item collision is a Bug, then destroy the bug and end the game with flag loss. */
    private void collisions() {
        //for loop for every object. determine what happens when it's a specific object (cookie/obstacle)
        for (Entity item : mapObjs) {
            if (icarus.getHitbox().intersect(item.getHitbox())) {
                // remove item from map item list.
                mapObjs.remove(item);
                if (item instanceof Cookie) {
                    // increment score & play sound
                    icarus.setScore(icarus.getScore() + 1);
                    pool.play(squeak, 1f, 1f, 0, 0, 1f);    //check if stops like mediaplayer
                } else {
                    //end game
                    postRender = true;
                    victory = false;
                }
            }
        }
    }

    //Counter stuff: Makes it look pretty.
    private void renderCounter(Canvas stage) {
        TextPaint paint = new TextPaint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "segoepr.ttf"));
        stage.drawText("COOKIE COUNTER: " + icarus.getScore() + "/30", 56, bg.getYCloud() + 100, paint);
    }

    private void spawner() {
        if(mapObjs.size() < 6) {
            Entity spawn = generateItems();
            if (spawn != null) {
                Random rand = new Random();
                int xCoor = rand.nextInt(1080 - spawn.getWidth());

                xCoor = (xCoor < spawn.getxOffset() ? (xCoor += spawn.getWidth()) : xCoor);

                mapObjs.add(spawn);
                spawn.setX(xCoor);
                spawn.setY(0 - spawn.getHeight());
            }
        }
    }

    /* Generates items to spawn on the map.
     * Generates a random number and spawns objects based on the number (or doesn't).
     * After creating object, it returns the object to the caller.
     * Returns null if number doesn't match anything.*/
    private Entity generateItems() {
        Random random = new Random();
        int num = random.nextInt(150);

        Log.i("GAMEPANEL", "NUM: " + num);
        if (num < 7) {
            //create cookie
            int name, width, height;
            if (num > 3) {
                //create Pablo
                name = R.drawable.pablo;
                width = 283;
                height = 281;
            } else if (num < 3) {
                //create Yang
                name = R.drawable.yang;
                width = 281;
                height = 264;
            } else {
                //create YEEZUS
                name = R.drawable.yeezus;
                width = 286;
                height = 260;
            } return new Cookie(BitmapFactory.decodeResource(getResources(), name), width, height);
        } else if ((num >= 7) && (num < 12)) {
            //create horrible thing
            return new Bug(BitmapFactory.decodeResource(getResources(), R.drawable.bug), 219, 157);
        } return null;
    }

    /* Ends the game appropriately. */
    private void endGame() {
        play.setRunning(false);
        backgroundMusic.stop();
        Intent i = new Intent(getContext(), EndGameActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("RESULT", victory);
        getContext().startActivity(i);
    }
}
