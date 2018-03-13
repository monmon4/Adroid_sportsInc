package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.R;

public class BookingForm_SecondFragment extends Fragment {

    public static BookingForm_SecondFragment newInstance() {
        BookingForm_SecondFragment fragment = new BookingForm_SecondFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booking_form__second, container, false);


        return root;
    }
}
