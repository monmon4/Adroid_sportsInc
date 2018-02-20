package com.quantumsit.sportsinc.Entities;

/**
 * Created by Bassam on 2/1/2018.
 */

public class UserEntity {

    String name, phone, mail,pass,date_of_birth;
    int id, type, gender; //TYPE 0 Trainee, 1 Coach, 2 Admin, 3 Manager, 4 Backend, 5 non_registered
    //GENDER 0 Male, 1 Female

    public UserEntity(String name, String phone, String pass, String mail,
                      int id, int type, int gender, String date_of_birth) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.pass = pass;
        this.id = id;
        this.type = type;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
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
