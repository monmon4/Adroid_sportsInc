package com.quantumsit.sportsinc.ADMINS;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.quantumsit.sportsinc.HomeActivity;
import com.quantumsit.sportsinc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Admin_CurrentClassNoteFragment extends Fragment {

    EditText notes_edit_text;
    FloatingActionButton done_button;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin__current_class_note,container,false);

        notes_edit_text = root.findViewById(R.id.notesEditText_admincurrentclassnotesfragment);
        done_button = root.findViewById(R.id.doneFloatingActionButton_admincurrentclassnotesfragment);

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String notes = notes_edit_text.getText().toString();

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return root;
    }

}
