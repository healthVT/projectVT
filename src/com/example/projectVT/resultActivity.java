package com.example.projectVT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.projectVT.util.projectVTServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

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

        //ImageView loadingImgage = (ImageView) findViewById(R.id.loadingImage);
        //loadingImgage.setImageResource(R.drawable.loader);

        Bundle extra = getIntent().getExtras();
        String foodList = extra.getString("foodList");
        String gender = extra.getString("gender");
        String vitaminChar = "";
        projectVTServer server = new projectVTServer();
        try{
            JSONObject resultJSON = server.execute("food/getVitaminDailyResult/?foodList=" + foodList + "&gender=" + gender).get();
            vitaminChar += vitaminResultString(resultJSON, "a");
            vitaminChar += vitaminResultString(resultJSON, "b1");
            vitaminChar += vitaminResultString(resultJSON, "b2");
            vitaminChar += vitaminResultString(resultJSON, "b3");
            vitaminChar += vitaminResultString(resultJSON, "b6");
            vitaminChar += vitaminResultString(resultJSON, "b12");
            vitaminChar += vitaminResultString(resultJSON, "c");
            vitaminChar += vitaminResultString(resultJSON, "d");
            vitaminChar += vitaminResultString(resultJSON, "e");
            vitaminChar += vitaminResultString(resultJSON, "k");
        }catch(Exception e){
            Log.e("Server call exception", "exception", e);
        }




        TextView resultText = (TextView) findViewById(R.id.resultText);
        resultText.setText(vitaminChar);

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

    DecimalFormat df = new DecimalFormat("#.##");
    private String vitaminResultString(JSONObject vitamin, String name){
        String vitaminString = "";

        try{
            if(vitamin.get(name) != null && vitamin.getInt(name) != 0 && vitamin.getInt(name) > 0){
                vitaminString = " " + name.toUpperCase() + ": " + df.format(vitamin.getDouble(name));
            }
        }catch(JSONException e){
            Log.e("JSONException", "Exception", e);
        }
        return vitaminString;
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
