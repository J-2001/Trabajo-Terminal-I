package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {

    private boolean btnTest_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button btnTest = this.findViewById(R.id.second_btn_test);

        btnTest_status = false;

        Intent intent = new Intent(getApplicationContext(), Analizador.class);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnTest_status) {
                    stopService(intent);
                    btnTest.setText("Iniciar Escaneo (Test)");
                    btnTest_status = false;
                } else {
                    startService(intent);
                    btnTest.setText("Detener Escaneo (Test)");
                    btnTest_status = true;
                }
            }
        });
    }
}