package com.quantumsit.sportsinc.Aaa_data;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Mona on 31-Dec-17.
 */

public class GlobalVars extends Application {

    DB_Sqlite_Handler myDB;

    String name, phone, pass, mail, date_of_birth;

    int id, type, gender; //TYPE 0 Trainee, 1 Coach, 2 Admin, 3 Manager, 4 Backend, 5 non_registered
                          //GENDER 0 Male, 1 Female


    @Override
    public void onCreate() {
        super.onCreate();
        myDB = new DB_Sqlite_Handler(getApplicationContext());
    }

    public void settAll (String name, String phone, String pass,String mail,
                         int id, int type, int gender, String date_of_birth){
        this.name = name;
        this.phone = phone;
        this.pass = pass;
        this.mail = mail;

        this.id = id;
        this.type = type;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGender() {
        return gender;
    }

    public String getPersonGender() {
        String PersonGender = "";
        switch (gender){
            case 0:
                PersonGender = "Male";
                break;
            case 1:
                PersonGender = "Female";
        }
        return PersonGender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public DB_Sqlite_Handler getMyDB() {
        return myDB;
    }
}
