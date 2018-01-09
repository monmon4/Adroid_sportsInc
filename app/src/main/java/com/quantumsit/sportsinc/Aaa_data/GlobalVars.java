package com.quantumsit.sportsinc.Aaa_data;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Mona on 31-Dec-17.
 */

public class GlobalVars extends Application {

    int user_is ; //1 parent, 2 coach, 3 admin, 4 non_registered

    String name, phone, mail;

    int id, type, gender, age; //TYPE 0 Trainee, 1 Coach, 2 Admin, 3 Manager, 4 Backend, 5 non_registered
                          //GENDER 0 Male, 1 Female


    ArrayList<course_info> courses;


    public int getUser_is() {
        return user_is;
    }

    public void setUser_is(int user_is) {
        this.user_is = user_is;
    }

    public void settAll (String name, String phone, String mail,
                            int id, int type, int gender, int age){
        this.name = name;
        this.phone = phone;
        this.mail = mail;

        this.id = id;
        this.type = type;
        this.gender = gender;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
