package com.quantumsit.sportsinc.Aaa_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
    private String KeyName = "name";
    private String KeyAddress = "address";
    private String KeyPhone = "phone";
    private String KeyLat = "Address_Lat";
    private String KeyLng = "Address_Lng";

    //Coach Running Class Info...

    // Class table name
    private String TABLE_classes = "myRunningClass";
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
    private String KeyPrimary = "ID";
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

        String CREATE_ACADEMY_INFO_TABLE = "CREATE Table " + TABLE_AcademyInfo + "("
        + KeyId + " INTEGER PRIMARY KEY AUTOINCREMENT," + KeyName + " TEXT," + KeyAddress + " TEXT,"
        + KeyLat + " TEXT, " + KeyLng + " TEXT,"
        + KeyPhone + " TEXT" + ")";

        String CREATE_Class_TABLE = "CREATE Table " + TABLE_classes + "("
                + KeyClassId + " INTEGER PRIMARY KEY," + KeyClassName + " TEXT,"
                + KeyClassDate + " TEXT," + KeyClassNote + " TEXT,"
                + KeyClassGroup + " INTEGER" + ")";

        String CREATE_Rules_TABLE = "CREATE Table " + TABLE_rules + "("
                + KeyRuleId + " INTEGER PRIMARY KEY," +KeyRuleClass + " INTEGER, "
                + KeyRuleName + " TEXT,"+ KeyRuleCheck + " INTEGER,"
                + KeyRuleNote + " TEXT" + ")";

        String CREATE_Trainee_TABLE = "CREATE Table " + TABLE_trainee + "("
                + KeyPrimary + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KeyTraineeId + " INTEGER ,"+ KeyTraineeName + " TEXT,"
                + KeyTraineeClass + " INTEGER, "+ KeyTraineeAttend + " INTEGER,"
                + KeyTraineeScore+ " INTEGER," + KeyTraineeNote + " TEXT"  + ")";

        db.execSQL(CREATE_ACADEMY_INFO_TABLE);
        db.execSQL(CREATE_Class_TABLE);
        db.execSQL(CREATE_Rules_TABLE);
        db.execSQL(CREATE_Trainee_TABLE);
    }

    public String DBTablesName(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        String x = "";
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                x = "Table Name=> "+c.getString(0)+"\n";
                c.moveToNext();
            }
        }
        return x;
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
        values.put(KeyName, info.getName());
        values.put(KeyAddress, info.getAddress());
        values.put(KeyLat, info.getLat());
        values.put(KeyLng, info.getLng());
        values.put(KeyPhone, info.getPhone());
        // Inserting Row
        db.insert(Constants.TABLE_AcademyInfo, null, values);
        db.close(); // Closing database connection
    }


    public boolean addClass(MyClass_info info){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KeyClassId,info.getClass_id());
        values.put(KeyClassName,info.getClass_name());
        values.put(KeyClassDate,info.getClass_date());
        values.put(KeyClassNote,info.getClass_note());
        values.put(KeyClassGroup,info.getGroup_id());

        long rowInserted = db.insert(TABLE_classes,null,values);
        db.close();

        if (rowInserted != -1)
            return true;

        return false;
    }

    public void addRule(Rule_info info){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KeyRuleId,info.getRule_id());
        values.put(KeyRuleClass,info.getClass_id());
        values.put(KeyRuleName,info.getRule_name());
        values.put(KeyRuleCheck,info.getRule_check());
        values.put(KeyRuleNote,info.getRule_note());

        db.insert(TABLE_rules,null,values);
        db.close();
    }

    public void addTrainee(Trainees_info info){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KeyTraineeId,info.getTrainee_id());
        values.put(KeyTraineeClass,info.getClass_id());
        values.put(KeyTraineeName,info.getTrainee_name());
        values.put(KeyTraineeAttend,info.getTrainee_attend());
        values.put(KeyTraineeScore,info.getTrainee_score());
        values.put(KeyTraineeNote,info.getTrainee_note());

        db.insert(TABLE_trainee,null,values);
        db.close();
    }

    public boolean Academy_empty(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM "+TABLE_AcademyInfo;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0)
            return false;
        return true;
    }

    public Academy_info getAcademyInfo () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_AcademyInfo, null);

        if (cursor != null)
            cursor.moveToFirst();
        Academy_info info = new Academy_info(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2) , cursor.getString(3), cursor.getString(4),cursor.getString(5));

        return info;
    }

    public List<Rule_info> getUncheckedRules(int class_id){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Rule_info> rules = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from "+TABLE_rules+" where "+KeyRuleClass+" = "+class_id+" And rule_check = 0",null);
        if (cursor.getCount() !=0){
            while (cursor.moveToNext()){
                Rule_info info = new Rule_info(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4));
                rules.add(info);
            }
        }
        return rules;
    }

    public List<Rule_info> getAllRules(int class_id){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Rule_info> rules = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from "+TABLE_rules+" where "+KeyRuleClass+" = "+class_id+" ",null);
        if (cursor.getCount() !=0){
            while (cursor.moveToNext()){
                Rule_info info = new Rule_info(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4));
                rules.add(info);
            }
        }
        return rules;
    }

    public List<Rule_info> getRules(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Rule_info> rules = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from "+TABLE_rules,null);
        if (cursor.getCount() !=0){
            while (cursor.moveToNext()){
                Rule_info info = new Rule_info(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4));
                rules.add(info);
            }
        }
        return rules;
    }

    public List<Trainees_info> getTrainees(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Trainees_info> trainees = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from "+TABLE_trainee,null);
        if (cursor.getCount() !=0){
            while (cursor.moveToNext()){
                Trainees_info info = new Trainees_info(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
                trainees.add(info);
            }
        }
        return trainees;
    }

    public List<Trainees_info> getClassTrainees(int class_id){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Trainees_info> trainees = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from "+TABLE_trainee+" where "+KeyTraineeClass+" = "+class_id,null);
        if (cursor.getCount() !=0){
            while (cursor.moveToNext()){
                Trainees_info info = new Trainees_info(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
                trainees.add(info);
            }
        }
        return trainees;
    }

    public List<Trainees_info> getClassAttendTrainees(int class_id){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Trainees_info> trainees = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from "+TABLE_trainee+" where "+KeyTraineeAttend+" = 1 AND "+KeyTraineeClass+" = "+class_id,null);
        if (cursor.getCount() !=0){
            while (cursor.moveToNext()){
                Trainees_info info = new Trainees_info(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
                trainees.add(info);
            }
        }
        return trainees;
    }

    public List<MyClass_info> getAllClasses(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyClass_info> classes = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from "+TABLE_classes,null);
        if (cursor.getCount() !=0){
            while (cursor.moveToNext()){
                MyClass_info info = new MyClass_info(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4));
                classes.add(info);
            }
        }
        return classes;
    }

    public MyClass_info getRunningClass(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from "+TABLE_classes,null);
        MyClass_info info = null;
        if (cursor.getCount() !=0){
            if (cursor.moveToNext()){
                info = new MyClass_info(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4));
            }
        }
        return info;
    }

    public int getClassesCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_classes,null);
        return cursor.getCount();
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

    public int updateClass(MyClass_info info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KeyClassId,info.getClass_id());
        values.put(KeyClassName,info.getClass_name());
        values.put(KeyClassDate,info.getClass_date());
        values.put(KeyClassNote,info.getClass_note());
        values.put(KeyClassGroup,info.getGroup_id());
        // updating row
        return db.update(TABLE_classes, values, KeyClassId + " = ?",
                new String[]{String.valueOf(info.getClass_id())});
    }

    public int updateRule(Rule_info info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KeyRuleId,info.getRule_id());
        values.put(KeyRuleClass,info.getClass_id());
        values.put(KeyRuleName,info.getRule_name());
        values.put(KeyRuleNote,info.getRule_note());
        values.put(KeyRuleCheck,info.getRule_check());
        // updating row
        return db.update(TABLE_rules, values,  KeyRuleId+ " = ?",
                new String[]{String.valueOf(info.getRule_id())});
    }

    public int updateTrainee(Trainees_info info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KeyTraineeId,info.getTrainee_id());
        values.put(KeyTraineeClass,info.getClass_id());
        values.put(KeyTraineeName,info.getTrainee_name());
        values.put(KeyTraineeAttend,info.getTrainee_attend());
        values.put(KeyTraineeScore,info.getTrainee_score());
        values.put(KeyTraineeNote,info.getTrainee_note());
        // updating row
        return db.update(TABLE_trainee, values, KeyPrimary + " = ?",
                new String[]{String.valueOf(info.getID())});
    }
    public void deleteAcademyInfo(Academy_info info) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_AcademyInfo, KeyId + " = ?",
                new String[] { String.valueOf(info.getId()) });
        db.close();
    }

    public boolean deleteClass(MyClass_info info){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_classes, KeyClassId + " = ?",
                new String[] { String.valueOf(info.getClass_id()) });
        db.close();
        boolean deleteRules = deleteClassRules(info.getClass_id());
        boolean deleteTrainees = deleteClassTrainees(info.getClass_id());
        boolean deleteClass = false;

        if (result > 0 )
            deleteClass =  true;

        if (deleteClass && deleteRules && deleteTrainees)
            return true;
        return false;
    }

    public boolean deleteClassTrainees(int class_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_trainee, KeyTraineeClass + " = ?",
                new String[] { String.valueOf(class_id) });
        db.close();

        if (result > 0 )
            return true;
        return false;
    }

    private boolean deleteClassRules(int class_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_rules, KeyRuleClass + " = ?",
                new String[] { String.valueOf(class_id) });
        db.close();

        if (result > 0 )
            return true;
        return false;
    }


}
