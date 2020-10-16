package com.example.event_management.fragments.competition;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.event_management.R;
import com.example.event_management.api.RetrofitClient;
import com.example.event_management.api.models.AgeGroup;
import com.example.event_management.api.models.SelectCompetition;
import com.example.event_management.databinding.FragmentCompetitionPaymentBinding;
import com.example.event_management.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionPaymentFragment extends Fragment {

    FragmentCompetitionPaymentBinding paymentBinding;
    String name, school, age;
    SharedPrefs sharedPrefs;
    String ageGroup = "Age Group: ";
    String theme = "Theme: ";

    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    public static final int GOOGLE_PAY_REQUEST_CODE = 123;

    public CompetitionPaymentFragment(String name, String school, String age) {
        this.name = name;
        this.school = school;
        this.age = age;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        paymentBinding = FragmentCompetitionPaymentBinding.inflate(inflater, container, false);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));
        paymentBinding.participantInfoName.setText(name);
        paymentBinding.participantInfoAge.setText(age);
        paymentBinding.participantInfoSchool.setText(school);
        paymentBinding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGooglePay();
            }
        });

        getAgeGroupInfo();
        return paymentBinding.getRoot();
    }

    private void getAgeGroupInfo() {
        String id = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString("ID");
        Call<SelectCompetition> call = RetrofitClient.getClient().getSelectedCompetition(sharedPrefs.getToken(), id);
        call.enqueue(new Callback<SelectCompetition>() {
            @Override
            public void onResponse(Call<SelectCompetition> call, Response<SelectCompetition> response) {
                SelectCompetition competition = response.body();
                Log.d("SELECTED COMPETITION", competition.getCompetition().getTitle());
                List<AgeGroup> ageGroups = competition.getCompetition().getAgeGroups();
                int childAge = Integer.parseInt(age.substring(0, age.indexOf('y')));

                for (AgeGroup group: ageGroups) {
                    if (Integer.parseInt(group.getEndAge()) > childAge) {
                        Log.d("PAYMENT_FRAGMENT", group.getName());
                        ageGroup += group.getStartAge() + " to " + group.getEndAge() + " years";
                        theme += group.getName();
                        paymentBinding.ageGroupInfo.setText(ageGroup);
                        paymentBinding.ageGroupThemeInfo.setText(theme);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SelectCompetition> call, Throwable t) {
                Log.d("SELECTED COMPETITION", "failed");
                Log.d("SELECTED COMPETITION", sharedPrefs.getToken());
            }
        });
    }

    private void openGooglePay(){
        Uri uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa","vaib2641@okaxis") // To be changed to the UPI provided
                .appendQueryParameter("tn","Registration Fee")
                .appendQueryParameter("tr","12345678") // After initiating transaction, your id goes here
                .appendQueryParameter("am","5.00") // Set the amount as the registration fee for the competition
                .appendQueryParameter("cu","INR")
//                .appendQueryParameter("url","https://google.com")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String message="";
        if(requestCode == GOOGLE_PAY_REQUEST_CODE) {
            try {
                String status = data.getStringExtra("Status"); // "SUCCESS" if transaction is successful, else "FAILURE"
                String transactionId = data.getStringExtra("txnId");
                if (status.equals("SUCCESS"))
                    message = "Payment successful";
                else
                    message = "Payment Failed";
                // TODO Make API call to update the transaction status to "SUCCESS" or "FAILURE"
            } catch (Exception e) {
                message = "Some error occurred, could not open Google Pay";
            }
        }
        if(!message.equals(""))
            Toast.makeText(paymentBinding.getRoot().getContext(), message,Toast.LENGTH_SHORT).show();
    }
}