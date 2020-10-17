package com.inner_wheel.event_management.fragments.registration;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.activities.MainActivity;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.ParentLogin;
import com.inner_wheel.event_management.databinding.FragmentParentLoginBinding;
import com.inner_wheel.event_management.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentLoginFragment extends Fragment {

    FragmentParentLoginBinding parentLoginBinding;
    ParentRegistrationPersonalInfoFragment parentRegistrationPersonalInfoFragment;
    private SharedPrefs sharedPrefs;
    public ParentLoginFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentLoginBinding = FragmentParentLoginBinding
            .inflate(inflater, container, false);
        parentRegistrationPersonalInfoFragment = new ParentRegistrationPersonalInfoFragment();
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));

        parentLoginBinding.loginToRegister.setOnClickListener(view -> Objects.requireNonNull(getActivity())
            .getSupportFragmentManager().beginTransaction()
            .replace(R.id.registration_frame_layout, parentRegistrationPersonalInfoFragment)
            .commit());

        parentLoginBinding.login.setOnClickListener(view -> {
            String email = Objects.requireNonNull(parentLoginBinding.emailField.getText())
                    .toString();
            String password = Objects.requireNonNull(parentLoginBinding.passwordField.getText())
                    .toString();

            if (checkFields(email, password)) {
                Call<ParentLogin> call = RetrofitClient.getClient().getParentLoginToken(email, password);
                call.enqueue(new Callback<ParentLogin>() {
                    @Override
                    public void onResponse(@NotNull Call<ParentLogin> call, @NotNull Response<ParentLogin> response) {
                        ParentLogin parentLogin = response.body();
                        if (parentLogin != null) {
                            if (parentLogin.getSuccess()) {
                                sharedPrefs.setToken(parentLogin.getAuthToken());
                                sharedPrefs.setUserAuthStatus("SIGNED_IN");
                                Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), MainActivity.class));
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        } else {
                            // show error message
                            Toast.makeText(getContext(), "Login unsuccessful!", Toast.LENGTH_SHORT).show();
                            sharedPrefs.setUserAuthStatus(null);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ParentLogin> call, @NotNull Throwable t) {
                        // show error message
                        Toast.makeText(getContext(), "Login unsuccessful! Please check your connection.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return parentLoginBinding.getRoot();
    }

    private boolean checkFields(String email, String password) {
        if (email.matches("") && password.matches("")) {
            Toast.makeText(getContext(), "Please enter your credentials", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.matches("")) {
            Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.matches("")) {
            Toast.makeText(getContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        parentLoginBinding = null;
    }
}