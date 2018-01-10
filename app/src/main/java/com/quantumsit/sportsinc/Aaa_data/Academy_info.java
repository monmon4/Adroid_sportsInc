package com.quantumsit.sportsinc.Aaa_data;

/**
 * Created by Mona on 09-Jan-18.
 */

public class Academy_info {

    int id;
    String address;
    String phone;

    public Academy_info(int id, String address, String phone) {
        this.id = id;
        this.address = address;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
