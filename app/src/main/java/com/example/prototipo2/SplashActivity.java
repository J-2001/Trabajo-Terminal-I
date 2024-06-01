package com.example.prototipo2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.splash_logo);

        // Cargar las animaciones
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_scale);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_fade_in);

        // Aplicar las animaciones al logotipo
        logo.startAnimation(scaleAnimation);
        logo.startAnimation(fadeInAnimation);

        new Handler().postDelayed(() -> {
            // Cargar la animación fade_out
            Animation fadeOutAnimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.logo_fade_out);
            logo.startAnimation(fadeOutAnimation);

            // Iniciar la actividad principal después del tiempo de espera y animación fade_out
            new Handler().postDelayed(() -> {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }, fadeOutAnimation.getDuration());
        }, SPLASH_DISPLAY_LENGTH - 1000); // Iniciar fade out un segundo antes del fin de SPLASH_DISPLAY_LENGTH
    }
}
