package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThirdActivity extends AppCompatActivity {

    private boolean btn01_status;
    private boolean btn02_status;
    private boolean btn03_status;
    private boolean btn04_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        LineChart chart01 = this.findViewById(R.id.third_chart_01);
        chart01.getXAxis().setValueFormatter(new TimeStampAxisValueFormatter());
        chart01.getXAxis().setLabelRotationAngle(270);
        chart01.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart01.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(111111111000F, 1));//171409011100
        entries.add(new Entry(111111112000F, 3));
        entries.add(new Entry(111111113000F, 1));
        entries.add(new Entry(111111114000F, 4));
        entries.add(new Entry(111111115000F, 1));//171409011100
        entries.add(new Entry(111111116000F, 3));
        entries.add(new Entry(111111117000F, 1));
        entries.add(new Entry(111111118000F, 4));
        Bateria bateria = new Bateria(getApplicationContext());
        Map<Long, Integer> data = bateria.getAllRowsData();
        for (long l : data.keySet()) {
            entries.add(new Entry(l, data.get(l)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "MyLabel");
        LineData lineData = new LineData(dataSet);
        chart01.setData(lineData);
        chart01.invalidate();

        /*/ TableLayouts
        TableLayout tabLay01 = this.findViewById(R.id.third_tablay_01);
        ArrayList<TableRow> tabLay01_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay01_Rows_TextViews = new ArrayList<>();
        int x = 0;
        boolean first = true;
        for (String row : bateria.getLastScan().split(";")) {
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

        TableLayout tabLay02 = this.findViewById(R.id.third_tablay_02);
        ArrayList<TableRow> tabLay02_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay02_Rows_TextViews = new ArrayList<>();
        x = 0;
        first = true;
        for (String row : bateria.getAllRows().split(";")) {
            tabLay02_Rows.add(new TableRow(this));
            tabLay02_Rows.get(x).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tabLay02_Rows_TextViews.add(new ArrayList<>());
            int y = 0;
            for (String r : row.split(",")) {
                tabLay02_Rows_TextViews.get(x).add(new TextView(this));
                tabLay02_Rows_TextViews.get(x).get(y).setText(r);
                tabLay02_Rows_TextViews.get(x).get(y).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                if (first) {
                    tabLay02_Rows_TextViews.get(x).get(y).setTypeface(tabLay02_Rows_TextViews.get(x).get(y).getTypeface(), Typeface.BOLD);
                }
                tabLay02_Rows.get(x).addView(tabLay02_Rows_TextViews.get(x).get(y));
                y += 1;
            }
            first = false;
            tabLay02.addView(tabLay02_Rows.get(x), new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            x += 1;
        }

        TableLayout tabLay03 = this.findViewById(R.id.third_tablay_03);
        ArrayList<TableRow> tabLay03_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay03_Rows_TextViews = new ArrayList<>();
        Escaneo escaneo = new Escaneo(getApplicationContext());
        x = 0;
        first = true;
        for (String row : escaneo.getAllScans().split(";")) {
            tabLay03_Rows.add(new TableRow(this));
            tabLay03_Rows.get(x).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tabLay03_Rows_TextViews.add(new ArrayList<>());
            int y = 0;
            for (String r : row.split(",")) {
                tabLay03_Rows_TextViews.get(x).add(new TextView(this));
                tabLay03_Rows_TextViews.get(x).get(y).setText(r);
                tabLay03_Rows_TextViews.get(x).get(y).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                if (first) {
                    tabLay03_Rows_TextViews.get(x).get(y).setTypeface(tabLay03_Rows_TextViews.get(x).get(y).getTypeface(), Typeface.BOLD);
                }
                tabLay03_Rows.get(x).addView(tabLay03_Rows_TextViews.get(x).get(y));
                y += 1;
            }
            first = false;
            tabLay03.addView(tabLay03_Rows.get(x), new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            x += 1;
        }

        TableLayout tabLay04 = this.findViewById(R.id.third_tablay_04);
        ArrayList<TableRow> tabLay04_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay04_Rows_TextViews = new ArrayList<>();
        Analizador analizador = new Analizador();
        x = 0;
        first = true;
        for (String row : analizador.getAllData(getApplicationContext()).split(";")) {
            tabLay04_Rows.add(new TableRow(this));
            tabLay04_Rows.get(x).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tabLay04_Rows_TextViews.add(new ArrayList<>());
            int y = 0;
            for (String r : row.split(",")) {
                tabLay04_Rows_TextViews.get(x).add(new TextView(this));
                tabLay04_Rows_TextViews.get(x).get(y).setText(r);
                tabLay04_Rows_TextViews.get(x).get(y).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                if (first) {
                    tabLay04_Rows_TextViews.get(x).get(y).setTypeface(tabLay04_Rows_TextViews.get(x).get(y).getTypeface(), Typeface.BOLD);
                }
                tabLay04_Rows.get(x).addView(tabLay04_Rows_TextViews.get(x).get(y));
                y += 1;
            }
            first = false;
            tabLay04.addView(tabLay04_Rows.get(x), new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            x += 1;
        }

        // Buttons
        Button btn01 = this.findViewById(R.id.third_btn_01);
        btn01_status = false;

        btn01.setOnClickListener(v -> {
            if (btn01_status) {
                tabLay01.setVisibility(View.GONE);
                btn01.setText(getString(R.string.third_btn_01_1));
                btn01_status = false;
            } else {
                tabLay01.setVisibility(View.VISIBLE);
                btn01.setText(getString(R.string.third_btn_01_0));
                btn01_status = true;
            }
        });

        Button btn02 = this.findViewById(R.id.third_btn_02);
        btn02_status = false;

        btn02.setOnClickListener(v -> {
            if (btn02_status) {
                tabLay02.setVisibility(View.GONE);
                btn02.setText(getString(R.string.third_btn_02_1));
                btn02_status = false;
            } else {
                tabLay02.setVisibility(View.VISIBLE);
                btn02.setText(getString(R.string.third_btn_02_0));
                btn02_status = true;
            }
        });

        Button btn03 = this.findViewById(R.id.third_btn_03);
        btn03_status = false;

        btn03.setOnClickListener(v -> {
            if (btn03_status) {
                tabLay03.setVisibility(View.GONE);
                btn03.setText(getString(R.string.third_btn_03_1));
                btn03_status = false;
            } else {
                tabLay03.setVisibility(View.VISIBLE);
                btn03.setText(getString(R.string.third_btn_03_0));
                btn03_status = true;
            }
        });

        Button btn04 = this.findViewById(R.id.third_btn_04);
        btn04_status = false;

        btn04.setOnClickListener(v -> {
            if (btn04_status) {
                tabLay04.setVisibility(View.GONE);
                btn04.setText(getString(R.string.third_btn_04_1));
                btn04_status = false;
            } else {
                tabLay04.setVisibility(View.VISIBLE);
                btn04.setText(getString(R.string.third_btn_04_0));
                btn04_status = true;
            }
        });*/
    }


}