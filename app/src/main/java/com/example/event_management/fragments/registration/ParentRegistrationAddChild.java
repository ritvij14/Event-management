package com.example.event_management.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.event_management.R;
import com.example.event_management.databinding.FragmentParentRegistrationAddChildBinding;

public class ParentRegistrationAddChild extends Fragment {

    FragmentParentRegistrationAddChildBinding childBinding;
    public ParentRegistrationAddChild() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        childBinding = FragmentParentRegistrationAddChildBinding
            .inflate(inflater, container, false);
        return childBinding.getRoot();
    }
}