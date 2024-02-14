package com.example.prototipo2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Bundle extras = getIntent().getExtras();

        LinearLayout layout = this.findViewById(R.id.notif_linlay);

        if (extras != null) {
            int categoria = extras.getInt(getString(R.string.extra_0));
            switch (categoria) {
                case 1:
                    CharSequence cs = "CCPM: " + extras.getFloat(getString(R.string.extra_1_1));
                    TextView tv1 = new TextView(this);
                    tv1.setText(cs);
                    tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout.addView(tv1);
                    cs = "Media: " + extras.getFloat(getString(R.string.extra_1_2));
                    TextView tv2 = new TextView(this);
                    tv2.setText(cs);
                    tv2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout.addView(tv2);
                    cs = "Desv. Est.: " + extras.getFloat(getString(R.string.extra_1_3));
                    TextView tv3 = new TextView(this);
                    tv3.setText(cs);
                    tv3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout.addView(tv3);
                    cs = "P(Z>x): " + extras.getFloat(getString(R.string.extra_1_4));
                    TextView tv4 = new TextView(this);
                    tv4.setText(cs);
                    tv4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout.addView(tv4);

                    break;
            }
        }

        Button btn = new Button(this);
        CharSequence cs = "Atras";
        btn.setText(cs);
        btn.setOnClickListener(v -> {
            finish();
        });
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(btn);
    }
}