package com.aurora.elezov.myapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRABHU on 11/12/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="ROUTES";
    private static final int DATABASE_VERSION = 1;
    private static final String ROUTE_TABLE = "routetab";
    private static final String STU_TABLE = "create table "+ROUTE_TABLE +"(route TEXT, email TEXT )";

Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(STU_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ROUTE_TABLE);

        // Create tables again
        onCreate(db);
    }
/* Insert into database*/
    public void insertIntoDB(String route,String email){
        Log.d("insert", "before insert");

        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("route", route);
        values.put("email", email);

        // insert
        db.insert(ROUTE_TABLE, null, values);

        // close
        db.close();

    }
/* Retrive  data from datab
ase */
    public List<DatabaseModel> getDataFromDB(String USER_EMAIL){
        List<DatabaseModel> modelList = new ArrayList<DatabaseModel>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ROUTE_TABLE+" WHERE email = '"+USER_EMAIL+"'", null);

        if (cursor.moveToFirst()){
            do {
             //   if (cursor.getString(1) == USER_EMAIL) {
                    DatabaseModel model = new DatabaseModel();
                    model.setRoute(cursor.getString(0));
                    model.setEmail(cursor.getString(1));
                    modelList.add(model);
              //  }
            }while (cursor.moveToNext());
        }


        Log.i("student data", modelList.toString());


        return modelList;
    }



}
