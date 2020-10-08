package com.example.event_management.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.event_management.R;
import com.example.event_management.databinding.FragmentParentRegistrationPersonalInfoBinding;
import com.example.event_management.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ParentRegistrationPersonalInfoFragment extends Fragment {

    FragmentParentRegistrationPersonalInfoBinding infoBinding;
    ParentLoginFragment parentLoginFragment;
    ParentRegistrationCredentialsFragment credentialsFragment;
    SharedPrefs sharedPrefs;
    public ParentRegistrationPersonalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        infoBinding = FragmentParentRegistrationPersonalInfoBinding
            .inflate(inflater, container, false);
        parentLoginFragment = new ParentLoginFragment();
        credentialsFragment = new ParentRegistrationCredentialsFragment();
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));

        infoBinding.registerOneNext.setOnClickListener(view -> {
            String name = Objects.requireNonNull(infoBinding.parentNameField.getText())
                    .toString();
            String contactNumber = Objects.requireNonNull(infoBinding.phoneField.getText())
                    .toString();
            String address = Objects.requireNonNull(infoBinding.addressField.getText())
                    .toString();
            String city = Objects.requireNonNull(infoBinding.cityField.getText())
                    .toString();
            String state = Objects.requireNonNull(infoBinding.stateField.getText())
                    .toString();

            if (checkFields(name, contactNumber, address, city, state)) {

                sharedPrefs.setName(name);
                sharedPrefs.setContactNumber(contactNumber);
                sharedPrefs.setAddress(address);
                sharedPrefs.setCity(city);
                sharedPrefs.setState(state);
                Objects.requireNonNull(getActivity())
                    .getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_frame_layout, credentialsFragment)
                    .addToBackStack(null)
                    .commit();
            } else {
                Toast.makeText(getContext(), "Please enter all details.", Toast.LENGTH_SHORT).show();
            }
        });

        infoBinding.registerToLogin.setOnClickListener(view ->
            Objects.requireNonNull(getActivity())
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.registration_frame_layout, parentLoginFragment)
                .commit());

        return infoBinding.getRoot();
    }

    private boolean checkFields(String name, String contactNumber, String address, String city, String state) {

        if (name.matches("")) {
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (contactNumber.matches("")) {
            Toast.makeText(getContext(), "Please enter your contact number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (address.matches("")) {
            Toast.makeText(getContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (city.matches("")) {
            Toast.makeText(getContext(), "Please enter your city", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (state.matches("")) {
            Toast.makeText(getContext(), "Please enter your state", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        infoBinding = null;
    }
}