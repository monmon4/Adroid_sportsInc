package com.quantumsit.sportsinc.Aaa_data;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Mona on 31-Dec-17.
 */

public class GlobalVars extends Application {

    int user_is ; //1 parent, 2 coach, 3 admin, 4 non_registered

    String name, phone, mail, gender, birth_date;
    ArrayList<course_info> courses;


    public int getUser_is() {
        return user_is;
    }

    public void setUser_is(int user_is) {
        this.user_is = user_is;
    }

}
