package info.androidhive.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Subbareddy on 7/2/2017.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Preferences.loadPreferences(this);

        Runnable r = new Runnable() {

            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    if (Preferences.loginStatus) {

                        Preferences.loadPreferences(SplashActivity.this);
                        Intent intent = new Intent(SplashActivity.this, SlideMenuActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    } else {

                        Preferences.loadPreferences(SplashActivity.this);
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }
                }

            }
        };

        Thread t = new Thread(r);
        t.start();
    }
}
