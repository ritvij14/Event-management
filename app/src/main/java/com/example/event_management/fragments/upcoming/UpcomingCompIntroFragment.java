package com.example.event_management.fragments.upcoming;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.event_management.R;
import com.example.event_management.databinding.FragmentUpcomingCompIntroBinding;

public class UpcomingCompIntroFragment extends Fragment {

    FragmentUpcomingCompIntroBinding introBinding;
    public UpcomingCompIntroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        introBinding = FragmentUpcomingCompIntroBinding
            .inflate(inflater, container, false);

        return introBinding.getRoot();
    }
}