package com.quantumsit.sportsinc.Aaa_data;

/**
 * Created by Bassam on 1/24/2018.
 */

public class Trainees_info {
    private int ID;
    private int trainee_id;
    private String trainee_name;
    private int class_id;
    private int trainee_attend;
    private int trainee_score;
    private String trainee_note;

    public Trainees_info(int ID ,int trainee_id, String trainee_name, int class_id, int trainee_attend, int trainee_score, String trainee_note) {
        this.ID = ID;
        this.trainee_id = trainee_id;
        this.trainee_name = trainee_name;
        this.class_id = class_id;
        this.trainee_attend = trainee_attend;
        this.trainee_score = trainee_score;
        this.trainee_note = trainee_note;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTrainee_id() {
        return trainee_id;
    }

    public void setTrainee_id(int trainee_id) {
        this.trainee_id = trainee_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getTrainee_attend() {
        return trainee_attend;
    }

    public void setTrainee_attend(int trainee_attend) {
        this.trainee_attend = trainee_attend;
    }

    public int getTrainee_score() {
        return trainee_score;
    }

    public void setTrainee_score(int trainee_score) {
        this.trainee_score = trainee_score;
    }

    public String getTrainee_name() {
        return trainee_name;
    }

    public void setTrainee_name(String trainee_name) {
        this.trainee_name = trainee_name;
    }

    public String getTrainee_note() {
        return trainee_note;
    }

    public void setTrainee_note(String trainee_note) {
        this.trainee_note = trainee_note;
    }
}