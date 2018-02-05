package com.quantumsit.sportsinc.Aaa_data;

/**
 * Created by Mona on 09-Jan-18.
 */

public class Academy_info {

    int id;
    String address , name;
    String phone;
    String Lat , Lng;

    public Academy_info() {
    }

    public Academy_info(int id, String address, String phone) {
        this.id = id;
        this.address = address;
        this.phone = phone;
    }

    public Academy_info(int id,String name , String address, String phone, String lat, String lng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.Lat = lat;
        this.Lng = lng;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

}
