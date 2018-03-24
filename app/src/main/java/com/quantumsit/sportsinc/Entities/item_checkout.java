package com.quantumsit.sportsinc.Entities;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item_checkout {

    String image_url, class_name, number_of_trainees, trainees_names, price;

    public item_checkout(String image_url, String class_name, String number_of_trainees, String trainees_names, String price) {
        this.image_url = image_url;
        this.class_name = class_name;
        this.number_of_trainees = number_of_trainees;
        this.trainees_names = trainees_names;
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getNumber_of_trainees() {
        return number_of_trainees;
    }

    public String getTrainees_names() {
        return trainees_names;
    }

    public String getPrice() {
        return price;
    }
}
