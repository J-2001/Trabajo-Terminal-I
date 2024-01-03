package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class FifthActivity extends AppCompatActivity {

    private final String root_frag = "root_fragment";
    private LinearLayout layout;
    private Button button;
    private int counter;

    private final OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            layout.removeView(button);
            loadFragment();
            counter += 1;
            button = new Button(layout.getContext());
            button.setText(getString(R.string.fifth_btn, counter));
            button.setOnClickListener(listener);
            layout.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        layout = this.findViewById(R.id.fifth_linlay);
        counter = 1;
        button = new Button(this);
        button.setText(getString(R.string.fifth_btn, counter));
        button.setOnClickListener(listener);
        layout.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

    }

    public void loadFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (counter) {
            case 1:
                ft.add(R.id.fifth_frmlay, new Consejo01Fragment());
                fm.popBackStack(root_frag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.addToBackStack(root_frag);

                break;
        }
        ft.commit();
    }

}