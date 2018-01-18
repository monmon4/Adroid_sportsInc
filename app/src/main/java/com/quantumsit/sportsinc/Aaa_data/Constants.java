package com.quantumsit.sportsinc.Aaa_data;

/**
 * Created by Mona on 04-Jan-18.
 */

public interface Constants {

    String server = "http://192.168.1.3/sport_inc/api/";

    String selectData = server + "selectdata";
    String updateData = server + "updatedata";
    String insertData = server + "insertdata";
    String classesData = server + "class_Details";
    String admin_cuurentClasses = server + "current_classes";
    String deleteData = server + "deletedata";
    String joinData = server + "joindata";
    String sendSMS = server + "sendSMS";
    String notification = server + "pushNotificationAndroid";


    //SQLite DB
    String TABLE_AcademyInfo = "info_academy";
}
