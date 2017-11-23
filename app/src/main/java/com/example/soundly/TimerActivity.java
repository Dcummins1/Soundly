package com.example.soundly;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {

    EditText editText;
    ProgressBar progressBar;
    Button startButton, stopButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        editText = (EditText) findViewById(R.id.editText);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        textView = (TextView) findViewById(R.id.textView);

        //creates onClickListener for start button.
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checks if there is user input; displays toast when no input has been given.
                if  (editText.getText().toString().equals("")) {
                    Context context = getApplicationContext();
                    CharSequence textT = "Please enter time";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, textT, duration);
                    toast.show();
                }
                //if there is user input, checks if the value of the input is 0; displays toast promting the user
                // for a number > 0 if true.
                else {
                    String text = editText.getText().toString();
                    int seconds = Integer.valueOf(text);

                    if (seconds == 0){
                        Context context = getApplicationContext();
                        CharSequence textT = "Please enter valid time input";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, textT, duration);
                        toast.show();
                    }
                    //Once program has confirmed that there is a valid input put it creates a new CountDownTimer,
                    //sets up the progressBar and "freezes" the editText field.
                    else {
                        final CountDownTimer countDownTimer = new CountDownTimer(seconds * 60000, 1000) {
                            @Override
                            public void onTick(long l) {
                                textView.setText("Minutes: " + ((int) (l / 60000) + 1));

                                int progress = (int) (l / 1000);
                                progressBar.setProgress(progress);
                            }
                            @Override
                            public void onFinish() {
                                textView.setText("Finished!");
                                editText.setFocusableInTouchMode(true);
                                System.out.println("finished");
                            }
                        };

                        //starts timer
                        countDownTimer.start();
                        editText.setText("");
                        editText.setFocusable(false);

                        //cancels timer on stopButton click
                        stopButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                countDownTimer.cancel();
                                textView.setText("");
                                editText.setFocusableInTouchMode(true);
                            }
                        });
                    }
                }
            }
        });
    }
}
