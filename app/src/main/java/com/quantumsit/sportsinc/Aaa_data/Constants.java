package com.quantumsit.sportsinc.Aaa_data;

/**
 * Created by Mona on 04-Jan-18.
 */

public interface Constants {

    String server = "http://192.168.1.14:8080/sport_inc/api/";

    String selectData = server + "selectdata";
    String updateData = server + "updatedata";
    String insertData = server + "insertdata";
    String classesData = server + "class_Details";
    String admin_currentClasses = server + "current_classes";
    String coach_running_class = server + "running_classes";
    String coach_courses = server + "coach_coursesName";
    String coach_classes = server + "coach_classesName";
    String finished_groups = server + "finished_groups";
    String finished_classes = server + "finished_classes";
    String finishedClasses = server + "class_attend";
    String ClassesTrainee = server + "trainee_attendance";
    String person_attend = server + "persons_Attendance";
    String deleteData = server + "deletedata";
    String joinData = server + "joindata";
    String sendSMS = server + "sendSMS";
    String notification = server + "selectNotification";

    //Trainee
    String traineeCoursesData = server + "trainee_finished_courses";
    String traineeClassScores = server + "classes_scores";



    //SQLite DB
    String TABLE_AcademyInfo = "info_academy";
}
