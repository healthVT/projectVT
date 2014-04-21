package com.example.projectVT.sqlite.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.projectVT.sqlite.helper.UserInfo;
import com.example.projectVT.sqlite.helper.VitaminLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jay on 4/14/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //Database version
    private static final int DATABASE_VERSION = 2;

    //Database name
    private static final String DATABASE_NAME = "projectVT";

    //Table Name
    private static final String TABLE_USER = "user";
    private static final String TABLE_LOG = "vitaminLog";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, " +
           "feet INTEGER, inch INTEGER, weight INTEGER, gender TEXT)";

    private static final String CREATE_VITAMINLOG_TABLE = "CREATE TABLE " + TABLE_LOG + " (id INTEGER PRIMARY KEY AUTOINCREMENT, logDate TEXT, " +
            "foodName TEXT, vitaminA REAL, vitaminC REAL, vitaminD REAL, vitaminE REAL, vitaminK REAL, vitaminB1 REAL, vitaminB2 REAL, " +
            "vitaminB3 REAL, vitaminB6 REAL, vitaminB12 REAL, userId long)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_VITAMINLOG_TABLE);
    }

    public long updateUserInformation(UserInfo user){
        SQLiteDatabase db = this.getWritableDatabase();

        UserInfo existsUser = getUser(user.getName());
        long userId;

        ContentValues values = new ContentValues();
        values.put("feet", user.getFeet());
        values.put("inch", user.getInch());
        values.put("weight", user.getWeight());
        values.put("gender", user.getGender());

        if(existsUser == null){
            values.put("name", user.getName());
            userId = db.insert(TABLE_USER, null, values);

        }else{
            userId = db.update(TABLE_USER, values, "name = ?", new String[] {user.getName()});
        }
        Log.e("updated", String.valueOf(userId));
        return userId;

    }

    public String updateVitaminLog(VitaminLog vitaminLog){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("userId", vitaminLog.getUserId());
        values.put("foodName", vitaminLog.getFoodName());
        values.put("vitaminA", vitaminLog.getVitaminA());
        values.put("vitaminC", vitaminLog.getVitaminC());
        values.put("vitaminD", vitaminLog.getVitaminD());
        values.put("vitaminE", vitaminLog.getVitaminE());
        values.put("vitaminK", vitaminLog.getVitaminK());
        values.put("vitaminB1", vitaminLog.getVitaminB1());
        values.put("vitaminB2", vitaminLog.getVitaminB2());
        values.put("vitaminB3", vitaminLog.getVitaminB3());
        values.put("vitaminB6", vitaminLog.getVitaminB6());
        values.put("vitaminB12", vitaminLog.getVitaminB12());
        values.put("logDate", getToday());

        long id = db.insert(TABLE_LOG, null, values);
        Log.e("inserted", String.valueOf(id));

        return "ok";
    }

    public List<VitaminLog> getVitaminLog(long userId){
        SQLiteDatabase db = this.getWritableDatabase();

        List<VitaminLog> vitaminLogList = new ArrayList<VitaminLog>();

        if(userId != 0){
            String today = getToday();

            String query = "SELECT * FROM " + TABLE_LOG + " WHERE userId = '" + userId + "'";
            Cursor c = db.rawQuery(query, null);

            while(c.moveToNext()){
                vitaminLogList.add(new VitaminLog(c.getLong(c.getColumnIndex("userId")), c.getString(c.getColumnIndex("foodName")),c.getDouble(c.getColumnIndex("vitaminA")),
                        c.getDouble(c.getColumnIndex("vitaminC")), c.getDouble(c.getColumnIndex("vitaminD")), c.getDouble(c.getColumnIndex("vitaminE")),
                        c.getDouble(c.getColumnIndex("vitaminK")), c.getDouble(c.getColumnIndex("vitaminB1")), c.getDouble(c.getColumnIndex("vitaminB2")),
                        c.getDouble(c.getColumnIndex("vitaminB3")), c.getDouble(c.getColumnIndex("vitaminB6")), c.getDouble(c.getColumnIndex("vitaminB12"))
                ));
            }
        }

        return vitaminLogList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        onCreate(db);
    }

    public UserInfo getUser(String userName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE name = '" + userName + "'";

        Cursor c = db.rawQuery(query, null);
        Log.e("C", String.valueOf(c.getCount()));
        if(c!=null && c.getCount() != 0){
            c.moveToFirst();
            Log.e("User", c.getString(c.getColumnIndex("name")));
            return new UserInfo(c.getLong(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")),
                    c.getInt(c.getColumnIndex("feet")), c.getInt(c.getColumnIndex("inch")), c.getInt(c.getColumnIndex("weight")),
                    c.getString(c.getColumnIndex("gender")));
        }else{
            return null;
        }


    }

    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null && db.isOpen()){
            db.close();
        }
    }

    public String getToday(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat dateFormation = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

        return dateFormation.format(cal.getTime());
    }

}
