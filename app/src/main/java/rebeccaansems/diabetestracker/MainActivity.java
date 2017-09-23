package rebeccaansems.diabetestracker;

import android.accounts.AbstractAccountAuthenticator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.Console;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private Toolbar toolbar;
    protected HorizontalBarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SixTwelveSx");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     //   startService(new Intent(getApplicationContext(), BackgroundService.class)); //Start Notifcation service

        mChart = (HorizontalBarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        Description description = new Description();
        description.setText("Blood Sugar Averages");
        mChart.setDescription(description);

        mChart.setDrawGridBackground(false);
        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setLabelCount(0);
        xl.setGranularity(0);
        xl.setLabelCount(24, true);
        xl.setAxisMinimum(0);
        xl.setAxisMaximum(24);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
        yl.setDrawLabels(false);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yl.setAxisMaximum(24);

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);
        yr.setAxisMaximum(24);

        setData(7);
        mChart.setFitBars(true);
        mChart.animateY(2500);
        mChart.setDrawBorders(true);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    private void setData(int count) {

        float barWidth = 5f;
        float spaceForBar = 8f;
        ArrayList<BarEntry> yValsLow = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yValsNorm = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yValsHigh = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yValsVeryHigh = new ArrayList<BarEntry>();

        List<BloodSugarDataPoint> bloodSugars = BloodSugarDataPoint.listAll(BloodSugarDataPoint.class);
        float[][] bloodSugarAvg = new float[24][2];

        for(int i=0; i < bloodSugars.size(); i++){
            bloodSugarAvg[Integer.parseInt(bloodSugars.get(i).dateTime.substring(3))][0]++;
            bloodSugarAvg[Integer.parseInt(bloodSugars.get(i).dateTime.substring(3))][1] += bloodSugars.get(i).bloodSugarValue;
        }
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        for(int i = 23; i>=0; i--){
            if(!Float.isNaN((bloodSugarAvg[i][1]/bloodSugarAvg[i][0]))){
                float avg = (bloodSugarAvg[i][1]/bloodSugarAvg[i][0]);
                if(avg < Float.parseFloat(sharedPref.getString("lowBlood", "4.0"))){
                    yValsLow.add(new BarEntry(i, avg));
                } else if (avg < Float.parseFloat(sharedPref.getString("highBlood", "10.0"))){
                    yValsNorm.add(new BarEntry(i, avg));
                } else if (avg < Float.parseFloat(sharedPref.getString("veryHighBlood", "15.0"))){
                    yValsHigh.add(new BarEntry(i, avg));
                } else {
                    yValsVeryHigh.add(new BarEntry(i, avg));
                }
            }

        }


        BarDataSet lowSet, normSet, highSet, veryHighSet;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            lowSet = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            lowSet.setValues(yValsLow);

            normSet = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            normSet.setValues(yValsNorm);

            highSet = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            highSet.setValues(yValsHigh);

            veryHighSet = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            veryHighSet.setValues(yValsVeryHigh);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            lowSet = new BarDataSet(yValsLow, "Low");
            normSet = new BarDataSet(yValsNorm, "Normal");
            highSet = new BarDataSet(yValsHigh, "High");
            veryHighSet = new BarDataSet(yValsVeryHigh, "Very High");

            lowSet.setDrawIcons(true);
            lowSet.setColor(Color.BLUE);
            lowSet.setHighLightColor(getResources().getColor(R.color.colorAccent));

            normSet.setDrawIcons(true);
            normSet.setColor(Color.GREEN);
            normSet.setHighLightColor(getResources().getColor(R.color.colorAccent));

            highSet.setDrawIcons(true);
            highSet.setColor(Color.YELLOW);
            highSet.setHighLightColor(getResources().getColor(R.color.colorAccent));

            veryHighSet.setDrawIcons(true);
            veryHighSet.setColor(Color.RED);
            veryHighSet.setHighLightColor(getResources().getColor(R.color.colorAccent));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(lowSet);
            dataSets.add(normSet);
            dataSets.add(highSet);
            dataSets.add(veryHighSet);
            BarData data = new BarData(dataSets);

            data.setValueTextSize(10f);
            data.setBarWidth(1);
            data.setValueTextSize(20);
            data.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
            data.setValueTypeface(Typeface.DEFAULT_BOLD);
            mChart.setData(data);
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.newentry:
                intent = new Intent(this, BloodGlucoseInput.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbutton, menu);
        return true;
    }

    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    };

}
