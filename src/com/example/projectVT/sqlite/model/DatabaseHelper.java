package com.example.projectVT.sqlite.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.projectVT.sqlite.helper.UserInfo;

/**
 * Created by Jay on 4/14/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "projectVT";

    //Table name
    private static final String TABLE_USER = "user";

    //column name
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_FT = "feet";
    private static final String KEY_IN = "inch";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_GENDER = "gender";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT, " +
            KEY_FT + " INTEGER, " + KEY_IN + " INTEGER, " + KEY_WEIGHT + " INTEGER, " + KEY_GENDER + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(CREATE_TABLE);
    }

    public long updateUserInformation(UserInfo user){
        SQLiteDatabase db = this.getWritableDatabase();

        UserInfo existsUser = getUser(user.getName());
        long userId;

        ContentValues values = new ContentValues();
        values.put(KEY_FT, user.getFeet());
        values.put(KEY_IN, user.getInch());
        values.put(KEY_WEIGHT, user.getWeight());
        values.put(KEY_GENDER, user.getGender());

        if(existsUser == null){
            values.put(KEY_NAME, user.getName());
            userId = db.insert(TABLE_USER, null, values);

        }else{
            userId = db.update(TABLE_USER, values, KEY_NAME + " = ?", new String[] {user.getName()});
        }
        Log.e("updated", String.valueOf(userId));
        return userId;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        onCreate(db);
    }

    public UserInfo getUser(String userName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_NAME + " = '" + userName + "'";

        Cursor c = db.rawQuery(query, null);

        if(c!=null && c.getCount() != 0){
            c.moveToFirst();
            return new UserInfo(c.getLong(c.getColumnIndex(KEY_ID)), c.getString(c.getColumnIndex(KEY_NAME)),
                    c.getInt(c.getColumnIndex(KEY_FT)), c.getInt(c.getColumnIndex(KEY_IN)), c.getInt(c.getColumnIndex(KEY_WEIGHT)),
                    c.getString(c.getColumnIndex(KEY_GENDER)));
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

}
