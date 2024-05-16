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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FourthActivity extends AppCompatActivity {

    private boolean btn01_status = false;
    private static final int textSize = 11;
    private static final int horizontalMargin1 = 1;
    private static final int horizontalMargin2 = 1;
    private static final int verticalMargin = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        Huella huella = new Huella(getApplicationContext());
        float huellaCarbono = huella.getTotalHuellaCarbono();

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

        List<String> rows = new ArrayList<>(Arrays.asList(huella.getAllRows().split(";")));
        rows.removeAll(Collections.singletonList(""));

        TableLayout tabLay01 = this.findViewById(R.id.fourth_tablay_01);
        ArrayList<TableRow> tabLay01_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay01_Rows_TextViews = new ArrayList<>();
        boolean first = true;

        Map<String, Float>  hashMap = new HashMap<>();

        for (int i = 0; i <= rows.size(); i++) {
            tabLay01_Rows.add(new TableRow(this));
            TableLayout.LayoutParams tllp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            tllp.setMargins(0, 0, 0, dpToPx(verticalMargin));
            tabLay01_Rows.get(i).setLayoutParams(tllp);

            tabLay01_Rows_TextViews.add(new ArrayList<>());

            tabLay01_Rows_TextViews.get(i).add(new TextView(this));

            if (first) {
                tabLay01_Rows_TextViews.get(i).get(0).setText(getString(R.string.fourth_tl_tv_01));
                tabLay01_Rows_TextViews.get(i).get(0).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(i).get(0).setTextSize(textSize+1);
                tabLay01_Rows_TextViews.get(i).get(0).setTypeface(tabLay01_Rows_TextViews.get(i).get(0).getTypeface(), Typeface.BOLD);
                tabLay01_Rows_TextViews.get(i).get(0).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(i).addView(tabLay01_Rows_TextViews.get(i).get(0));

                tabLay01_Rows_TextViews.get(i).add(new TextView(this));
                tabLay01_Rows_TextViews.get(i).get(1).setText(getString(R.string.fourth_tl_tv_02));
                TableRow.LayoutParams trlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                trlp.setMargins(dpToPx(horizontalMargin1), 0, 0, 0);
                tabLay01_Rows_TextViews.get(i).get(1).setLayoutParams(trlp);
                tabLay01_Rows_TextViews.get(i).get(1).setTextSize(textSize+1);
                tabLay01_Rows_TextViews.get(i).get(1).setTypeface(tabLay01_Rows_TextViews.get(i).get(1).getTypeface(), Typeface.BOLD);
                tabLay01_Rows_TextViews.get(i).get(1).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(i).addView(tabLay01_Rows_TextViews.get(i).get(1));

                tabLay01_Rows_TextViews.get(i).add(new TextView(this));
                tabLay01_Rows_TextViews.get(i).get(2).setText(getString(R.string.fourth_tl_tv_03));
                TableRow.LayoutParams trlp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                trlp1.setMargins(dpToPx(horizontalMargin2), 0, dpToPx(horizontalMargin2), 0);
                tabLay01_Rows_TextViews.get(i).get(2).setLayoutParams(trlp1);
                tabLay01_Rows_TextViews.get(i).get(2).setTextSize(textSize+1);
                tabLay01_Rows_TextViews.get(i).get(2).setTypeface(tabLay01_Rows_TextViews.get(i).get(2).getTypeface(), Typeface.BOLD);
                tabLay01_Rows_TextViews.get(i).get(2).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(i).addView(tabLay01_Rows_TextViews.get(i).get(2));

                tabLay01_Rows_TextViews.get(i).add(new TextView(this));
                tabLay01_Rows_TextViews.get(i).get(3).setText(getString(R.string.fourth_tl_tv_04));
                tabLay01_Rows_TextViews.get(i).get(3).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(i).get(3).setTextSize(textSize+1);
                tabLay01_Rows_TextViews.get(i).get(3).setTypeface(tabLay01_Rows_TextViews.get(i).get(3).getTypeface(), Typeface.BOLD);
                tabLay01_Rows_TextViews.get(i).get(3).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(i).addView(tabLay01_Rows_TextViews.get(i).get(3));
                first = false;
            } else {
                String[] r = rows.get(i-1).split(",");
                Escaneo escaneo = new Escaneo(getApplicationContext());
                long[] timeStamps = escaneo.getScanTimeStamps(r[0]);
                DateHandler dh = new DateHandler();
                String videoStreaming = escaneo.getScanVideoStreaming(r[0]);
                float footprint = Float.parseFloat(r[1]);
                if (footprint > 0) {
                    if (hashMap.containsKey(videoStreaming)) {
                        hashMap.put(videoStreaming, hashMap.get(videoStreaming) + footprint);
                    } else {
                        hashMap.put(videoStreaming, footprint);
                    }
                }

                tabLay01_Rows_TextViews.get(i).get(0).setText(dh.timeStampToFormattedString(timeStamps[0]));
                tabLay01_Rows_TextViews.get(i).get(0).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(i).get(0).setTextSize(textSize);
                tabLay01_Rows_TextViews.get(i).get(0).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(i).addView(tabLay01_Rows_TextViews.get(i).get(0));

                tabLay01_Rows_TextViews.get(i).add(new TextView(this));
                tabLay01_Rows_TextViews.get(i).get(1).setText(dh.timeStampToFormattedString(timeStamps[1]));
                tabLay01_Rows_TextViews.get(i).get(1).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(i).get(1).setTextSize(textSize);
                tabLay01_Rows_TextViews.get(i).get(1).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(i).addView(tabLay01_Rows_TextViews.get(i).get(1));

                tabLay01_Rows_TextViews.get(i).add(new TextView(this));
                tabLay01_Rows_TextViews.get(i).get(2).setText(videoStreaming);
                tabLay01_Rows_TextViews.get(i).get(2).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(i).get(2).setTextSize(textSize);
                tabLay01_Rows_TextViews.get(i).get(2).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(i).addView(tabLay01_Rows_TextViews.get(i).get(2));

                tabLay01_Rows_TextViews.get(i).add(new TextView(this));
                tabLay01_Rows_TextViews.get(i).get(3).setText(r[1]);
                tabLay01_Rows_TextViews.get(i).get(3).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay01_Rows_TextViews.get(i).get(3).setTextSize(textSize);
                tabLay01_Rows_TextViews.get(i).get(3).setGravity(Gravity.CENTER);
                tabLay01_Rows.get(i).addView(tabLay01_Rows_TextViews.get(i).get(3));
            }

            tabLay01.addView(tabLay01_Rows.get(i));
        }

        Button btn01 = this.findViewById(R.id.fourth_btn_01);

        if (rows.isEmpty()) {
            btn01.setVisibility(View.GONE);
        }

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

        PieChart chart = this.findViewById(R.id.fourth_chart_01);
        chart.getDescription().setEnabled(false);

        Map<Float, String> treeMap = new TreeMap<>(Collections.reverseOrder());
        for (String s : hashMap.keySet()) {
            float v = hashMap.get(s);
            if (treeMap.containsKey(v)) {
                treeMap.put(v + 0.000001f, s);
            } else {
                treeMap.put(v, s);
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (float f : treeMap.keySet()) {
            entries.add(new PieEntry(f, treeMap.get(f)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "MyLabel");
        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.invalidate();
    }

    private float[] getEquivalencias(float gco2e) {
        float[] equivalencias = {0, 0, 0, 0};
        equivalencias[0] = gco2e / 2347.69814F;
        equivalencias[1] = equivalencias[0] * 9.73577129F;
        equivalencias[2] = gco2e / 3031.35524F;
        equivalencias[3] = gco2e / 1.96814494F;
        return equivalencias;
    }

    private int dpToPx(int dp) {
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