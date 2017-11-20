package com.example.soundly;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {

    EditText editText;
    Button startButton, stopButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        editText = (EditText) findViewById(R.id.editText);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        textView = (TextView) findViewById(R.id.textView);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checks if user has entered a number; displays message when no input has been given.
                //else, creates new CountDownTimer
                if  (editText.getText().toString().equals("")) {
                    Context context = getApplicationContext();
                    CharSequence textT = "Please enter time";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, textT, duration);
                    toast.show();
                }
                else {
                    String text = editText.getText().toString();
                    int seconds = Integer.valueOf(text);

                    final CountDownTimer countDownTimer = new CountDownTimer(seconds * 60000, 1000) {
                        @Override
                        public void onTick(long l) {
                            textView.setText("Minutes: " + (int) (l / 60000) + " Seconds: " + (int) (l / 1000));
                        }

                        @Override
                        public void onFinish() {
                            textView.setText("Finished!");
                        }
                    };

                    //starts timer
                    countDownTimer.start();
                    editText.setText("");

                    //cancels timer on stopButton click
                    stopButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            countDownTimer.cancel();
                            textView.setText("");
                        }
                    });
                }
            }
        });
    }



}
