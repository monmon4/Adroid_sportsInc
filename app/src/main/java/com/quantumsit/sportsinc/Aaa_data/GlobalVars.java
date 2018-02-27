package com.quantumsit.sportsinc.Aaa_data;

import android.app.Application;

import com.quantumsit.sportsinc.BuildConfig;
import com.quantumsit.sportsinc.Entities.UserEntity;

import net.gotev.uploadservice.UploadService;

import java.util.ArrayList;

/**
 * Created by Mona on 31-Dec-17.
 */

public class GlobalVars extends Application {

    DB_Sqlite_Handler myDB;

    String name ="",imgUrl , phone ="", pass ="", mail ="" ,date_of_birth ="";
    int id , type, gender, age; //TYPE 0 Trainee, 1 Coach, 2 Admin, 3 Manager, 4 Backend, 5 non_registered
                          //GENDER 0 Male, 1 Female
    int person_id;
    ArrayList<course_info> courses;

    UserEntity myAccount;


    public GlobalVars() {
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Or, you can define it manually.
        UploadService.NAMESPACE = "com.example.bassam.sporstincmanger";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myDB = new DB_Sqlite_Handler(getApplicationContext());
    }

    public void settAll (String name,String imgUrl , String phone, String pass, String mail,
                         int id, int type, int gender, String date_of_birth){
        this.name = name;
        this.imgUrl = imgUrl;
        this.phone = phone;
        this.pass = pass;
        this.mail = mail;

        this.id = id;
        this.person_id = id;
        this.type = type;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
    }

    public void settAll (String name, String phone, String mail,
                            int id, int type, int gender, int age){
        this.name = name;
        this.phone = phone;
        this.mail = mail;

        this.id = id;
        this.person_id = id;
        this.type = type;
        this.gender = gender;
        this.age = age;
    }

    public UserEntity getMyAccount() {
        return myAccount;
    }

    public void setMyAccount(UserEntity myAccount) {
        this.myAccount = myAccount;
    }

    public void setUser(UserEntity user) {
        this.name = user.getName();
        this.imgUrl = user.getImgUrl();
        this.phone = user.getPhone();
        this.pass = user.getPass();
        this.mail = user.getMail();

        this.id = user.getId();
        this.type = user.getType();
        this.gender = user.getGender();
        this.date_of_birth = user.getDate_of_birth();
    }

    public UserEntity getUser(){
        UserEntity entity = new  UserEntity(this.name,this.imgUrl, this.phone, this.pass, this.mail,
                this.id, this.type, this.gender, this.date_of_birth);
        return entity;
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

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public DB_Sqlite_Handler getMyDB() {
        return myDB;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
}
