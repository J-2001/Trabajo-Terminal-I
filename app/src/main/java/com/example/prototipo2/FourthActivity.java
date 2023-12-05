package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

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

        // Huella de Carbono Total
        Huella huella = new Huella();
        TextView tv01 = this.findViewById(R.id.fourth_tv_01);
        tv01.setText(huella.getTotalHuellaCarbono(getApplicationContext()) + " gCO2e");

        // Llenado de las TableLayout
        TableLayout tabLay01 = this.findViewById(R.id.fourth_tablay_01);
        ArrayList<TableRow> tabLay01_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay01_Rows_TextViews = new ArrayList<>();
        int x = 0;
        boolean first = true; // Hacer el texto en negrita para la primer columna
        for (String row : huella.getAllRows(getApplicationContext()).split(";")) {
            tabLay01_Rows.add(new TableRow(this));
            tabLay01_Rows.get(x).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tabLay01_Rows_TextViews.add(new ArrayList<>());
            int y = 0;
            for (String r : row.split(",")) {
                tabLay01_Rows_TextViews.get(x).add(new TextView(this));
                tabLay01_Rows_TextViews.get(x).get(y).setText(r);
                tabLay01_Rows_TextViews.get(x).get(y).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(y));
                y += 1;
            }
            tabLay01.addView(tabLay01_Rows.get(x), new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            x += 1;
        }

        // Buttons
        Button btn01 = this.findViewById(R.id.fourth_btn_01);
        btn01_status = false;

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn01_status) {
                    tabLay01.setVisibility(View.GONE);
                    btn01.setText("Ver Huella por Escaneo");
                    btn01_status = false;
                } else {
                    tabLay01.setVisibility(View.VISIBLE);
                    btn01.setText("Ocultar Huella por Escaneo");
                    btn01_status = true;
                }
            }
        });
    }
}