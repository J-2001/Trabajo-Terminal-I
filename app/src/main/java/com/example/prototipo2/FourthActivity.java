package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class FourthActivity extends AppCompatActivity {

    private boolean btn01_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        Huella huella = new Huella();
        TextView tv01 = this.findViewById(R.id.fourth_tv_01);
        tv01.setText(getString(R.string.fourth_tv_01, huella.getTotalHuellaCarbono(getApplicationContext())));

        TableLayout tabLay01 = this.findViewById(R.id.fourth_tablay_01);
        ArrayList<TableRow> tabLay01_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay01_Rows_TextViews = new ArrayList<>();
        int x = 0;
        boolean first = true;
        for (String row : huella.getAllRows(getApplicationContext()).split(";")) {
            tabLay01_Rows.add(new TableRow(this));
            tabLay01_Rows.get(x).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tabLay01_Rows_TextViews.add(new ArrayList<>());
            int y = 0;
            for (String r : row.split(",")) {
                tabLay01_Rows_TextViews.get(x).add(new TextView(this));
                tabLay01_Rows_TextViews.get(x).get(y).setText(r);
                tabLay01_Rows_TextViews.get(x).get(y).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                if (first) {
                    tabLay01_Rows_TextViews.get(x).get(y).setTypeface(tabLay01_Rows_TextViews.get(x).get(y).getTypeface(), Typeface.BOLD);
                }
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(y));
                y += 1;
            }
            first = false;
            tabLay01.addView(tabLay01_Rows.get(x), new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            x += 1;
        }

        // Buttons
        Button btn01 = this.findViewById(R.id.fourth_btn_01);
        btn01_status = false;

        btn01.setOnClickListener(v -> {
            if (btn01_status) {
                tabLay01.setVisibility(View.GONE);
                btn01.setText(getString(R.string.fourth_btn_01_1));
                btn01_status = false;
            } else {
                tabLay01.setVisibility(View.VISIBLE);
                btn01.setText(getString(R.string.fourth_btn_01_0));
                btn01_status = true;
            }
        });
    }
}