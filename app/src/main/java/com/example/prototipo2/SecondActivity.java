package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;

public class SecondActivity extends AppCompatActivity {

    private Button btnTest;
    private boolean btnTest_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        CoordinatorLayout coordinatorLayout = this.findViewById(R.id.secondActivity);

        ImageButton ibtnNetflix = this.findViewById(R.id.second_ibtn_netflix);

        btnTest = this.findViewById(R.id.second_btn_test);

        btnTest_status = false;

        Intent intent = new Intent(getApplicationContext(), Analizador.class);

        Intent netflix = getPackageManager().getLaunchIntentForPackage("com.netflix.mediaclient");//"com.google.android.youtube");//

        ibtnNetflix.setOnClickListener(v -> {
            if (!btnTest_status) {
                if (netflix != null) {
                    ibtnNetflix.setClickable(false);
                    btnTest.setVisibility(View.VISIBLE);
                    btnTest_status = true;
                    startForegroundService(intent);
                    startActivity(netflix);
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No se pudo abrir Netflix!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

        btnTest.setOnClickListener(v -> {
            if (btnTest_status) {
                stopService(intent);
                btnTest_status = false;
                btnTest.setVisibility(View.GONE);
                ibtnNetflix.setClickable(true);
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Escaneo Terminado!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

    }
}