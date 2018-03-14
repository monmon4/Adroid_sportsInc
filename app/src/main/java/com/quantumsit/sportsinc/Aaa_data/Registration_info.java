package com.quantumsit.sportsinc.Aaa_data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bassam on 1/24/2018.
 */

public class Registration_info {

    private String first_name, last_name, phone, nationality, address, email;
    private int gender;

    private String medical;

    private String P_first_name, first_nationality, first_address, first_mail, first_phone;
    private String P_second_name, second_nationality, second_address, second_mail, second_phone;

    private String E_first_name, E_first_phone;
    private String E_second_name, E_second_phone;

    private int hear_about_us;

    public Registration_info() {

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public String getP_first_name() {
        return P_first_name;
    }

    public void setP_first_name(String p_first_name) {
        P_first_name = p_first_name;
    }

    public String getFirst_nationality() {
        return first_nationality;
    }

    public void setFirst_nationality(String first_nationality) {
        this.first_nationality = first_nationality;
    }

    public String getFirst_address() {
        return first_address;
    }

    public void setFirst_address(String first_address) {
        this.first_address = first_address;
    }

    public String getFirst_mail() {
        return first_mail;
    }

    public void setFirst_mail(String first_mail) {
        this.first_mail = first_mail;
    }

    public String getFirst_phone() {
        return first_phone;
    }

    public void setFirst_phone(String first_phone) {
        this.first_phone = first_phone;
    }

    public String getP_second_name() {
        return P_second_name;
    }

    public void setP_second_name(String p_second_name) {
        P_second_name = p_second_name;
    }

    public String getSecond_nationality() {
        return second_nationality;
    }

    public void setSecond_nationality(String second_nationality) {
        this.second_nationality = second_nationality;
    }

    public String getSecond_address() {
        return second_address;
    }

    public void setSecond_address(String second_address) {
        this.second_address = second_address;
    }

    public String getSecond_mail() {
        return second_mail;
    }

    public void setSecond_mail(String second_mail) {
        this.second_mail = second_mail;
    }

    public String getSecond_phone() {
        return second_phone;
    }

    public void setSecond_phone(String second_phone) {
        this.second_phone = second_phone;
    }

    public String getE_first_name() {
        return E_first_name;
    }

    public void setE_first_name(String e_first_name) {
        E_first_name = e_first_name;
    }

    public String getE_first_phone() {
        return E_first_phone;
    }

    public void setE_first_phone(String e_first_phone) {
        E_first_phone = e_first_phone;
    }

    public String getE_second_name() {
        return E_second_name;
    }

    public void setE_second_name(String e_second_name) {
        E_second_name = e_second_name;
    }

    public String getE_second_phone() {
        return E_second_phone;
    }

    public void setE_second_phone(String e_second_phone) {
        E_second_phone = e_second_phone;
    }

    public int getHear_about_us() {
        return hear_about_us;
    }

    public void setHear_about_us(int hear_about_us) {
        this.hear_about_us = hear_about_us;
    }
}
