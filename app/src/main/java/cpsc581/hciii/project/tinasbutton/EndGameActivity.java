package cpsc581.hciii.project.tinasbutton;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class EndGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_end_game);

        final boolean victory = getIntent().getBooleanExtra("RESULT", false);

        final ImageButton resetButton = (ImageButton) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (victory) {
                    resetButton.setImageResource(R.drawable.cookie2);
                } else {
                    resetButton.setImageResource(R.drawable.cookie1);
                }
                startActivity(new Intent(EndGameActivity.this, GameActivity.class));
            }
        });

        TextView msg = (TextView) findViewById(R.id.msg);
        msg.setTypeface(Typeface.createFromAsset(getAssets(), "segoepr.ttf"));
        if (victory) {
            resetButton.setImageResource(R.drawable.cookie1);
            msg.setText("VICTORY!");
        } else {
            resetButton.setImageResource(R.drawable.cookie3);
            msg.setText("DEFEAT!");
        }
    }
}
