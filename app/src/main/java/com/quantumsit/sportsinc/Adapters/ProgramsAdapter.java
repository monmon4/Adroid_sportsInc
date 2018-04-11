package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Entities.ProgramEntity;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProgramsAdapter  extends ArrayAdapter<ProgramEntity> {
    Context context ;
    List<ProgramEntity> myPrograms;


    public ProgramsAdapter(@NonNull Context context, int resource, List<ProgramEntity> myPrograms) {
        super(context, resource);
        this.context = context;
        this.myPrograms = myPrograms;
    }

    @Override
    public int getCount() {
        return myPrograms.size();
    }

    @Nullable
    @Override
    public ProgramEntity getItem(int position) {
        return myPrograms.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_programs, null);
        }
        ProgramEntity myProgram = getItem(position);

        TextView ProgramName = view.findViewById(R.id.program_name);
        Typeface face;

        face = Typeface.createFromAsset(context.getAssets(), "Sports_FOX_Sports_UScore.otf");

        ProgramName.setTypeface(face);
        String name = myProgram.getName();
        ProgramName.setText(name);

        return  view;
    }
}
