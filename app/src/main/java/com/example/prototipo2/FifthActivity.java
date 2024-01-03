package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FifthActivity extends AppCompatActivity {

    private static final String root_frag = "root_fragment";
    private static final int limit = 2;
    private LinearLayout layout;
    private Button button;
    private List<Integer> displayed;
    private int number;
    private boolean first;

    private final OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            layout.removeView(button);
            switch (number) {
                case 1:
                    loadFragment(new Consejo01Fragment());
                    break;
                case 2:
                    loadFragment(new Consejo02Fragment());
                    break;
            }
            number = newNumber();
            if (number != -1) {
                button = new Button(layout.getContext());
                button.setText(getString(R.string.fifth_btn, number));
                button.setOnClickListener(listener);
                layout.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        layout = this.findViewById(R.id.fifth_linlay);
        displayed = new ArrayList<>();
        number = newNumber();
        button = new Button(this);
        button.setText(getString(R.string.fifth_btn, number));
        button.setOnClickListener(listener);
        layout.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        first = true;
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (first)  {
            ft.add(R.id.fifth_frmlay, fragment);
            fm.popBackStack(root_frag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.addToBackStack(root_frag);
            first = false;
        } else {
            ft.replace(R.id.fifth_frmlay, fragment);
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public int newNumber() {
        Random random = new Random();
        int r;
        while (true) {
            r = random.nextInt(limit) + 1;
            if (displayed.size() == limit) {
                return -1;
            } else if (!displayed.contains(r)) {
                displayed.add(r);
                return r;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}