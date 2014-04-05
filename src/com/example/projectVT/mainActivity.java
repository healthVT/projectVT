package com.example.projectVT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class mainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    View.OnClickListener submitListener;
    Button submitButton;
    RadioGroup genderRadio;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        submitListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setEnabled(false);
                EditText vitaminText = (EditText) findViewById(R.id.viatmenInput);
                EditText ftText = (EditText) findViewById(R.id.ftInput);
                EditText inText = (EditText) findViewById(R.id.inInput);
                EditText weightText = (EditText) findViewById(R.id.weightInput);
                genderRadio = (RadioGroup) findViewById(R.id.radioGroupGender);
                RadioButton genderText = (RadioButton) findViewById(genderRadio.getCheckedRadioButtonId());

                CharSequence vitaminInput = vitaminText.getText();
                CharSequence ftInput = ftText.getText();
                CharSequence inInput = inText.getText();
                CharSequence weightInput = weightText.getText();
                CharSequence genderInput = genderText.getText();

                Intent intent = new Intent(mainActivity.this, resultActivity.class);

                if(vitaminInput.length() == 0 || ftInput.length() == 0 || inInput.length() == 0 || weightInput.length() == 0){
                    Toast.makeText(getApplicationContext(), "FUCKER!! ENTER ALL OF NUMBERS.", Toast.LENGTH_LONG).show();
                    submitButton.setEnabled(true);
                    return;
                }

                intent.putExtra("vitamenA", vitaminInput);
                intent.putExtra("ft", ftInput);
                intent.putExtra("in", inInput);
                intent.putExtra("weight", weightInput);
                intent.putExtra("gender", genderInput);

                startActivityForResult(intent, 0);

            }
        };

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(submitListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        submitButton.setEnabled(true);
    }

}
