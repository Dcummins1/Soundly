package com.example.soundly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SleepGraph extends AppCompatActivity {
    private FirebaseAuth auth;
    Calendar calendar = Calendar.getInstance();
    String minute1;
    TextView title;
    String label;
    Long millis;
    public static ArrayList<String> x_axis=new ArrayList<String>();
    public static ArrayList<String> labelList=new ArrayList<String>();
    public static ArrayList<String> y_axis=new ArrayList<String>();
    String time;
    String y;
    int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_graph);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.tvTitle);

    }
    public void onPause() {
        super.onPause();
        finish();
    }
    public void onResume() {

        super.onResume();
        readFile();
        x_axis.clear();
        y_axis.clear();
        labelList.clear();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(getFilesDir() + File.separator + "soundly_output.txt")));
            String read;
            String NAME = br.readLine();
            String Title = NAME.substring(0, 19);
            title.append(Title);
            //Toast.makeText(this, NAME, Toast.LENGTH_LONG).show();

            while ((read = br.readLine()) != null) {

                String[] parts =  read.split(",");
                if (parts != null) {
                    time = parts[0];
                    y = (parts[1]);

                    if (Double.parseDouble(y) < 10) {
                        x_axis.add(i + "");
                        y_axis.add(y);
                        millis = Long.parseLong(time);
                        calendar.setTimeInMillis(millis);

                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        if (minute < 10) {
                            minute1 = "0" + Integer.toString(minute);
                        }
                        else {minute1 = Integer.toString(minute);}
                        label = String.format("%d:"+minute1, hour);

                        labelList.add(label);
                        i++;
                    }
                }

            }
            GraphView graph;
            LineGraphSeries<DataPoint> series;
            graph = (GraphView) findViewById(R.id.graph);
            series = new LineGraphSeries<>(data());   //initializing/defining series to get the data from the method 'data()'
            graph.removeAllSeries();
            graph.addSeries(series);                   //adding the series to the GraphView
            // series.setTitle(NAME);
            series.setColor(Color.BLUE);
            series.setDrawDataPoints(false);
            series.setDataPointsRadius(5);
            series.setThickness(3);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(5);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(10.0);

            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            int x = labelList.size();
            System.out.println(x+"");

            // graph.getGridLabelRenderer().setNumHorizontalLabels(5);
            if (x != 0) {
                staticLabelsFormatter.setHorizontalLabels(new String[]{labelList.get(0), labelList.get(x/4), labelList.get(x / 2), labelList.get(x * 3/4), labelList.get(x - 1)});
            }
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        } catch (Exception e) {
            String data = "";

            StringBuffer sbuff = new StringBuffer();
            InputStream is = this.getResources().openRawResource(R.raw.output);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //BufferedReader br = new BufferedReader(new FileReader());

            if (is != null) {

                try {


                    String NAME = br.readLine();//skip first line

                    while ((data = br.readLine()) != null) {

                        sbuff.append(data + "\n");
                        String[] parts = data.split(",");
                        if (parts != null) {
                            time = parts[0];
                            y = (parts[1]);
                            if (Double.parseDouble(y) < 10) {
                                x_axis.add(i + "");
                                y_axis.add(y);
                                i++;
                            }
                        }

                    }
                    GraphView graph;
                    LineGraphSeries<DataPoint> series;
                    graph = (GraphView) findViewById(R.id.graph);
                    series = new LineGraphSeries<>(data());   //initializing/defining series to get the data from the method 'data()'
                    graph.addSeries(series);                   //adding the series to the GraphView
                    series.setTitle(NAME);
                    series.setColor(Color.BLUE);
                    series.setDrawDataPoints(false);
                    series.setDataPointsRadius(5);
                    series.setThickness(3);
                    graph.getViewport().setMinX(0);
                    graph.getViewport().setMaxX(i);
                    graph.getViewport().setMinY(0);
                    graph.getViewport().setMaxY(10.0);

                    graph.getViewport().setYAxisBoundsManual(true);
                    graph.getViewport().setXAxisBoundsManual(true);
                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                    int x1 = labelList.size();
                    Toast.makeText(this, x1+"", Toast.LENGTH_LONG).show();

                    if (x1 != 0){
                        staticLabelsFormatter.setHorizontalLabels(new String[]{labelList.get(0), labelList.get(x1/2), labelList.get(x1-1)});}

                    // graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


                    //Toast.makeText(this, xs+"", Toast.LENGTH_LONG).show();
                } catch (Exception p) {
                    p.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }






        public DataPoint[] data () {
            int n = x_axis.size();     //to find out the no. of data-points
            DataPoint[] values = new DataPoint[n];     //creating an object of type DataPoint[] of size 'n'
            for (int j = 0; j < n; j++) {
                DataPoint v = new DataPoint(Double.parseDouble(x_axis.get(j)), Double.parseDouble(y_axis.get(j)));
                values[j] = v;
            }

            return values;
        }

    public void readFile(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(getFilesDir() + File.separator + "soundly_output.txt")));
            String read;
            StringBuilder builder = new StringBuilder("");

            while ((read = bufferedReader.readLine()) != null) {
                builder.append(read + "\n");
            }
            System.out.println("#############################################################");
            System.out.println("Output: " + builder);
            System.out.println("#############################################################");

            bufferedReader.close();

        }
        catch(IOException e){
            System.out.println(e.getMessage());

        }
    }

    public void signOut() {
        auth.signOut();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.menu_home:
                intent = new Intent(this, UserAreaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.menu_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_sleep_graph:
//                intent = new Intent(this, SleepGraph.class);
//                startActivity(intent);
                break;
            case R.id.menu_sign_out:
                signOut();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    }
















