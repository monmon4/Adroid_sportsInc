package com.quantumsit.sportsinc.Aaa_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mona on 09-Jan-18.
 */

public class DB_Sqlite_Handler extends SQLiteOpenHelper {

    public String table_name;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SportsInc";


    // table name
    private static final String TABLE_AcademyInfo = Constants.TABLE_AcademyInfo;
    // Shops Table Columns names
    private static final String KeyId = "id";
    private static final String KeyAddress = "address";
    private static final String KeyPhone = "phone";



    public DB_Sqlite_Handler(Context context, String table_name) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.table_name = table_name;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ACADEMY_INFO_TABLE = "CREATE Table" + TABLE_AcademyInfo + "("
        + KeyId + "INTEGER PRIMARY KEY," + KeyAddress + "TEXT,"
        + KeyPhone + "TEXT" + ")";

        db.execSQL(CREATE_ACADEMY_INFO_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + table_name);
        // Creating tables again
        onCreate(db);
    }

    public void addAcademyInfo(Academy_info info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KeyAddress, info.getAddress());
        values.put(KeyPhone, info.getPhone());
        // Inserting Row
        db.insert(Constants.TABLE_AcademyInfo, null, values);
        db.close(); // Closing database connection
    }

    public Academy_info getAcademyInfo (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_AcademyInfo, new String[] { KeyId,
                        KeyAddress, KeyPhone }, KeyId + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Academy_info info = new Academy_info(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return info;
    }

    public int updateAcademyInfo(Academy_info info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KeyAddress, info.address);
        values.put(KeyPhone, info.getPhone());
        // updating row
        return db.update(Constants.TABLE_AcademyInfo, values, KeyId + " = ?",
                new String[]{String.valueOf(info.getId())});
    }

    public void deleteAcademyInfo(Academy_info info) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_AcademyInfo, KeyId + " = ?",
                new String[] { String.valueOf(info.getId()) });
        db.close();
    }

}
