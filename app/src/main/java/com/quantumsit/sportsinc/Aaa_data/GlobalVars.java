package com.quantumsit.sportsinc.Aaa_data;

import android.app.Application;
import android.content.Context;

import com.quantumsit.sportsinc.BuildConfig;
import com.quantumsit.sportsinc.Entities.BookingCourseEntity;
import com.quantumsit.sportsinc.Entities.Booking_info;
import com.quantumsit.sportsinc.Entities.UserEntity;

import net.gotev.uploadservice.UploadService;

import java.util.ArrayList;

/**
 * Created by Mona on 31-Dec-17.
 */

public class GlobalVars extends Application {

    private static Context mContext;
    DB_Sqlite_Handler myDB;

    String name ="",imgUrl , phone ="", pass ="", mail ="" ,date_of_birth ="";
    int id , type, gender, age; //TYPE 0 Trainee, 1 Coach, 2 Admin, 3 Manager, 4 Backend, 5 non_registered
                          //GENDER 0 Male, 1 Female
    int person_id;

    public UserEntity myAccount;

    int course_id = -1, class_id=-1;

    boolean parent = false;

    Booking_info booking_info;

    public Booking_info getBooking_info() {
        return booking_info;
    }

    public void setBooking_info(Booking_info booking_info) {
        this.booking_info = booking_info;
    }

    public ArrayList<BookingCourseEntity> bookingCourseEntities = new ArrayList<>();

    public ArrayList<BookingCourseEntity> getBookingCourseEntities() {
        return bookingCourseEntities;
    }

    public void addBookingCourseEntities(BookingCourseEntity bookingCourseEntity) {
        this.bookingCourseEntities.add(bookingCourseEntity);
    }

    public void clearBookingCourseEntities () {
        this.bookingCourseEntities.clear();
    }

    public void removeBookingCourseEntities(int i) {
        this.bookingCourseEntities.remove(i);
    }

    public GlobalVars() {
        mContext = this;
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Or, you can define it manually.
        UploadService.NAMESPACE = "com.example.bassam.sporstincmanger";
    }

    public static Context getContext(){
        return mContext;
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
        myAccount = null;
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
        if (myAccount != null)
            return myAccount;
        else
            return this.getUser();
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
        if (myAccount != null && myAccount.getId() == person_id)
            myAccount.setName(name);
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        if (myAccount != null && myAccount.getId() == person_id)
            myAccount.setImgUrl(imgUrl);
        this.imgUrl = imgUrl;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        if (myAccount != null && myAccount.getId() == person_id)
            myAccount.setPass(pass);
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (myAccount != null && myAccount.getId() == person_id)
            myAccount.setPhone(phone);
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        if (myAccount != null && myAccount.getId() == person_id)
            myAccount.setMail(mail);
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

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
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
