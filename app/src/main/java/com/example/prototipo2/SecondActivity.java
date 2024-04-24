package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;

public class SecondActivity extends AppCompatActivity {

    private boolean status = false;
    private Intent netflix;
    private Intent disneyPlus;
    private Intent starPlus;
    private Intent primeVideo;
    private Intent max;
    private Intent crunchyroll;
    private Intent vix;
    private Intent analizador;
    CoordinatorLayout coordinatorLayout;
    ImageButton ibtnNetflix;
    ImageButton ibtnDisneyPlus;
    ImageButton ibtnStarPlus;
    ImageButton ibtnPrimeVideo;
    ImageButton ibtnMax;
    ImageButton ibtnCrunchyroll;
    ImageButton ibtnVix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        coordinatorLayout = this.findViewById(R.id.secondActivity);

        ibtnNetflix = this.findViewById(R.id.second_ibtn_netflix);
        ibtnDisneyPlus = this.findViewById(R.id.second_ibtn_disneyplus);
        ibtnStarPlus = this.findViewById(R.id.second_ibtn_starplus);
        ibtnPrimeVideo = this.findViewById(R.id.second_ibtn_primevideo);
        ibtnMax = this.findViewById(R.id.second_ibtn_max);
        ibtnCrunchyroll = this.findViewById(R.id.second_ibtn_crunchyroll);
        ibtnVix = this.findViewById(R.id.second_ibtn_vix);

        analizador = new Intent(getApplicationContext(), Analizador.class);

        ibtnNetflix.setOnClickListener(v -> {
            if (!status) {
                netflix = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_netflix));

                if (netflix != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnMax.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    ibtnVix.setClickable(false);
                    status = true;
                    analizador.putExtra("videostreaming", "Netflix");
                    startForegroundService(analizador);
                    startActivity(netflix);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No se pudo abrir Netflix!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    netflix = new Intent(Intent.ACTION_VIEW);
                    netflix.setData(Uri.parse("market://details?id=" + getString(R.string.second_ibtn_netflix)));
                    startActivity(netflix);
                }
            }
        });

        ibtnDisneyPlus.setOnClickListener(v -> {
            if (!status) {
                disneyPlus = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_disneyplus));

                if (disneyPlus != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnMax.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    ibtnVix.setClickable(false);
                    status = true;
                    analizador.putExtra("videostreaming", "Disney+");
                    startForegroundService(analizador);
                    startActivity(disneyPlus);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No se pudo abrir Disney+!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    disneyPlus = new Intent(Intent.ACTION_VIEW);
                    disneyPlus.setData(Uri.parse("market://details?id=" + getString(R.string.second_ibtn_disneyplus)));
                    startActivity(disneyPlus);
                }
            }
        });

        ibtnStarPlus.setOnClickListener(v -> {
            if (!status) {
                starPlus = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_starplus));

                if (starPlus != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnMax.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    ibtnVix.setClickable(false);
                    status = true;
                    analizador.putExtra("videostreaming", "Star+");
                    startForegroundService(analizador);
                    startActivity(starPlus);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No se pudo abrir Star+!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    starPlus = new Intent(Intent.ACTION_VIEW);
                    starPlus.setData(Uri.parse("market://details?id=" + getString(R.string.second_ibtn_starplus)));
                    startActivity(starPlus);
                }
            }
        });

        ibtnPrimeVideo.setOnClickListener(v -> {
            if (!status) {
                primeVideo = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_primevideo));

                if (primeVideo != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnMax.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    ibtnVix.setClickable(false);
                    status = true;
                    analizador.putExtra("videostreaming", "Prime Video");
                    startForegroundService(analizador);
                    startActivity(primeVideo);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No se pudo abrir Prime Video!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    primeVideo = new Intent(Intent.ACTION_VIEW);
                    primeVideo.setData(Uri.parse("market://details?id=" + getString(R.string.second_ibtn_primevideo)));
                    startActivity(primeVideo);
                }
            }
        });

        ibtnMax.setOnClickListener(v -> {
            if (!status) {
                max = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_max));

                if (max != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnMax.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    ibtnVix.setClickable(false);
                    status = true;
                    analizador.putExtra("videostreaming", "Max");
                    startForegroundService(analizador);
                    startActivity(max);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No se pudo abrir Max!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    max = new Intent(Intent.ACTION_VIEW);
                    max.setData(Uri.parse("market://details?id=" + getString(R.string.second_ibtn_max)));
                    startActivity(max);
                }
            }
        });

        ibtnCrunchyroll.setOnClickListener(v -> {
            if (!status) {
                crunchyroll = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_crunchyroll));

                if (crunchyroll != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnMax.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    ibtnVix.setClickable(false);
                    status = true;
                    analizador.putExtra("videostreaming", "Crunchyroll");
                    startForegroundService(analizador);
                    startActivity(crunchyroll);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No se pudo abrir Crunchyroll!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    crunchyroll = new Intent(Intent.ACTION_VIEW);
                    crunchyroll.setData(Uri.parse("market://details?id=" + getString(R.string.second_ibtn_crunchyroll)));
                    startActivity(crunchyroll);
                }
            }
        });

        ibtnVix.setOnClickListener(v -> {
            if (!status) {
                vix = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_vix));

                if (vix != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnMax.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    ibtnVix.setClickable(false);
                    status = true;
                    analizador.putExtra("videostreaming", "ViX");
                    startForegroundService(analizador);
                    startActivity(vix);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No se pudo abrir ViX!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    vix = new Intent(Intent.ACTION_VIEW);
                    vix.setData(Uri.parse("market://details?id=" + getString(R.string.second_ibtn_vix)));
                    startActivity(vix);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (status) {
            stopService(analizador);
            status = false;
            ibtnNetflix.setClickable(true);
            ibtnDisneyPlus.setClickable(true);
            ibtnStarPlus.setClickable(true);
            ibtnPrimeVideo.setClickable(true);
            ibtnMax.setClickable(true);
            ibtnCrunchyroll.setClickable(true);
            ibtnVix.setClickable(true);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Analizador Terminado!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
}