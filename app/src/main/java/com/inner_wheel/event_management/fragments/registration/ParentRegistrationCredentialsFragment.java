package com.inner_wheel.event_management.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.SignUpResponse;
import com.inner_wheel.event_management.databinding.FragmentParentRegistrationCredentialsBinding;
import com.inner_wheel.event_management.utils.SharedPrefs;

import net.steamcrafted.loadtoast.LoadToast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentRegistrationCredentialsFragment extends Fragment {

    FragmentParentRegistrationCredentialsBinding credentialsBinding;
    ParentLoginFragment loginFragment;
   SharedPrefs sharedPrefs;
    LoadToast lt;
    public ParentRegistrationCredentialsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        credentialsBinding = FragmentParentRegistrationCredentialsBinding
            .inflate(inflater, container, false);
        loginFragment = new ParentLoginFragment();
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));
        lt = new LoadToast(getContext());
        lt.setText("Loading...");
        lt.setTranslationY(100).setBorderColor(getResources().getColor(R.color.color_accent));

        credentialsBinding.registerTwoNext.setOnClickListener(view -> {
            lt.show();
            String parentEmail = Objects.requireNonNull(credentialsBinding
                .emailField.getText()).toString();
            String parentPassword = Objects.requireNonNull(credentialsBinding
                .passwordField.getText()).toString();
            String parentConfirmPassword = Objects.requireNonNull(credentialsBinding
                .confirmPasswordField.getText()).toString();

            if (checkCredentials(parentEmail, parentPassword, parentConfirmPassword)) {

                Call<SignUpResponse> call = RetrofitClient.getClient().signUpParent(sharedPrefs.getName(),
                        parentEmail,
                        sharedPrefs.getCity(),
                        sharedPrefs.getState(),
                        sharedPrefs.getAddress(),
                        sharedPrefs.getContactNumber(),
                        parentPassword);
                call.enqueue(new Callback<SignUpResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<SignUpResponse> call, @NotNull Response<SignUpResponse> response) {
                        SignUpResponse res =response.body();
                        if (res != null) {
                            if (res.isSuccess()) {
                                sharedPrefs.setEmail(parentEmail);
                                Toast.makeText(getContext(), "Sign up successful. Please login.", Toast.LENGTH_SHORT).show();
                                lt.success();
                                lt.hide();
                                Objects.requireNonNull(getActivity())
                                        .getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.registration_frame_layout, loginFragment)
                                        .addToBackStack(null)
                                        .commit();
                                sharedPrefs.setIsJustRegistered(true);
                            }
                        } else {
                            // print error message
                            lt.error();
                            lt.hide();
                            Toast.makeText(getContext(), "Sign up Unsuccessful!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<SignUpResponse> call, @NotNull Throwable t) {
                        // error
                        lt.error();
                        lt.hide();
                        Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                    }
                });
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

        if (password.length() < 8) {
            Toast.makeText(getContext(), "Password should be 8 characters or longer", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!(password.equals(confirmPassword))) {
            Toast.makeText(getContext(), "Please enter the same password.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}