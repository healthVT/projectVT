package com.example.projectVT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.projectVT.sqlite.helper.UserInfo;
import com.example.projectVT.sqlite.model.DatabaseHelper;
import com.example.projectVT.util.projectVTServer;
import org.json.JSONException;
import org.json.JSONObject;

public class mainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private View.OnClickListener submitListener, plusListener;
    private View.OnFocusChangeListener setTableListener;
    private Button submitButton, plusButton;
    private TextView infoText;
    private EditText nameField, ftText, inText, weightText, vitaminText;
    private AutoCompleteTextView foodInput;
    private RadioGroup genderRadio;
    private TableLayout infoTable;
    private LinearLayout vitaminList;
    private String inputFoodList;
    private DatabaseHelper db;
    private UserInfo user;
    private SharedPreferences sharedData;
    private ArrayAdapter<String> adapter;
    private Spinner amountSpinner;


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


                Intent intent = new Intent(mainActivity.this, resultActivity.class);

                intent.putExtra("foodList", inputFoodList);
                intent.putExtra("gender", user.getGender());

                Log.e("in", String.valueOf(user.getId()));
                if(user != null){
                    //db.updateVitaminLog(new VitaminLog(user.getId(), "Apple", 2.1, 3.1, 4.1, 5.1, 6.1, 7.1, 8.1, 9.1, 10.1, 11.1));
                }

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

        foodInput = (AutoCompleteTextView) findViewById(R.id.foodInput);
        foodInput.setOnFocusChangeListener(setTableListener);

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

        //Spinner
        amountSpinner = (Spinner) findViewById(R.id.amount);

        inputFoodList = "";


        //setup user info if the user already entered information
        String userName = sharedData.getString("projectVT.userName", null);

        if(userName != null){
            setUserInfoText(userName);
            infoText.setVisibility(TextView.VISIBLE);
        }else{
            nameField.requestFocus();
        }

        //call util to get http response
        try{
            projectVTServer server = new projectVTServer();
            JSONObject jsonResult = server.execute("food/getFoodList").get();
            String[] foodList = jsonResult.get("foodList").toString().split(",");
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, foodList);
            foodInput.setAdapter(adapter);

        }catch(Exception e){
            Log.e("Project VT Server exception ", "Exception", e);
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
        if(user == null){
            user = db.getUser(userName);
        }

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
        try{
            projectVTServer server = new projectVTServer();
            CharSequence vitaminInput = foodInput.getText();
            String vitaminChar = vitaminInput.toString() + ":\n";

            JSONObject resultJSON = server.execute("food/getVitaminByFood/?foodName=" + vitaminInput.toString()).get();
            resultJSON = resultJSON.getJSONObject("vitamin");

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


            TextView eachText = new TextView(this);
            eachText.setText(vitaminChar);
            eachText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            foodInput.setText("");
            vitaminList.addView(eachText, 0);

            String amount = getAmountInString(amountSpinner.getSelectedItem().toString());
            String common = "";
            if(!inputFoodList.equals("")){
                common =",";
            }
            inputFoodList += common + vitaminInput.toString() + ":" + amount;

        }catch(Exception e){
            Log.e("add text exception", "exception" , e);
        }


    }

    private String getAmountInString(String amount){
        if(amount.equals("1/4")){
            return "0.25";
        }else if(amount.equals("1/2")){
            return "0.5";
        }else{
            return amount;
        }

    }



    private String vitaminResultString(JSONObject vitamin, String name){
        String vitaminString = "";

        try{
            if(vitamin.get(name) != null && vitamin.get(name).toString() != "0.0"){
                vitaminString = " " + name.toUpperCase() + ": " + vitamin.get(name);
            }
        }catch(JSONException e){
            Log.e("JSONException", "Exception", e);
        }
        return vitaminString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        submitButton.setEnabled(true);
    }

}
