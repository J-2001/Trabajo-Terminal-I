package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;

public class SecondActivity extends AppCompatActivity {

    private Button btnTest;
    private boolean btnTest_status;
    private Intent netflix;
    private Intent disneyPlus;
    private Intent hbomax;
    private Intent primeVideo;
    private Intent starPlus;
    private Intent crunchyroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        CoordinatorLayout coordinatorLayout = this.findViewById(R.id.secondActivity);

        ImageButton ibtnNetflix = this.findViewById(R.id.second_ibtn_netflix);
        ImageButton ibtnDisneyPlus = this.findViewById(R.id.second_ibtn_disneyplus);
        ImageButton ibtnHBOmax = this.findViewById(R.id.second_ibtn_hbomax);
        ImageButton ibtnPrimeVideo = this.findViewById(R.id.second_ibtn_primevideo);
        ImageButton ibtnStarPlus = this.findViewById(R.id.second_ibtn_starplus);
        ImageButton ibtnCrunchyroll = this.findViewById(R.id.second_ibtn_crunchyroll);

        btnTest = this.findViewById(R.id.second_btn_test);
        btnTest_status = false;

        Intent intent = new Intent(getApplicationContext(), Analizador.class);

        ibtnNetflix.setOnClickListener(v -> {
            if (!btnTest_status) {
                netflix = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_netflix));

                if (netflix != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnHBOmax.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    btnTest.setVisibility(View.VISIBLE);
                    btnTest_status = true;
                    startForegroundService(intent);
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
            if (!btnTest_status) {
                disneyPlus = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_disneyplus));

                if (disneyPlus != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnHBOmax.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    btnTest.setVisibility(View.VISIBLE);
                    btnTest_status = true;
                    startForegroundService(intent);
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

        ibtnHBOmax.setOnClickListener(v -> {
            if (!btnTest_status) {
                hbomax = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_hbomax));

                if (hbomax != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnHBOmax.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    btnTest.setVisibility(View.VISIBLE);
                    btnTest_status = true;
                    startForegroundService(intent);
                    startActivity(hbomax);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No se pudo abrir HBOmax!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    hbomax = new Intent(Intent.ACTION_VIEW);
                    hbomax.setData(Uri.parse("market://details?id=" + getString(R.string.second_ibtn_hbomax)));
                    startActivity(hbomax);
                }
            }
        });

        ibtnPrimeVideo.setOnClickListener(v -> {
            if (!btnTest_status) {
                primeVideo = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_primevideo));

                if (primeVideo != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnHBOmax.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    btnTest.setVisibility(View.VISIBLE);
                    btnTest_status = true;
                    startForegroundService(intent);
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

        ibtnStarPlus.setOnClickListener(v -> {
            if (!btnTest_status) {
                starPlus = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_starplus));

                if (starPlus != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnHBOmax.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    btnTest.setVisibility(View.VISIBLE);
                    btnTest_status = true;
                    startForegroundService(intent);
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

        ibtnCrunchyroll.setOnClickListener(v -> {
            if (!btnTest_status) {
                crunchyroll = getPackageManager().getLaunchIntentForPackage(getString(R.string.second_ibtn_crunchyroll));

                if (crunchyroll != null) {
                    ibtnNetflix.setClickable(false);
                    ibtnDisneyPlus.setClickable(false);
                    ibtnHBOmax.setClickable(false);
                    ibtnPrimeVideo.setClickable(false);
                    ibtnStarPlus.setClickable(false);
                    ibtnCrunchyroll.setClickable(false);
                    btnTest.setVisibility(View.VISIBLE);
                    btnTest_status = true;
                    startForegroundService(intent);
                    startActivity(crunchyroll);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No se pudo abrir Crunchyroll", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    crunchyroll = new Intent(Intent.ACTION_VIEW);
                    crunchyroll.setData(Uri.parse("market://details?id=" + getString(R.string.second_ibtn_crunchyroll)));
                    startActivity(crunchyroll);
                }
            }
        });

        btnTest.setOnClickListener(v -> {
            if (btnTest_status) {
                stopService(intent);
                btnTest_status = false;
                btnTest.setVisibility(View.GONE);
                ibtnNetflix.setClickable(true);
                ibtnDisneyPlus.setClickable(true);
                ibtnHBOmax.setClickable(true);
                ibtnPrimeVideo.setClickable(true);
                ibtnStarPlus.setClickable(true);
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Escaneo Terminado!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

    }
}