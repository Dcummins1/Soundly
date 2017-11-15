package com.example.soundly;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class SleepGraph extends AppCompatActivity {
    String time;
    List<Double> numbers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        //Should read from phone memory
//        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "SoundlyOutput/output.txt";

//        try {
//            BufferedReader br = new BufferedReader(new FileReader(path));
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//            while (line != null) {
//                sb.append(line);
//                sb.append(System.lineSeparator());
//                line = br.readLine();
//            }
//            String everything = sb.toString();
//            br.close();
//            Toast.makeText(SleepGraph.this, everything, Toast.LENGTH_SHORT).show();
//        }
//        catch(IOException e){
//            Log.e("Exception", "File read failed: " + e.toString());
//            Toast.makeText(SleepGraph.this, "nope", Toast.LENGTH_SHORT).show();
//        }


//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("test.txt")));
//            //BufferedReader br = new BufferedReader(new FileReader());
//
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//
//            while (line != null) {
//                br.readLine(); //read first line and ignore
//                //sb.append(line);
//                //sb.append(System.lineSeparator());
////                Toast.makeText(this, line, Toast.LENGTH_LONG).show();
//                String[] parts =  line.split(",");
//                if (parts !=null) {
//                time = parts[0];
//                Float x = Float.parseFloat(parts[1]);
//                Float y = Float.parseFloat(parts[2]);
//                Float z = Float.parseFloat(parts[3]);
//
//
//            }
//            //Toast.makeText(this, time, Toast.LENGTH_LONG).show();
//                }
//            //String everything = sb.toString();
//            br.close();
//            StringBuilder builder = new StringBuilder();
//
//        }
//        catch(IOException e){
//            Log.e("Exception", "File read failed: " + e.toString());
//            Toast.makeText(SleepGraph.this, "nope", Toast.LENGTH_SHORT).show();
//
//        }

    }






}
        //Basic idea is to 1st read a static file and graph movements during sleep. Then 2nd Read live accelerometer file and graph.
        // This will be used to trigger the main function to stop other applications
        //read in static file











