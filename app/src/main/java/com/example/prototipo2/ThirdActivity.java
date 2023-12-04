package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Llenado de las TableLayout
        TableLayout tabLay01 = this.findViewById(R.id.third_tablay_01);
        ArrayList<TableRow> tabLay01_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay01_Rows_TextViews = new ArrayList<>();
        Bateria bateria = new Bateria(getApplicationContext());
        int x = 0;
        boolean first = true; // Hacer el texto en negrita para la primer columna
        for (String row : bateria.getAllRows().split(";")) {
            tabLay01_Rows.add(new TableRow(this));
            tabLay01_Rows.get(x).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tabLay01_Rows_TextViews.add(new ArrayList<>());
            int y = 0;
            for (String r : row.split("_")) {
                tabLay01_Rows_TextViews.get(x).add(new TextView(this));
                tabLay01_Rows_TextViews.get(x).get(y).setText(r);
                tabLay01_Rows_TextViews.get(x).get(y).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(y));
                y += 1;
            }
            tabLay01.addView(tabLay01_Rows.get(x), new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            x += 1;
        }

    }
}