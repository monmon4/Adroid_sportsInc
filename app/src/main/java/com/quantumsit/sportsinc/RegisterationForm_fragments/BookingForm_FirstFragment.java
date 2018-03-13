package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.R;

public class BookingForm_FirstFragment extends Fragment {

    public static BookingForm_FirstFragment newInstance() {
        BookingForm_FirstFragment fragment = new BookingForm_FirstFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_booking_form__first, container, false);


         return root;
    }

}
