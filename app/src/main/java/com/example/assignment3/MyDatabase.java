package com.example.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    public Context context;
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="PasswordManager.db";
    public static final String TABLE_NAME="my_library";
    public static final String COLUMN_NAME="_name";
    public static final String COLUMN_URL="_url";
    public static final String COLUMN_PASSWORD="_password";
    public static final String COLUMN_ID="_id";

    public static final String USER_TABLE = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_UPASSWORD = "password";

    public static final String RECYCLE_BIN_TABLE = "recycle_bin";
    public static final String COLUMN_DELETED_NAME = "deleted_name";
    public static final String COLUMN_DELETED_URL = "deleted_url";
    public static final String COLUMN_DELETED_PASSWORD = "deleted_password";
    public static final String COLUMN_DELETE_TIME = "delete_time";

    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "CREATE TABLE "+TABLE_NAME+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_NAME+" TEXT, "+COLUMN_URL+" TEXT, "+COLUMN_PASSWORD+" TEXT);";

        String query1 = "CREATE TABLE " + USER_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_UPASSWORD + " TEXT);";
        String query2 = "CREATE TABLE " + RECYCLE_BIN_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DELETED_NAME + " TEXT, " +
                COLUMN_DELETED_URL + " TEXT, " +
                COLUMN_DELETED_PASSWORD + " TEXT, " +
                COLUMN_DELETE_TIME + " INTEGER);";
        db.execSQL(query);
        db.execSQL(query1);
        db.execSQL(query2);
        // Insert hardcoded user data
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, "samrazafar");
        cv.put(COLUMN_UPASSWORD, "123456");
        long result = db.insert(USER_TABLE, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    void add(String title, String url, String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME,title);
        cv.put(COLUMN_URL,url);
        cv.put(COLUMN_PASSWORD,password);
        long result=db.insert(TABLE_NAME,null,cv);
        if(result==-1){
            Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Added Successfully!",Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readAllData(){
        String query="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=null;
        if(db!=null){
            cursor=db.rawQuery(query,null);
        }
        return cursor;
    }
    void updateData(String row_id,String title, String url, String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME,title);
        cv.put(COLUMN_URL,url);
        cv.put(COLUMN_PASSWORD,password);

        long result=db.update(TABLE_NAME,cv,"_id=?",new String[]{row_id});
        if(result==-1){
            Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Updates Successfully!",Toast.LENGTH_SHORT).show();
        }
    }
//DELETE WITHOUT RECYCLE BIN FUNCTIONALITY
    void DeleteRow(String row_id){
        SQLiteDatabase db=this.getWritableDatabase();
        long result=db.delete(TABLE_NAME,"_id=?",new String[]{row_id});
        if(result==-1){
            Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Entry Deleted!",Toast.LENGTH_SHORT).show();
        }
    }

//RECYCLE BIN FUNCTIONALITY

    void moveToRecycleBin(String row_id, String name, String url, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DELETED_NAME, name);
        cv.put(COLUMN_DELETED_URL, url);
        cv.put(COLUMN_DELETED_PASSWORD, password);
        cv.put(COLUMN_DELETE_TIME, System.currentTimeMillis()); // Timestamp in milliseconds
        long result = db.insert(RECYCLE_BIN_TABLE, null, cv);
        if (result != -1) {
            // Item moved to recycle bin, now delete from main table
            DeleteRow(row_id);
            Toast.makeText(context, "Item moved to recycle bin", Toast.LENGTH_SHORT).show();
            ((Home) context).updateDataAndNotifyAdapter();
        } else {
            Toast.makeText(context, "Failed to move item to recycle bin", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteFromRecycleBin(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(RECYCLE_BIN_TABLE, "_id=?", new String[]{row_id});
        if (result != -1) {
            Toast.makeText(context, "Item permanently deleted from recycle bin", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to delete item from recycle bin", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to restore item from recycle bin to main table
    public void restoreFromRecycleBin(String row_id, String name, String url, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_URL, url);
        cv.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result != -1) {
            // Item restored to main table, now delete from recycle bin
            deleteFromRecycleBin(row_id);
            Toast.makeText(context, "Item restored from recycle bin", Toast.LENGTH_SHORT).show();
            if (callback != null) {
                callback.onItemRestored(); // Notify the UI
            }
        } else {
            Toast.makeText(context, "Failed to restore item from recycle bin", Toast.LENGTH_SHORT).show();
        }
    }
    public Cursor getRecycleBinData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + RECYCLE_BIN_TABLE;
        return db.rawQuery(query, null);
    }

    public void registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_PASSWORD, password);
        long result = db.insert(USER_TABLE, null, cv);
        if(result==-1){
            Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show();
        }
    }
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE +
                " WHERE " + COLUMN_USERNAME + " = ? AND " +
                COLUMN_UPASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean loginSuccessful = cursor.getCount() > 0;
        cursor.close();
        return loginSuccessful;
    }

    private DatabaseCallback callback;

    public void setCallback(DatabaseCallback callback) {
        this.callback = callback;
    }

    public interface DatabaseCallback {
        void onDataUpdated();
        void onItemRestored();
    }
}
