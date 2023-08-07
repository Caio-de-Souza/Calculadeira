package com.lima.souza.app.calculadeira;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private ImageView icon, title, logoApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initComponents();
    }

    private void initComponents() {
        icon = findViewById(R.id.icone_splash_screen);
        title = findViewById(R.id.titulo_splash_screen);
        logoApplication = findViewById(R.id.logo_dev);

        initSplashAnimation();
        initTimeToDestroyScreen();
    }

    private void initTimeToDestroyScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                SplashScreen.this.finish();
            }
        }, 5000);
    }

    private void initSplashAnimation() {
        Animation animLeft = setupAnimation(SplashScreen.this, R.anim.animation_left, 800);
        animLeft.setStartOffset(300);

        Animation animRight = setupAnimation(SplashScreen.this, R.anim.animation_right, 800);
        Animation fade = setupAnimation(SplashScreen.this, android.R.anim.fade_in, 2000);
        fade.setStartOffset(1000);

        icon.startAnimation(animRight);
        title.startAnimation(animLeft);
        logoApplication.startAnimation(fade);
    }

    private Animation setupAnimation(Context context, int resource, int duracao) {
        Animation animation = AnimationUtils.loadAnimation(context, resource);
        Interpolator interpolartor = new FastOutSlowInInterpolator();
        animation.setInterpolator(interpolartor);
        animation.setDuration(duracao);
        return animation;
    }
}