package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ThirdActivity extends AppCompatActivity {

    private List<String> videoStreaming;
    private Map<String, Integer> colors;
    private List<Integer> scansIds;
    private static final int textSize = 16;
    private static final int horizontalPadding = 28;
    private static final int verticalPadding = 11;
    private final int duration = 1400;
    private final int[] pointer = {0, 0};
    private ChipGroup chipgrp01;
    private ChipGroup chipgrp02;
    private ChipGroup chipgrp03;
    private ChipGroup chipgrp04;
    private final ArrayList<Chip> chips01 = new ArrayList<>();
    private final ArrayList<Chip> chips02 = new ArrayList<>();
    private final ArrayList<Chip> chips03 = new ArrayList<>();
    private final ArrayList<Chip> chips04 = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        videoStreaming = Arrays.asList(getString(R.string.netflix), getString(R.string.disneyplus), getString(R.string.starplus), getString(R.string.primevideo), getString(R.string.max), getString(R.string.crunchyroll), getString(R.string.vix));

        colors = new HashMap<>();
        colors.put("Default", getColor(R.color.amarillo_oscuro));
        colors.put(videoStreaming.get(0), getColor(R.color.netflix));
        colors.put(videoStreaming.get(1), getColor(R.color.disneyplus));
        colors.put(videoStreaming.get(2), getColor(R.color.starplus));
        colors.put(videoStreaming.get(3), getColor(R.color.primevideo));
        colors.put(videoStreaming.get(4), getColor(R.color.max));
        colors.put(videoStreaming.get(5), getColor(R.color.crunchyroll));
        colors.put(videoStreaming.get(6), getColor(R.color.vix));
        colors.put(getString(R.string.third_chip_03_01), getColor(R.color.verde_oscuro));
        colors.put(getString(R.string.third_chip_03_02), getColor(R.color.rojo_oscuro));
        colors.put(getString(R.string.third_chip_04_01), getColor(R.color.azul));
        colors.put(getString(R.string.third_chip_04_02), getColor(R.color.rojo));

        LineChart lineChart = this.findViewById(R.id.third_chart_01);
        lineChart.getXAxis().setValueFormatter(new TimeStampAxisValueFormatter());
        lineChart.getXAxis().setLabelRotationAngle(315); //315
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setTextSize(11);
        lineChart.getLegend().setFormSize(9);
        lineChart.getLegend().setXEntrySpace(15);
        lineChart.getLegend().setFormToTextSpace(5);
        lineChart.getDescription().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        Bateria bateria = new Bateria(getApplicationContext());
        Map<Long, Integer> data = bateria.getAllRowsData();
        Map<Float, Integer> dataF = formatter(data);
        for (Float f : dataF.keySet()) {
            entries.add(new Entry(f, dataF.get(f)));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Capacidad de la Batería (en microamperios-hora)");
        lineDataSet.setColor(colors.get("Default"));
        lineDataSet.setHighlightEnabled(false);

        LineData lineData = new LineData(lineDataSet);
        lineData.setValueFormatter(new LargeValueFormatter());

        lineChart.setData(lineData);
        lineChart.animateX(duration);

        Escaneo escaneo = new Escaneo(getApplicationContext());

        chipgrp01 = this.findViewById(R.id.third_chipgrp_01);

        for (int i = 0; i < videoStreaming.size(); i++) {
            chips01.add(new Chip(this));
            chips01.get(i).setText(videoStreaming.get(i));
            chips01.get(i).setId(i);
            chips01.get(i).setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
            chips01.get(i).setTextSize(textSize);
            chips01.get(i).setChipDrawable(ChipDrawable.createFromAttributes(this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));
            chips01.get(i).setPadding(dpToPx(horizontalPadding), dpToPx(verticalPadding), dpToPx(horizontalPadding), dpToPx(verticalPadding));
            chipgrp01.addView(chips01.get(i));
        }

        chipgrp01.setOnCheckedStateChangeListener((chipGroup, list) -> {
            if (pointer[1] == 0) {
                LineData nLineData = new LineData();
                for (int id : list) {
                    List<Entry> nEntries = new ArrayList<>();
                    Map<Long, Integer> nData = escaneo.getScansDataFilteredByVideoStreaming(videoStreaming.get(id));
                    if (nData.isEmpty()) {
                        chips01.get(id).setEnabled(false);
                        continue;
                    }
                    Map<Float, Integer> nDataF = formatter(nData);
                    for (Float f : nDataF.keySet()) {
                        nEntries.add(new Entry(f, nDataF.get(f)));
                    }
                    LineDataSet nLineDataSet = new LineDataSet(nEntries, videoStreaming.get(id));
                    nLineDataSet.setColor(colors.get(videoStreaming.get(id)));
                    nLineDataSet.setHighlightEnabled(false);
                    nLineData.addDataSet(nLineDataSet);
                }
                if (nLineData.getDataSetCount() == 0) {
                    lineChart.setData(lineData);
                } else {
                    nLineData.setValueFormatter(new LargeValueFormatter());
                    lineChart.setData(nLineData);
                }
                lineChart.animateX(duration);
            }
        });

        DateHandler dh = new DateHandler();

        chipgrp02 = this.findViewById(R.id.third_chipgrp_02);

        scansIds = Arrays.stream(escaneo.getScansIds()).boxed().collect(Collectors.toList());

        for (int i = 0; i< scansIds.size(); i++) {
            chips02.add(new Chip(this));
            long[] scanTS = escaneo.getScanTimeStamps(String.valueOf(scansIds.get(i)));
            String text = dh.timeStampToFormattedString(scanTS[0]) + " - " + dh.timeStampToFormattedString(scanTS[1]);
            chips02.get(i).setText(text);
            chips02.get(i).setId(i);
            chips02.get(i).setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
            chips02.get(i).setTextSize(textSize);
            chips02.get(i).setChipDrawable(ChipDrawable.createFromAttributes(this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));
            chips02.get(i).setPadding(dpToPx(horizontalPadding), dpToPx(verticalPadding), dpToPx(horizontalPadding), dpToPx(verticalPadding));
            chipgrp02.addView(chips02.get(i));
        }

        chipgrp02.setOnCheckedStateChangeListener((chipGroup, list) -> {
            if (pointer[1] == 1) {
                LineData nLineData = new LineData();
                for (int id : list) {
                    List<Entry> nEntries = new ArrayList<>();
                    Map<Long, Integer> nData = escaneo.getScanData(scansIds.get(id));
                    Map<Float, Integer> nDataF = formatter(nData);
                    for (Float f : nDataF.keySet()) {
                        nEntries.add(new Entry(f, nDataF.get(f)));
                    }
                    LineDataSet nLineDataSet = new LineDataSet(nEntries, (String) chips02.get(id).getText());
                    nLineDataSet.setColor(colors.get(escaneo.getScanVideoStreaming(String.valueOf(scansIds.get(id)))));
                    nLineDataSet.setHighlightEnabled(false);
                    nLineData.addDataSet(nLineDataSet);
                }
                if (nLineData.getDataSetCount() == 0) {
                    lineChart.setData(lineData);
                } else {
                    nLineData.setValueFormatter(new LargeValueFormatter());
                    lineChart.setData(nLineData);
                }
                lineChart.animateX(duration);
            }
        });

        chipgrp03 = this.findViewById(R.id.third_chipgrp_03);

        chips03.add(new Chip(this));
        chips03.get(0).setText(getString(R.string.third_chip_03_01));
        chips03.get(0).setId(2);
        chips03.get(0).setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
        chips03.get(0).setTextSize(textSize);
        chips03.get(0).setChipDrawable(ChipDrawable.createFromAttributes(this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));
        chips03.get(0).setPadding(dpToPx(horizontalPadding), dpToPx(verticalPadding), dpToPx(horizontalPadding), dpToPx(verticalPadding));
        chipgrp03.addView(chips03.get(0));
        chips03.add(new Chip(this));
        chips03.get(1).setText(getString(R.string.third_chip_03_02));
        chips03.get(1).setId(3);
        chips03.get(1).setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
        chips03.get(1).setTextSize(textSize);
        chips03.get(1).setChipDrawable(ChipDrawable.createFromAttributes(this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));
        chips03.get(1).setPadding(dpToPx(horizontalPadding), dpToPx(verticalPadding), dpToPx(horizontalPadding), dpToPx(verticalPadding));
        chipgrp03.addView(chips03.get(1));

        chipgrp03.setOnCheckedStateChangeListener((chipGroup, list) -> {
            if (pointer[1] == 2) {
                LineData nLineData = new LineData();
                for (int id : list) {
                    List<Entry> nEntries = new ArrayList<>();
                    Map<Long, Integer> nData = bateria.getRowsDataFilteredByStatus(id);
                    if (nData.isEmpty()) {
                        chips03.get(id-2).setEnabled(false);
                        continue;
                    }
                    Map<Float, Integer> nDataF = formatter(nData);
                    for (Float f : nDataF.keySet()) {
                        nEntries.add(new Entry(f, nDataF.get(f)));
                    }
                    LineDataSet nLineDataSet = new LineDataSet(nEntries, (String) chips03.get(id-2).getText());
                    nLineDataSet.setColor(colors.get((String) chips03.get(id-2).getText()));
                    nLineDataSet.setHighlightEnabled(false);
                    nLineData.addDataSet(nLineDataSet);
                }
                if (nLineData.getDataSetCount() == 0) {
                    lineChart.setData(lineData);
                } else {
                    nLineData.setValueFormatter(new LargeValueFormatter());
                    lineChart.setData(nLineData);
                }
                lineChart.animateX(duration);
            }
        });

        chipgrp04 = this.findViewById(R.id.third_chipgrp_04);

        chips04.add(new Chip(this));
        chips04.get(0).setText(getString(R.string.third_chip_04_01));
        chips04.get(0).setId(0);
        chips04.get(0).setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
        chips04.get(0).setTextSize(textSize);
        chips04.get(0).setChipDrawable(ChipDrawable.createFromAttributes(this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));
        chips04.get(0).setPadding(dpToPx(horizontalPadding), dpToPx(verticalPadding), dpToPx(horizontalPadding), dpToPx(verticalPadding));
        chipgrp04.addView(chips04.get(0));
        chips04.add(new Chip(this));
        chips04.get(1).setText(getString(R.string.third_chip_04_02));
        chips04.get(1).setId(1);
        chips04.get(1).setLayoutParams(new ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT));
        chips04.get(1).setTextSize(textSize);
        chips04.get(1).setChipDrawable(ChipDrawable.createFromAttributes(this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice));
        chips04.get(1).setPadding(dpToPx(horizontalPadding), dpToPx(verticalPadding), dpToPx(horizontalPadding), dpToPx(verticalPadding));
        chipgrp04.addView(chips04.get(1));

        chipgrp04.setOnCheckedStateChangeListener((chipGroup, list) -> {
            if (pointer[1] == 3) {
                LineData nLineData = new LineData();
                for (int id : list) {
                    List<Entry> nEntries = new ArrayList<>();
                    Map<Long, Integer> nData = bateria.getRowsDataFilteredByConsumption(id);
                    if (nData.isEmpty()) {
                        chips04.get(id).setEnabled(false);
                        continue;
                    }
                    Map<Float, Integer> nDataF = formatter(nData);
                    for (Float f : nDataF.keySet()) {
                        nEntries.add(new Entry(f, nDataF.get(f)));
                    }
                    LineDataSet nLineDataSet = new LineDataSet(nEntries, (String) chips04.get(id).getText());
                    nLineDataSet.setColor(colors.get((String) chips04.get(id).getText()));
                    nLineDataSet.setHighlightEnabled(false);
                    nLineData.addDataSet(nLineDataSet);
                }
                if (nLineData.getDataSetCount() == 0) {
                    lineChart.setData(lineData);
                } else {
                    nLineData.setValueFormatter(new LargeValueFormatter());
                    lineChart.setData(nLineData);
                }
                lineChart.animateX(duration);
            }
        });

        Button btn01 = this.findViewById(R.id.third_btn_01);
        btn01.setOnClickListener(v -> {
            if (pointer[1] != 0) {
                pointer[0] = pointer[1];
                pointer[1] = 0;
                clearFilters();
            }
            if (chipgrp01.getVisibility() == View.GONE) {
                chipgrp01.setVisibility(View.VISIBLE);
            } else {
                chipgrp01.setVisibility(View.GONE);
                pointer[0] = 0;
                pointer[1] = -1;
                clearFilters();
                pointer[1] = 0;
            }
            lineChart.setData(lineData);
            lineChart.animateX(duration);
        });

        Button btn02 = this.findViewById(R.id.third_btn_02);
        btn02.setOnClickListener(v -> {
            if (pointer[1] != 1) {
                pointer[0] = pointer[1];
                pointer[1] = 1;
                clearFilters();
            }
            if (chipgrp02.getVisibility() == View.GONE) {
                chipgrp02.setVisibility(View.VISIBLE);
            } else {
                chipgrp02.setVisibility(View.GONE);
                pointer[0] = 1;
                pointer[1] = -1;
                clearFilters();
                pointer[1] = 1;
            }
            lineChart.setData(lineData);
            lineChart.animateX(duration);
        });

        Button btn03 = this.findViewById(R.id.third_btn_03);
        btn03.setOnClickListener(v -> {
            if (pointer[1] != 2) {
                pointer[0] = pointer[1];
                pointer[1] = 2;
                clearFilters();
            }
            if (chipgrp03.getVisibility() == View.GONE) {
                chipgrp03.setVisibility(View.VISIBLE);
            } else {
                chipgrp03.setVisibility(View.GONE);
                pointer[0] = 2;
                pointer[1] = -1;
                clearFilters();
                pointer[1] = 2;
            }
            lineChart.setData(lineData);
            lineChart.animateX(duration);
        });

        Button btn04 = this.findViewById(R.id.third_btn_04);
        btn04.setOnClickListener(v -> {
            if (pointer[1] != 3) {
                pointer[0] = pointer[1];
                pointer[1] = 3;
                clearFilters();
            }
            if (chipgrp04.getVisibility() == View.GONE) {
                chipgrp04.setVisibility(View.VISIBLE);
            } else {
                chipgrp04.setVisibility(View.GONE);
                pointer[0] = 3;
                pointer[1] = -1;
                clearFilters();
                pointer[1] = 3;
            }
            lineChart.setData(lineData);
            lineChart.animateX(duration);
        });

        TextView tv = this.findViewById(R.id.third_tv);

        if (data.isEmpty()) {
            tv.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
            TextView tvt = this.findViewById(R.id.third_tv_t);
            tvt.setVisibility(View.GONE);
            btn01.setVisibility(View.GONE);
            btn02.setVisibility(View.GONE);
            btn03.setVisibility(View.GONE);
            btn04.setVisibility(View.GONE);
        }
    }

    private void clearFilters() {
        switch (pointer[0]) {
            case 0:
                for (Chip c : chips01) {
                    c.setChecked(false);
                }
                chipgrp01.setVisibility(View.GONE);
                break;
            case 1:
                for (Chip c : chips02) {
                    c.setChecked(false);
                }
                chipgrp02.setVisibility(View.GONE);
                break;
            case 2:
                for (Chip c : chips03) {
                    c.setChecked(false);
                }
                chipgrp03.setVisibility(View.GONE);
                break;
            case 3:
                for (Chip c : chips04) {
                    c.setChecked(false);
                }
                chipgrp04.setVisibility(View.GONE);
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