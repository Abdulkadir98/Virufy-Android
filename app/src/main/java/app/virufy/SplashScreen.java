package app.virufy;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    TextView message;

    int pos = 0;
    String s = "Fighting COVID one breath at a time.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.activity_splash_screen);
        message = findViewById(R.id.message);

//        startActivity(new Intent(SplashScreen.this, Consent_Study.class));
        new CountDownTimer(108 * s.length(), 65) {
            @Override
            public void onTick(long l) {
                pos++;
                if(pos <= s.length())
                message.setText(s.substring(0, pos));
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashScreen.this, GetStartedPage.class));
            }
        }.start();
    }
}