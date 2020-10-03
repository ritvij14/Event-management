package com.example.event_management.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.event_management.R;
import com.example.event_management.databinding.FragmentParentRegistrationPersonalInfoBinding;

import java.util.Objects;

public class ParentRegistrationPersonalInfoFragment extends Fragment {

    FragmentParentRegistrationPersonalInfoBinding infoBinding;
    ParentLoginFragment parentLoginFragment;
    ParentRegistrationCredentialsFragment credentialsFragment;
    public ParentRegistrationPersonalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        infoBinding = FragmentParentRegistrationPersonalInfoBinding
            .inflate(inflater, container, false);
        parentLoginFragment = new ParentLoginFragment();
        credentialsFragment = new ParentRegistrationCredentialsFragment();

        infoBinding.registerOneNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.requireNonNull(infoBinding.parentNameField.getText())
                    .toString().isEmpty()) {
                    Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager().beginTransaction()
                        .replace(R.id.registration_frame_layout, credentialsFragment)
                        .addToBackStack(null)
                        .commit();
                } else {
                    Toast.makeText(getContext(), "Please enter details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        infoBinding.registerToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity())
                    .getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_frame_layout, parentLoginFragment)
                    .commit();
            }
        });
        return infoBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        infoBinding = null;
    }
}