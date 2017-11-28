package com.example.soundly;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupActivity extends AppCompatActivity {

    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    Button popupButton, insidePopupButton;
    TextView popupText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        init();
        popupInit();

        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMessage.showAsDropDown(popupButton, 0, 0);
            }
        });

        insidePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMessage.dismiss();
            }
        });

    }

    @SuppressLint("WrongConstant")
    public void init() {
        popupButton = (Button) findViewById(R.id.popupbutton);
        popupText = new TextView(this);
        insidePopupButton = new Button(this);
        layoutOfPopup = new LinearLayout(this);
        insidePopupButton.setText("OK");
        popupText.setText("This is Popup Window.press OK to dismiss         it.");
        popupText.setPadding(0, 0, 0, 20);
        layoutOfPopup.setOrientation(1);
        layoutOfPopup.addView(popupText);
        layoutOfPopup.addView(insidePopupButton);
    }

    public void popupInit() {
//        popupButton.setOnClickListener((View.OnClickListener) this);
//        insidePopupButton.setOnClickListener((View.OnClickListener) this);
        popupMessage = new PopupWindow(layoutOfPopup, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupMessage.setContentView(layoutOfPopup);
    }

//    @Override
//    public void onClick(View v) {
//
//        if (v.getId() == R.id.popupbutton) {
//            popupMessage.showAsDropDown(popupButton, 0, 0);
//        }
//
//        else {
//            popupMessage.dismiss();
//        }
//    }
}
