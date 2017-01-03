package com.quascenta.petersroad.activities;

/**
 * Created by AKSHAY on 11/3/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import com.quascenta.petersroad.adapters.ExpandableListAdapter;
import com.quascenta.petersroad.broadway.R;
import com.quascenta.petersroad.fragments.LogFragment;
import com.quascenta.petersroad.interfaces.IView;
import com.quascenta.petersroad.interfaces.Presenter;
import com.quascenta.petersroad.model.device_log;
import com.quascenta.petersroad.presenters.PresenterImpl;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

public class DetailSensorsActivity extends AppCompatActivity implements ExpandableListAdapter.OnSensorSelected,IView,LogFragment.OnHeadlineSelectedListener {
    public static final String EXTRA_NAME = "cheese_name";
    private static ArrayList<Line> lines;
    ProgressDialog progressdialog;
    ArrayList<Line> refinedline;
    Presenter presenter;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    LogFragment logFragment = new LogFragment();
    private LineChartView chart;
    private LineChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_detail_layout);
        ButterKnife.bind(this);
        presenter = new PresenterImpl(this, getApplicationContext());
        progressdialog = new ProgressDialog(this);
        chart = (LineChartView) findViewById(R.id.cubiclinechart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        data = new LineChartData();
        lines = new ArrayList<>();
        refinedline = new ArrayList<>();
        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (savedInstanceState == null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_content_list, logFragment, this.toString())
                    .commit();
        }
        //  LoadDates();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }


    void LoadDates(int day, String month1, int year) {

        presenter.onDaySelected(day, month1, year);
        presenter.loadProgressBar(progressdialog);


    }

    @Override
    public void Plot(ArrayList<Line> arrayList) {
        lines.addAll(arrayList);
        DisplayGraph(refineSelection(2, lines));
        progressdialog.cancel();
    }


    private void DisplayGraph(ArrayList<Line> lines) {
        data.setLines(lines);
        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis();
            if (hasAxesNames) {
                axisX.setName("");
                axisY.setName("C");
            }

            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        chart.setLineChartData(data);
        resetViewport(chart);
        chart.setInteractive(true);
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.startDataAnimation();


    }


    private ArrayList<Line> refineSelection(int n, ArrayList<Line> lines) {
        ArrayList<Line> refinedlines = new ArrayList<>();
        refinedlines.add(0, lines.get(n));
        return refinedlines;
    }


    @Override
    public void change(String s) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "Seleted url : " + s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(ArrayList<device_log> device_logs) {
        logFragment.updateUI(device_logs);
    }

    @Override
    public void onFailiure() {

    }

    private void resetViewport(Chart chart) {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getCurrentViewport());
        v.bottom = 0;
        v.top = 80;
        v.left = 0;
        v.right = 12;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }


    void prepareDataAnimation(){
        for (Line line : data.getLines()) {
            for (PointValue value : line.getValues()) {
                value.setTarget((float) Math.random() * 100, (float) Math.random() * 100);
            }
        }


    }

    @Override
    public void sensorSelected(int position, String x) {
        DisplayGraph(selectSensor(x));
    }

    private ArrayList<Line> selectSensor(String x) {

        switch (x) {
            case "Sensor1":
                return refineSelection(0, lines);
            case "Sensor2":
                return refineSelection(1, lines);
            case "Sensor3":
                return refineSelection(2, lines);
            case "Sensor4":
                return refineSelection(3, lines);
            case "Sensor5":
                return refineSelection(4, lines);
            case "Sensor6":
                return refineSelection(5, lines);
            case "Sensor7":
                return refineSelection(6, lines);
            case "Sensor8":
                return refineSelection(7, lines);

        }
        return refineSelection(0, lines);
    }

    @Override
    public void onArticleSelected(int position) {

    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            View parentLayout = findViewById(android.R.id.content);
                       Snackbar.make(parentLayout,value.getY() +"C"+" recorded at "+String.format("%d",(int)value.getX())+":00", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

            // TODO Auto-generated method stub

        }
    }
}


