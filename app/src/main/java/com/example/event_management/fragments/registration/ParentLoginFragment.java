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

import java.util.Objects;

public class ParentLoginFragment extends Fragment {

    FragmentParentLoginBinding parentLoginBinding;
    ParentRegistrationFragment parentRegistrationFragment;
    public ParentLoginFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentLoginBinding = FragmentParentLoginBinding
            .inflate(inflater, container, false);
        parentRegistrationFragment = new ParentRegistrationFragment();

        parentLoginBinding.loginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity())
                    .getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_frame_layout, parentRegistrationFragment)
                    .commit();
            }
        });
        return parentLoginBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        parentLoginBinding = null;
    }
}