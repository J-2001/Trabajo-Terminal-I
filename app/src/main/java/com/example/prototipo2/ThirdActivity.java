package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    private boolean btn01_status;
    private boolean btn02_status;
    private boolean btn03_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Charts
        LineChart chart01 = this.findViewById(R.id.third_chart_01);
        int[][] data = {{1,1}, {2,2}, {3,3}};
        List<Entry> entries = new ArrayList<Entry>();
        for (int[] d : data) {
            entries.add(new Entry(d[0], d[1]));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        // dataSet.setColor();
        // dataSet.setValueTextColor();
        LineData lineData = new LineData(dataSet);
        chart01.setData(lineData);
        chart01.invalidate();

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

        TableLayout tabLay02 = this.findViewById(R.id.third_tablay_02);
        ArrayList<TableRow> tabLay02_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay02_Rows_TextViews = new ArrayList<>();
        Escaneo escaneo = new Escaneo(getApplicationContext());
        x = 0;
        first = true; // Hacer el texto en negrita para la primer columna
        for (String row : escaneo.getAllScans().split(";")) {
            tabLay02_Rows.add(new TableRow(this));
            tabLay02_Rows.get(x).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tabLay02_Rows_TextViews.add(new ArrayList<>());
            int y = 0;
            for (String r : row.split(",")) {
                tabLay02_Rows_TextViews.get(x).add(new TextView(this));
                tabLay02_Rows_TextViews.get(x).get(y).setText(r);
                tabLay02_Rows_TextViews.get(x).get(y).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay02_Rows.get(x).addView(tabLay02_Rows_TextViews.get(x).get(y));
                y += 1;
            }
            tabLay02.addView(tabLay02_Rows.get(x), new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            x += 1;
        }

        TableLayout tabLay03 = this.findViewById(R.id.third_tablay_03);
        ArrayList<TableRow> tabLay03_Rows = new ArrayList<>();
        ArrayList<ArrayList<TextView>> tabLay03_Rows_TextViews = new ArrayList<>();
        Analizador analizador = new Analizador();
        x = 0;
        first = true; // Hacer el texto en negrita para la primer columna
        for (String row : analizador.getAllData(getApplicationContext()).split(";")) {
            tabLay03_Rows.add(new TableRow(this));
            tabLay03_Rows.get(x).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tabLay03_Rows_TextViews.add(new ArrayList<>());
            int y = 0;
            for (String r : row.split(",")) {
                tabLay03_Rows_TextViews.get(x).add(new TextView(this));
                tabLay03_Rows_TextViews.get(x).get(y).setText(r);
                tabLay03_Rows_TextViews.get(x).get(y).setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tabLay03_Rows.get(x).addView(tabLay03_Rows_TextViews.get(x).get(y));
                y += 1;
            }
            tabLay03.addView(tabLay03_Rows.get(x), new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            x += 1;
        }

        // Buttons
        Button btn01 = this.findViewById(R.id.third_btn_01);
        btn01_status = false;

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn01_status) {
                    tabLay01.setVisibility(View.GONE);
                    btn01.setText("Ver Datos de la Batería");
                    btn01_status = false;
                } else {
                    tabLay01.setVisibility(View.VISIBLE);
                    btn01.setText("Ocultar Datos de la Batería");
                    btn01_status = true;
                }
            }
        });

        Button btn02 = this.findViewById(R.id.third_btn_02);
        btn02_status = false;

        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn02_status) {
                    tabLay02.setVisibility(View.GONE);
                    btn02.setText("Ver Escaneos");
                    btn02_status = false;
                } else {
                    tabLay02.setVisibility(View.VISIBLE);
                    btn02.setText("Ocultar Escaneos");
                    btn02_status = true;
                }
            }
        });

        Button btn03 = this.findViewById(R.id.third_btn_03);
        btn03_status = false;

        btn03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn03_status) {
                    tabLay03.setVisibility(View.GONE);
                    btn03.setText("Ver Datos del Analizador");
                    btn03_status = false;
                } else {
                    tabLay03.setVisibility(View.VISIBLE);
                    btn03.setText("Ocultar Datos del Analizador");
                    btn03_status = true;
                }
            }
        });
    }
}