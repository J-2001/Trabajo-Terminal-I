package com.example.prototipo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ThirdActivity extends AppCompatActivity {

    private final List<String> videoStreaming = Arrays.asList("Netflix", "Disney+", "Star+", "Prime Video", "Max", "Crunchyroll", "ViX");
    private static final int textSize = 12;
    private int[] pointer = {0, 0};
    private ArrayList<Chip> chips01 = new ArrayList<>();
    private ArrayList<Chip> chips02 = new ArrayList<>();
    private ArrayList<Chip> chips03 = new ArrayList<>();
    private ArrayList<Chip> chips04 = new ArrayList<>();

    private boolean btn01_status;
    private boolean btn02_status;
    private boolean btn03_status;
    private boolean btn04_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        LineChart lineChart = this.findViewById(R.id.third_chart_01);
        lineChart.getXAxis().setValueFormatter(new TimeStampAxisValueFormatter());
        lineChart.getXAxis().setLabelRotationAngle(270);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        Bateria bateria = new Bateria(getApplicationContext());
        Map<Long, Integer> data = bateria.getAllRowsData();
        Map<Float, Integer> dataF = formatter(data);
        for (Float f : dataF.keySet()) {
            entries.add(new Entry(f, dataF.get(f)));
        }
        Map<Long, Integer> test = new LinkedHashMap<>();
        test.put(1714109070663L, 1);
        test.put(1714109170663L, 3);
        test.put(1714109270663L, 1);
        test.put(1714109370663L, 4);
        Map<Float, Integer> testF = formatter(test);
        for (Float f : testF.keySet()) {
            entries.add(new Entry(f, testF.get(f)));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "MyLabel");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();

        Escaneo escaneo = new Escaneo(getApplicationContext());

        ChipGroup chipgrp01 = this.findViewById(R.id.third_chipgrp_01);

        for (int i = 0; i < videoStreaming.size(); i++) {
            chips01.add(new Chip(this));
            chips01.get(i).setText(videoStreaming.get(i));
            chips01.get(i).setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
            chips01.get(i).setTextSize(textSize);
            chips01.get(i).setChipDrawable(ChipDrawable.createFromAttributes(this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));
            chipgrp01.addView(chips01.get(i));
        }

        chipgrp01.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                Log.i("Pruebas(01): ", list.toString());
                if (pointer[1] == 0) {
                    if (list.isEmpty()) {
                        lineChart.setData(lineData);
                        lineChart.invalidate();
                    } else {
                        LineData nLineData = new LineData();
                        for (int id : list) {
                            List<Entry> nEntries = new ArrayList<>();
                            Map<Long, Integer> nData = escaneo.getScansDataFilteredByVideoStreaming(videoStreaming.get(id-1));
                            /*if (nData.isEmpty()) {
                                chips01.get(id-1).setEnabled(false);
                                return;
                            }*/
                            Map<Float, Integer> nDataF = formatter(nData);
                            for (Float f : nDataF.keySet()) {
                                nEntries.add(new Entry(f, nDataF.get(f)));
                            }
                            LineDataSet nLineDataSet = new LineDataSet(nEntries, videoStreaming.get(id-1));
                            nLineData.addDataSet(nLineDataSet);
                            if (id == 1) {
                                List<Entry> xentries = new ArrayList<>();
                                Map<Long, Integer> ntest = new LinkedHashMap<>();
                                ntest.put(1714109070663L, 4);
                                ntest.put(1714109170663L, 3);
                                ntest.put(1714109270663L, 2);
                                ntest.put(1714109370663L, 1);
                                Map<Float, Integer> ntestF = formatter(ntest);
                                for (Float f : ntestF.keySet()) {
                                    xentries.add(new Entry(f, ntestF.get(f)));
                                }

                                LineDataSet xlineDataSet = new LineDataSet(xentries, "MyLabel22");
                                nLineData.addDataSet(xlineDataSet);
                            }
                            if (id == 2) {
                                List<Entry> xentries = new ArrayList<>();
                                Map<Long, Integer> ntest = new LinkedHashMap<>();
                                ntest.put(1714109070663L, 1);
                                ntest.put(1714109170663L, 2);
                                ntest.put(1714109270663L, 3);
                                ntest.put(1714109370663L, 5);
                                Map<Float, Integer> ntestF = formatter(ntest);
                                for (Float f : ntestF.keySet()) {
                                    xentries.add(new Entry(f, ntestF.get(f)));
                                }

                                LineDataSet xlineDataSet = new LineDataSet(xentries, "MyLabel33");
                                nLineData.addDataSet(xlineDataSet);
                            }
                        }
                        lineChart.setData(nLineData);
                        lineChart.invalidate();
                    }
                }
            }
        });

        ChipGroup chipgrp02 = this.findViewById(R.id.third_chipgrp_02);
        ChipGroup chipgrp03 = this.findViewById(R.id.third_chipgrp_03);
        ChipGroup chipgrp04 = this.findViewById(R.id.third_chipgrp_04);

        Button btn01 = this.findViewById(R.id.third_btn_01);
        btn01.setOnClickListener(v -> {
            if (pointer[1] != 0) {
                pointer[0] = pointer[1];
                pointer[1] = 0;
                clearFilters();
            }
            chipgrp01.setVisibility(View.VISIBLE);
            chipgrp02.setVisibility(View.GONE);
            chipgrp03.setVisibility(View.GONE);
            chipgrp04.setVisibility(View.GONE);
        });

        Button btn02 = this.findViewById(R.id.third_btn_02);
        btn02.setOnClickListener(v -> {
            chipgrp01.setVisibility(View.GONE);
            chipgrp02.setVisibility(View.VISIBLE);
            chipgrp03.setVisibility(View.GONE);
            chipgrp04.setVisibility(View.GONE);
        });

        Button btn03 = this.findViewById(R.id.third_btn_03);
        btn03.setOnClickListener(v -> {
            chipgrp01.setVisibility(View.GONE);
            chipgrp02.setVisibility(View.GONE);
            chipgrp03.setVisibility(View.VISIBLE);
            chipgrp04.setVisibility(View.GONE);
        });

        Button btn04 = this.findViewById(R.id.third_btn_04);
        btn04.setOnClickListener(v -> {
            chipgrp01.setVisibility(View.GONE);
            chipgrp02.setVisibility(View.GONE);
            chipgrp03.setVisibility(View.GONE);
            chipgrp04.setVisibility(View.VISIBLE);
        });

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

    private void clearFilters() {
        switch (pointer[0]) {
            case 0:
                for (Chip c : chips01) {
                    c.setChecked(false);
                }
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    private Map<Float, Integer> formatter(Map<Long, Integer> orig) {
        Map<Float, Integer> formatted = new LinkedHashMap<>();
        for (Long l : orig.keySet()) {
            String key = l.toString().substring(2, 10);
            formatted.put(Float.valueOf(key), orig.get(l));
        }
        return formatted;
    }

    private int dpToPx(int dp) {
        float dpi = getResources().getDisplayMetrics().density;
        return (int) (dp * dpi + 0.5f);
    }

}