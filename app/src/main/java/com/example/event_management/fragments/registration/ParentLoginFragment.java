package com.example.event_management.fragments.registration;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.event_management.R;
import com.example.event_management.databinding.FragmentParentLoginBinding;

public class ParentLoginFragment extends Fragment {

    FragmentParentLoginBinding parentLoginBinding;
    public ParentLoginFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentLoginBinding = FragmentParentLoginBinding.inflate(inflater, container, false);
        return parentLoginBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        parentLoginBinding = null;
    }
}