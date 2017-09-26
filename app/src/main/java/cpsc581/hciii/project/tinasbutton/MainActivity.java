package cpsc581.hciii.project.tinasbutton;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/* All this activity does is have a cookie button that can be pressed to start a game! */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setTypeface(Typeface.createFromAsset(getBaseContext().getAssets(), "segoepr.ttf"));

        /* Sets the button to the declared XML button and sets an onTouchListener to it.
         * Opted to use OnTouch instead of OnClick so the button can "react" when touched. */
        final ImageButton startButton = (ImageButton) findViewById(R.id.startButton);
        startButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startButton.setImageResource(R.drawable.cookie2);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    startButton.setImageResource(R.drawable.cookie1);
                } return false;
            }
        });

        /* The long press listener starts the game upon pressing. This is simply for aesthetics. */
        startButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                return false;
            }
        });
    }
}
