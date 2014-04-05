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

        int vitamenRate = 1000;
        Bundle extra = getIntent().getExtras();
        String vitamenA = extra.getCharSequence("vitamenA").toString();
        CharSequence resultChar;

        if(vitamenA != null){
            int resultVitamenA = vitamenRate - Integer.parseInt(vitamenA);
            if(resultVitamenA > 0){
                resultChar = "GO TAKE MORE VITAMEN A!!!!!!!!!!!!!! FUCKER~";
            }else{
                resultChar = "GO TAKE OTHER VITAMEN INSTANT OF A!!!!!! FATTY";
            }
        }else{
            resultChar = "FUCKER!! ENTER A NUMBER.";
        }

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
