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
        float huellaCarbono = huella.getTotalHuellaCarbono(getApplicationContext());

        TextView tv01 = this.findViewById(R.id.fourth_tv_11);
        tv01.setText(getString(R.string.fourth_tv_11, huellaCarbono));

        float[] equivalencias = getEquivalencias(huellaCarbono);

        TextView tv02 = this.findViewById(R.id.fourth_tv_12);
        tv02.setText(getString(R.string.fourth_tv_12, equivalencias[0]));

        TextView tv03 = this.findViewById(R.id.fourth_tv_13);
        tv03.setText(getString(R.string.fourth_tv_13, equivalencias[1]));

        TextView tv04 = this.findViewById(R.id.fourth_tv_14);
        tv04.setText(getString(R.string.fourth_tv_14, equivalencias[2]));

        TextView tv05 = this.findViewById(R.id.fourth_tv_15);
        tv05.setText(getString(R.string.fourth_tv_15, equivalencias[3]));

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

    public float[] getEquivalencias(float gco2e) {
        float[] equivalencias = {0, 0, 0, 0};
        equivalencias[0] = gco2e / 2347.69814F;
        equivalencias[1] = equivalencias[0] * 9.73577129F;
        equivalencias[2] = gco2e / 3031.35524F;
        equivalencias[3] = gco2e / 1.96814494F;
        return equivalencias;
    }
}
/*
https://espanol.epa.gov/la-energia-y-el-medioambiente/calculador-de-equivalencias-de-gases-de-efecto-invernadero

8887 gCO2e = 1 galon gasolina
8887 gCO2e = 3.78541 litros gasolina
*2347.69814 gCO2e = 1 litro gasolina

1 galon gasolina = 22.9 millas recorridas por un vehiculo de pasajeros (4 llantas y 2 ejes)
3.78541 litros gasolina = 22.9 millas recorridas ""
1 litro gasolina = 6.04954285 millas recorridas ""
(1 milla = 1.60934 kilometros)
*1 litro gasolina = 9.73577129 km recorridos ""

22000 gCO2e = 1 cilindro de propano
22000 gCO2e = 16 libras de ""
(1 libra = 0.453592 kilogramos)
22000 gCO2e = 7.25748 kilos de ""
*3031.35524 gCO2e = 1 kilo de ""

0.8927348 gCO2e = 1 libra carbon quemado
0.8927348 gCO2e = 0.453592 kilogramos carbon ""
*1.96814494 gC02e = 1 kilogramo carbon ""
*/