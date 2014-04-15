package com.example.projectVT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Jay on 3/14/14.
 */
public class resultActivity extends Activity {

    Button backButton;
    View.OnClickListener backListener;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        //create vitA_RDA_database
        //read required vitA via inputs (BMI, age, disease,...)
        int vitamenRate = 1000;
        Bundle extra = getIntent().getExtras();
        int vitamenA = extra.getInt("vitamenA");
        CharSequence resultChar;

        if(vitamenA != 0){
            int resultVitamenA = vitamenRate - vitamenA;
            if(resultVitamenA > 0){
                resultChar = "GO TAKE MORE VITAMEN A!!!!!!!!!!!!!! FUCKER~";
            }else{
                resultChar = "GO TAKE OTHER VITAMEN INSTANT OF A!!!!!! FATTY";
            }
        }else{
            resultChar = "FUCKER!! ENTER A NUMBER.";
        }
        //save value to vitA_ID_database


        TextView resultText = (TextView) findViewById(R.id.resultText);
        resultText.setText(resultChar);

        //Listener
        backListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };

        backButton = (Button) findViewById(R.id.resultBackButton);
        backButton.setOnClickListener(backListener);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
