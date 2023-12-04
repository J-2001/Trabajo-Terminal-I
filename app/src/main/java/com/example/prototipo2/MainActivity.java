package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Espacio para borrar las bases de datos (Testing)
         */

        Usuario usuario = new Usuario(getApplicationContext(), this);

        Button btn01 = this.findViewById(R.id.main_btn_01);

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secondActivity = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(secondActivity);
            }
        });

    }
}