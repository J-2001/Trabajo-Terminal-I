package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class FourthActivity extends AppCompatActivity {

    private boolean btn01_status = false;
    private static final int textSize = 17;
    private static final int horizontalMargin = 36;
    private static final int verticalMargin = 5;

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

        ImageView iv01 = this.findViewById(R.id.fourth_iv_01);
        tv02.measure(0, 0);
        iv01.getLayoutParams().height = tv02.getMeasuredHeight();
        iv01.requestLayout();


        ImageView iv02 = this.findViewById(R.id.fourth_iv_02);
        tv03.measure(0, 0);
        iv02.getLayoutParams().height = tv03.getMeasuredHeight();
        iv02.requestLayout();

        ImageView iv03 = this.findViewById(R.id.fourth_iv_03);
        tv04.measure(0, 0);
        iv03.getLayoutParams().height = tv04.getMeasuredHeight();
        iv03.requestLayout();

        ImageView iv04 = this.findViewById(R.id.fourth_iv_04);
        tv05.measure(0, 0);
        iv04.getLayoutParams().height = tv05.getMeasuredHeight();
        iv04.requestLayout();

        String rows = huella.getAllRows(getApplicationContext());

        TableLayout tabLay01 = this.findViewById(R.id.fourth_tablay_01);
        ArrayList<TableRow> tabLay01_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay01_Rows_TextViews = new ArrayList<>();
        boolean first = true;
        int x = 0;

        for (String row : rows.split(";")) {
            tabLay01_Rows.add(new TableRow(this));
            TableLayout.LayoutParams tllp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            tllp.setMargins(0, 0, 0, dpToPx(verticalMargin));
            tabLay01_Rows.get(x).setLayoutParams(tllp);

            tabLay01_Rows_TextViews.add(new ArrayList<>());

            tabLay01_Rows_TextViews.get(x).add(new TextView(this));

            if (first) {
                tabLay01_Rows_TextViews.get(x).get(0).setText(getString(R.string.fourth_tl_tv_01));
                tabLay01_Rows_TextViews.get(x).get(0).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(x).get(0).setTextSize(textSize+1);
                tabLay01_Rows_TextViews.get(x).get(0).setTypeface(tabLay01_Rows_TextViews.get(x).get(0).getTypeface(), Typeface.BOLD);
                tabLay01_Rows_TextViews.get(x).get(0).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(0));

                tabLay01_Rows_TextViews.get(x).add(new TextView(this));
                tabLay01_Rows_TextViews.get(x).get(1).setText(getString(R.string.fourth_tl_tv_02));
                TableRow.LayoutParams trlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                trlp.setMargins(dpToPx(horizontalMargin), 0, dpToPx(horizontalMargin), 0);
                tabLay01_Rows_TextViews.get(x).get(1).setLayoutParams(trlp);
                tabLay01_Rows_TextViews.get(x).get(1).setTextSize(textSize+1);
                tabLay01_Rows_TextViews.get(x).get(1).setTypeface(tabLay01_Rows_TextViews.get(x).get(1).getTypeface(), Typeface.BOLD);
                tabLay01_Rows_TextViews.get(x).get(1).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(1));

                tabLay01_Rows_TextViews.get(x).add(new TextView(this));
                tabLay01_Rows_TextViews.get(x).get(2).setText(getString(R.string.fourth_tl_tv_03));
                TableRow.LayoutParams trlp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                trlp1.setMargins(0, 0, dpToPx(horizontalMargin), 0);
                tabLay01_Rows_TextViews.get(x).get(2).setLayoutParams(trlp1);
                tabLay01_Rows_TextViews.get(x).get(2).setTextSize(textSize+1);
                tabLay01_Rows_TextViews.get(x).get(2).setTypeface(tabLay01_Rows_TextViews.get(x).get(2).getTypeface(), Typeface.BOLD);
                tabLay01_Rows_TextViews.get(x).get(2).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(2));

                tabLay01_Rows_TextViews.get(x).add(new TextView(this));
                tabLay01_Rows_TextViews.get(x).get(3).setText(getString(R.string.fourth_tl_tv_04));
                tabLay01_Rows_TextViews.get(x).get(3).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(x).get(3).setTextSize(textSize+1);
                tabLay01_Rows_TextViews.get(x).get(3).setTypeface(tabLay01_Rows_TextViews.get(x).get(3).getTypeface(), Typeface.BOLD);
                tabLay01_Rows_TextViews.get(x).get(3).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(3));
                first = false;
            } else {
                String[] r = row.split(",");
                Escaneo escaneo = new Escaneo(getApplicationContext());
                long[] timeStamps = escaneo.getScanTimeStamps(r[1]);
                DateHandler dh = new DateHandler();
                String videoStreaming = escaneo.getScanVideoStreaming(r[1]);

                tabLay01_Rows_TextViews.get(x).get(0).setText(dh.timeStampToFormattedString(timeStamps[0]));
                tabLay01_Rows_TextViews.get(x).get(0).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(x).get(0).setTextSize(textSize);
                tabLay01_Rows_TextViews.get(x).get(0).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(0));

                tabLay01_Rows_TextViews.get(x).add(new TextView(this));
                tabLay01_Rows_TextViews.get(x).get(1).setText(dh.timeStampToFormattedString(timeStamps[1]));
                tabLay01_Rows_TextViews.get(x).get(1).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(x).get(1).setTextSize(textSize);
                tabLay01_Rows_TextViews.get(x).get(1).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(1));

                tabLay01_Rows_TextViews.get(x).add(new TextView(this));
                tabLay01_Rows_TextViews.get(x).get(2).setText(videoStreaming);
                tabLay01_Rows_TextViews.get(x).get(2).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(x).get(2).setTextSize(textSize);
                tabLay01_Rows_TextViews.get(x).get(2).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(2));

                tabLay01_Rows_TextViews.get(x).add(new TextView(this));
                tabLay01_Rows_TextViews.get(x).get(3).setText(r[2]);
                tabLay01_Rows_TextViews.get(x).get(3).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(x).get(3).setTextSize(textSize);
                tabLay01_Rows_TextViews.get(x).get(3).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(x).addView(tabLay01_Rows_TextViews.get(x).get(3));
            }

            tabLay01.addView(tabLay01_Rows.get(x));
            x += 1;
        }

        Button btn01 = this.findViewById(R.id.fourth_btn_01);

        btn01.setOnClickListener(v -> {
            if (btn01_status) {
                tabLay01.setVisibility(View.GONE);
                btn01.setText(getString(R.string.fourth_btn_01_0));
                btn01_status = false;
            } else {
                tabLay01.setVisibility(View.VISIBLE);
                btn01.setText(getString(R.string.fourth_btn_01_1));
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

    public int dpToPx(int dp) {
        float dpi = getResources().getDisplayMetrics().density;
        return (int) (dp * dpi + 0.5f);
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