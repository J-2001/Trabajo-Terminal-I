package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class SixthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth);

        Button btn = this.findViewById(R.id.sixth_btn);
        btn.setOnClickListener(v -> finish());
    }
}