package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.quantumsit.sportsinc.Entities.CurrentClassesEntity;
import com.quantumsit.sportsinc.Entities.classesEntity;

import java.util.ArrayList;

/**
 * Created by Bassam on 15/3/2018.
 */

public class CurrentClassesAdapter  extends ArrayAdapter<classesEntity>{
    Context context;
    ArrayList<classesEntity> classesList;
    public CurrentClassesAdapter(@NonNull Context context, int resource , ArrayList<classesEntity> classesList) {
        super(context, resource);
        this.context = context;
        this.classesList = classesList;
    }


}
