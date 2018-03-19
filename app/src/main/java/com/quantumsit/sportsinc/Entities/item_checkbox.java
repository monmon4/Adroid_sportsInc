package com.quantumsit.sportsinc.Entities;

import com.quantumsit.sportsinc.Aaa_data.Rule_info;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;

/**
 * Created by mona_ on 12/30/2017.
 */

public class item_checkbox {

    String note = "";
    String name;
    boolean selected;

    Trainees_info trainee ;

    public item_checkbox(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public item_checkbox(Trainees_info trainee){
        this.trainee = trainee;
        this.name = trainee.getTrainee_name();
        this.selected = trainee.getSelected();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
       if (trainee != null)
            trainee.setSelected(selected);

        this.selected = selected;
    }

    public String getName() {return name;}

    public boolean getSelected() {
        return selected;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        if (trainee != null)
            trainee.setTrainee_note(note);

        this.note = note;
    }

    public Trainees_info getTrainee() {
        return trainee;
    }
}
