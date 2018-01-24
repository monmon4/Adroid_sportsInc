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


    // academy table name
    private String TABLE_AcademyInfo = Constants.TABLE_AcademyInfo;
    // Shops Table Columns names
    private String KeyId = "id";
    private String KeyAddress = "address";
    private String KeyPhone = "phone";


    //Coach Running Class Info...

    // Class table name
    private String TABLE_classes = "class";
    // Class Table Columns names
    private String KeyClassId = "class_id";
    private String KeyClassName = "class_name";
    private String KeyClassDate = "class_date";
    private String KeyClassNote = "class_note";
    private String KeyClassGroup = "group_id";


    // Rules Table name
    private String TABLE_rules = "rules";
    // Rules Table Columns names
    private String KeyRuleId = "rule_id";
    private String KeyRuleClass = "class_id";
    private String KeyRuleName = "rule_name";
    private String KeyRuleCheck = "rule_check";
    private String KeyRuleNote = "rule_note";

    // ClassTrainees Table name
    private String TABLE_trainee = "trainees";
    // ClassTrainees Table Columns name
    private String KeyTraineeId = "trainee_id";
    private String KeyTraineeName = "trainee_name";
    private String KeyTraineeClass = "class_id";
    private String KeyTraineeAttend = "trainee_attend";
    private String KeyTraineeScore = "trainee_score";
    private String KeyTraineeNote = "trainee_note";


    public DB_Sqlite_Handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ACADEMY_INFO_TABLE = "CREATE Table" + TABLE_AcademyInfo + "("
        + KeyId + "INTEGER PRIMARY KEY," + KeyAddress + "TEXT,"
        + KeyPhone + "TEXT" + ")";

        String CREATE_Class_TABLE = "CREATE Table" + TABLE_classes + "("
                + KeyClassId + "INTEGER PRIMARY KEY," + KeyClassName + "TEXT,"
                + KeyClassDate + "TEXT" + KeyClassNote + "TEXT"
                + KeyClassGroup + "INTEGER" + ")";

        String CREATE_Rules_TABLE = "CREATE Table" + TABLE_rules + "("
                + KeyRuleId + "INTEGER PRIMARY KEY," + KeyRuleName + "TEXT,"
                + KeyRuleNote + "TEXT" + KeyRuleCheck + "INTEGER"
                +KeyRuleClass + "INTEGER" + ")";

        String CREATE_Trainee_TABLE = "CREATE Table" + TABLE_trainee + "("
                + KeyTraineeId + "INTEGER PRIMARY KEY," + KeyTraineeName + "TEXT,"
                + KeyTraineeNote + "TEXT" + KeyTraineeScore+ "INTEGER"
                + KeyTraineeAttend + "INTEGER" + KeyTraineeClass + "INTEGER" + ")";

        db.execSQL(CREATE_ACADEMY_INFO_TABLE);
        db.execSQL(CREATE_Class_TABLE);
        db.execSQL(CREATE_Rules_TABLE);
        db.execSQL(CREATE_Trainee_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_AcademyInfo);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_classes);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_rules);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_trainee);
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


    public void addClass(){
        
    }
}
