package com.quantumsit.sportsinc.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bassam on 12/26/2017.
 */

public class Booking_info implements Serializable {
    String name, mail, phone, nationality, day_of_birth , month_of_birth, year_of_birth, address;
    int gender;

    String medical;

    String f_name, f_nationality, f_address,f_mail, f_phone;
    String m_name, m_nationality, m_address,m_mail, m_phone;

    String e1_name, e1_phone, e2_name, e2_phone;
    String hear_about_us;


    public Booking_info() {
    }

    public void setSecond(String medical) {
        this.medical = medical;
    }

    public void setFirst(String name, String mail, String phone, String nationality,
                         String  day_of_birth,String month_of_birth,String year_of_birth,
                         String address, int gender) {
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.nationality = nationality;
        this.address = address;
        this.day_of_birth = day_of_birth;
        this.month_of_birth = month_of_birth;
        this.year_of_birth = year_of_birth;
        this.gender = gender;
    }

    public void setThird(String f_name,String f_nationality,String f_address,String f_mail,String f_phone,
            String m_name,String m_nationality,String m_address,String m_mail,String m_phone){

        this.f_name = f_name;
        this.f_address = f_address;
        this.f_mail = f_mail;
        this.f_nationality = f_nationality;
        this.f_phone = f_phone;
        this.m_name = m_name;
        this.m_address = m_address;
        this.m_mail = m_mail;
        this.m_nationality = m_nationality;
        this.m_phone = m_phone;
    }

    public  void setForth(String e1_name,String e1_phone,String e2_name,String e2_phone,
            String hear_about_us){
        this.e1_name = e1_name;
        this.e1_phone = e1_phone;
        this.e2_name = e2_name;
        this.e2_phone = e2_phone;
        this.hear_about_us = hear_about_us;

    }




    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public String getNationality() {
        return nationality;
    }

    public String getAddress() {
        return address;
    }

    public int getGender() {
        return gender;
    }

    public String getMedical() {
        return medical;
    }

    public String getF_name() {
        return f_name;
    }

    public String getF_nationality() {
        return f_nationality;
    }

    public String getF_address() {
        return f_address;
    }

    public String getF_mail() {
        return f_mail;
    }

    public String getF_phone() {
        return f_phone;
    }

    public String getM_name() {
        return m_name;
    }

    public String getM_nationality() {
        return m_nationality;
    }

    public String getM_address() {
        return m_address;
    }

    public String getM_mail() {
        return m_mail;
    }

    public String getM_phone() {
        return m_phone;
    }

    public String getE1_name() {
        return e1_name;
    }

    public String getE1_phone() {
        return e1_phone;
    }

    public String getE2_name() {
        return e2_name;
    }

    public String getE2_phone() {
        return e2_phone;
    }

    public String getHear_about_us() {
        return hear_about_us;
    }

    public String getDay_of_birth() {
        return day_of_birth;
    }

    public String getMonth_of_birth() {
        return month_of_birth;
    }

    public String getYear_of_birth() {
        return year_of_birth;
    }
}
