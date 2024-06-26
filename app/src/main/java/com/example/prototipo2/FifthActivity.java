package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FifthActivity extends AppCompatActivity {

    private final int limit = 15;
    private final List<Integer> order = new ArrayList<>();
    private int pointer = 0;
    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        Random random = new Random();
        while (order.size() < limit) {
            int r = random.nextInt(limit);
            if (!order.contains(r)) {
                order.add(r);
            }
        }

        updateFragment();

        Button previous = this.findViewById(R.id.fifth_btn_prv);
        Button next = this.findViewById(R.id.fifth_btn_nxt);

        previous.setOnClickListener(v -> {
            pointer -= 1;
            updateFragment();
            if (!next.isEnabled()) {
                next.setEnabled(true);
            }
            if (pointer == 0) {
                previous.setEnabled(false);
            }
        });

        next.setOnClickListener(v -> {
            pointer += 1;
            updateFragment();
            if (!previous.isEnabled()) {
                previous.setEnabled(true);
            }
            if (pointer == limit-1) {
                next.setEnabled(false);
            }
        });

    }

    public void updateFragment() {
        switch (order.get(pointer)) {
            case 0:
                loadFragment(new Consejo01Fragment());
                break;
            case 1:
                loadFragment(new Consejo02Fragment());
                break;
            case 2:
                loadFragment(new Consejo03Fragment());
                break;
            case 3:
                loadFragment(new Consejo04Fragment());
                break;
            case 4:
                loadFragment(new Consejo05Fragment());
                break;
            case 5:
                loadFragment(new Consejo06Fragment());
                break;
            case 6:
                loadFragment(new Consejo07Fragment());
                break;
            case 7:
                loadFragment(new Consejo08Fragment());
                break;
            case 8:
                loadFragment(new Consejo09Fragment());
                break;
            case 9:
                loadFragment(new Consejo10Fragment());
                break;
            case 10:
                loadFragment(new Consejo11Fragment());
                break;
            case 11:
                loadFragment(new Consejo12Fragment());
                break;
            case 12:
                loadFragment(new Consejo13Fragment());
                break;
            case 13:
                loadFragment(new Consejo14Fragment());
                break;
            case 14:
                loadFragment(new Consejo15Fragment());
                break;
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (first) {
            ft.add(R.id.fifth_frmlay, fragment);
            first = false;
        } else {
            ft.replace(R.id.fifth_frmlay, fragment);
        }
        ft.commit();
    }

}