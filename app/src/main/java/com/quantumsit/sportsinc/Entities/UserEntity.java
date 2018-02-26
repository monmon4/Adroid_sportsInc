package com.quantumsit.sportsinc.Entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bassam on 2/1/2018.
 */

public class UserEntity {

    String name,imgUrl , phone, mail,pass,date_of_birth;
    int id, type, gender; //TYPE 0 Trainee, 1 Coach, 2 Admin, 3 Manager, 4 Backend, 5 non_registered
    //GENDER 0 Male, 1 Female

    public UserEntity(String name,String imgUrl , String phone, String pass, String mail,
                      int id, int type, int gender, String date_of_birth) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.phone = phone;
        this.mail = mail;
        this.pass = pass;
        this.id = id;
        this.type = type;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
    }

    public UserEntity(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
            this.imgUrl = jsonObject.getString("ImageUrl");
            this.phone = jsonObject.getString("phone");
            this.mail = jsonObject.getString("email");
            this.pass = jsonObject.getString("pass");
            this.id = jsonObject.getInt("id");
            this.type = jsonObject.getInt("type");
            this.gender = jsonObject.getInt("gender");
            this.date_of_birth = jsonObject.getString("date_of_birth");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
}
