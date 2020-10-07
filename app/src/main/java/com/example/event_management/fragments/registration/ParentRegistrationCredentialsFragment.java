package com.example.event_management.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.event_management.R;
import com.example.event_management.databinding.FragmentParentRegistrationCredentialsBinding;

import java.util.Objects;

public class ParentRegistrationCredentialsFragment extends Fragment {

    FragmentParentRegistrationCredentialsBinding credentialsBinding;
    ParentRegistrationAddChild addChildFragment;
    public ParentRegistrationCredentialsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        credentialsBinding = FragmentParentRegistrationCredentialsBinding
            .inflate(inflater, container, false);
        addChildFragment = new ParentRegistrationAddChild();

        credentialsBinding.registerTwoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String parentEmail = Objects.requireNonNull(credentialsBinding
                    .emailField.getText()).toString();
                String parentPassword = Objects.requireNonNull(credentialsBinding
                    .passwordField.getText()).toString();
                String parentConfirmPassword = Objects.requireNonNull(credentialsBinding
                    .confirmPasswordField.getText()).toString();

                if (checkCredentials(parentEmail, parentPassword, parentConfirmPassword)) {
                    Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager().beginTransaction()
                        .replace(R.id.registration_frame_layout, addChildFragment)
                        .addToBackStack(null)
                        .commit();
                }
            }
        });
        return credentialsBinding.getRoot();
    }

    private boolean checkCredentials(String email, String password, String confirmPassword) {

        if (email.matches("") && password.matches("") && confirmPassword.matches("")) {
            Toast.makeText(getContext(), "Please enter all credentials", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.matches("")) {
            Toast.makeText(getContext(), "Please enter an email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.matches("")) {
            Toast.makeText(getContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (confirmPassword.matches("")) {
            Toast.makeText(getContext(), "Please confirm your password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!(password.equals(confirmPassword))) {
            Toast.makeText(getContext(), "Please enter the same password.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}