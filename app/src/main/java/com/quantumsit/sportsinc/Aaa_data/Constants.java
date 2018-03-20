package com.quantumsit.sportsinc.Aaa_data;

/**
 * Created by Mona on 04-Jan-18.
 */

public interface Constants {

   // String server = "http://192.168.1.7:8080/sport_inc/api/";

    String server = "http://173.212.198.28:8010/sports_inc/api/";
    String upload_host = "http://173.212.198.28:8010/sports_inc/assets/uploads/";
    String UPLOAD_URL = "http://173.212.198.28:8010/sports_inc/academy/upload_Image";


    String socialLogin = server + "social_login";

    String selectData = server + "selectdata";
    String updateData = server + "updatedata";
    String insertData = server + "insertdata";
    String join = server + "joindata";
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
    String traineeSwitchGroup = server + "switch_group";

    String profile = "profile_img";
    String profile_host = upload_host + profile + "/";
    String certification = "certifications";
    String certification_host = upload_host + certification + "/";
    String others = "other_img";
    String others_host = upload_host + others + "/";

    //SQLite DB
    String TABLE_AcademyInfo = "info_academy";
}
