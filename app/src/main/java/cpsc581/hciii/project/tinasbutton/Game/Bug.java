package cpsc581.hciii.project.tinasbutton.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by William Hong on 9/25/17.
 */

public class Bug extends Entity {

    private final int SPEED = 20;

    private Bitmap bug;

    public Bug(Bitmap bug, int width, int height) {
        super(540, 0, width, height);
        this.bug = Bitmap.createScaledBitmap(bug, width, height, false);
    }

    @Override
    public void tick() {
        this.setY(this.getY() + SPEED);
    }

    @Override
    public void render(Canvas stage) {
        stage.drawBitmap(bug, getX(), getY(), null);
    }

    @Override
    public Rect getHitbox() {
        int third = this.getWidth() / 3;
        return new Rect(getX() + third, getY(), getX() + getWidth() - third, getY() + getHeight());
    }
}
