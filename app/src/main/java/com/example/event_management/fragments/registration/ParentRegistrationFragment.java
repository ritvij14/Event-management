package com.example.event_management.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.event_management.R;
import com.example.event_management.databinding.FragmentParentRegistrationBinding;

import java.util.Objects;

public class ParentRegistrationFragment extends Fragment {

    FragmentParentRegistrationBinding registrationBinding;
    ParentLoginFragment parentLoginFragment;
    public ParentRegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registrationBinding = FragmentParentRegistrationBinding
            .inflate(inflater, container, false);
        parentLoginFragment = new ParentLoginFragment();

        registrationBinding.registerToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity())
                    .getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_frame_layout, parentLoginFragment)
                    .commit();
            }
        });
        return registrationBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registrationBinding = null;
    }
}