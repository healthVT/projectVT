package com.example.projectVT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import java.util.List;
import java.util.ArrayList;
import com.example.projectVT.sqlite.helper.UserInfo;
import com.example.projectVT.sqlite.model.DatabaseHelper;

public class mainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    View.OnClickListener submitListener, plusListener;
    View.OnFocusChangeListener setTableListener;
    Button submitButton, plusButton;
    TextView infoText;
    EditText nameField, viatmenInput, ftText, inText, weightText, vitaminText;
    RadioGroup genderRadio;
    TableLayout infoTable;
    LinearLayout vitaminList;
    List<Integer> vitaminArray = new ArrayList<Integer>();
    DatabaseHelper db;
    UserInfo user;
    SharedPreferences sharedData;
    //testing change on git
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        db = new DatabaseHelper(getApplicationContext());
        sharedData = this.getSharedPreferences("projectVT.userName", Context.MODE_PRIVATE);

        /** Listener **/
        submitListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton.setEnabled(false);
                vitaminText = (EditText) findViewById(R.id.viatmenInput);
                RadioButton genderText = (RadioButton) findViewById(genderRadio.getCheckedRadioButtonId());

                CharSequence gender = "";

                if(genderText != null){
                    gender = genderText.getText();
                }

                int totalVitaminA = 0;
                //CharSequence vitaminInput = vitaminText.getText();
                for(int i=0;i<vitaminArray.size();i++){
                    totalVitaminA += vitaminArray.get(i);
                }

                CharSequence ftInput = ftText.getText();
                CharSequence inInput = inText.getText();
                CharSequence weightInput = weightText.getText();
                CharSequence genderInput = gender;

                Intent intent = new Intent(mainActivity.this, resultActivity.class);

                if(totalVitaminA == 0 || ftInput.length() == 0 || inInput.length() == 0 || weightInput.length() == 0){
                    Toast.makeText(getApplicationContext(), "FUCKER!! ENTER ALL OF NUMBERS.", Toast.LENGTH_LONG).show();
                    submitButton.setEnabled(true);
                    return;
                }

                intent.putExtra("vitamenA", totalVitaminA);
                intent.putExtra("ft", ftInput);
                intent.putExtra("in", inInput);
                intent.putExtra("weight", weightInput);
                intent.putExtra("gender", genderInput);

                startActivityForResult(intent, 0);

            }
        };

        setTableListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus){
                if(hasFocus){
                    if(view.getTag().toString().contains("showInfoTable")){
                        setInfoTable(true);
                        infoText.setVisibility(TextView.INVISIBLE);
                    }else if(view.getTag().toString().contains("hideInfoTable")){
                        setUserInfoText(null);
                        setInfoTable(false);
                        infoText.setVisibility(TextView.VISIBLE);
                    }
                }
            }
        };

        plusListener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                addTextToView();
            }
        };



        /** init variable **/
        infoTable = (TableLayout) findViewById(R.id.userInfoTable);

        infoText = (TextView) findViewById(R.id.infoText);

        nameField = (EditText) findViewById(R.id.nameField);
        nameField.setOnFocusChangeListener(setTableListener);

        viatmenInput = (EditText) findViewById(R.id.viatmenInput);
        viatmenInput.setOnFocusChangeListener(setTableListener);

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(submitListener);

        //User information edit text
        ftText = (EditText) findViewById(R.id.ftInput);
        inText = (EditText) findViewById(R.id.inInput);
        weightText = (EditText) findViewById(R.id.weightInput);
        genderRadio = (RadioGroup) findViewById(R.id.radioGroupGender);

        //Vitamin list
        vitaminList = (LinearLayout) findViewById(R.id.vitaminList);
        plusButton = (Button) findViewById(R.id.plusButton);
        plusButton.setOnClickListener(plusListener);

        //setup user info if the user already entered information
        String userName = sharedData.getString("projectVT.userName", null);
        if(userName != null){
            setUserInfoText(userName);
            infoText.setVisibility(TextView.VISIBLE);
        }else{
            nameField.requestFocus();
        }

    }

    private void setInfoTable(boolean show){
        if(show){
            infoTable.setVisibility(TableLayout.VISIBLE);
        }else{
            infoTable.setVisibility(TableLayout.GONE);
        }
    }

    private void setUserInfoText(String userName){
        UserInfo user = db.getUser(userName);
        if(user != null){
            nameField.setText(user.getName());
            ftText.setText(String.valueOf(user.getFeet()));
            inText.setText(String.valueOf(user.getInch()));
            weightText.setText(String.valueOf(user.getWeight()));

            for(int i=0; i<genderRadio.getChildCount(); i++){
                RadioButton radio = (RadioButton) genderRadio.getChildAt(i);
                if(radio != null && radio.getTag() != null && radio.getTag().toString().equals(user.getGender())){
                    radio.setChecked(true);
                }
            }
        }

        CharSequence nameInput = nameField.getText();
        CharSequence ftInput = ftText.getText();
        CharSequence inInput = inText.getText();
        CharSequence weightInput = weightText.getText();
        RadioButton genderText = (RadioButton) findViewById(genderRadio.getCheckedRadioButtonId());

        sharedData.edit().putString("projectVT.userName", nameInput.toString()).commit();

        CharSequence gender = "";

        if(genderText != null){
            gender = genderText.getText();
        }
         infoText.setText("H: " + ftInput + "." + inInput + " W: " + weightInput + " Gender: " + gender);

        if(user == null){
            if(nameInput.length() != 0){
                UserInfo newUser = new UserInfo();
                newUser.setName(nameInput.toString());
                if(ftInput.length() != 0){
                    newUser.setFeet(Integer.parseInt(ftInput.toString()));
                }
                if(inInput.length() != 0){
                    newUser.setInch(Integer.parseInt(inInput.toString()));
                }
                if(weightInput.length() != 0){
                    newUser.setWeight(Integer.parseInt(weightInput.toString()));
                }
                if(gender.length() != 0){
                    newUser.setGender(gender.toString());
                }

                db.updateUserInformation(newUser);
            }

        }

    }

    private void addTextToView(){
        CharSequence vitaminChar = viatmenInput.getText();
        TextView eachText = new TextView(this);
        eachText.setText(vitaminChar);
        eachText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        viatmenInput.setText("");
        vitaminList.addView(eachText, 0);

        vitaminArray.add(Integer.parseInt(vitaminChar.toString()));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        submitButton.setEnabled(true);
    }

}
