package com.inner_wheel.event_management.fragments.competition;

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

import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.AgeGroup;
import com.inner_wheel.event_management.api.models.RegistrationResponse;
import com.inner_wheel.event_management.api.models.SelectCompetition;
import com.inner_wheel.event_management.api.models.TransactionInitiate;
import com.inner_wheel.event_management.databinding.FragmentCompetitionPaymentBinding;
import com.inner_wheel.event_management.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CompetitionPaymentFragment extends Fragment {

    FragmentCompetitionPaymentBinding paymentBinding;
    String name, school, age, compID, participantID, feeAmount, id, ageGroupID;
    SharedPrefs sharedPrefs;
    String ageGroup = "Age Group: ";
    String theme = "Theme: ";

    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    public static final int GOOGLE_PAY_REQUEST_CODE = 123;

    public CompetitionPaymentFragment(String name, String school, String age, String participantID) {
        this.name = name;
        this.school = school;
        this.age = age;
        this.participantID = participantID;
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

        getAgeGroupInfo();
        paymentBinding.pay.setOnClickListener(view -> initiateTransaction());

        paymentBinding.backButton.setOnClickListener(v -> Objects.requireNonNull(getActivity())
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_competitions_frame, new UpcomingCompIntroFragment())
                .commit());

        return paymentBinding.getRoot();
    }

    private void initiateTransaction() {
        Call<TransactionInitiate> call = RetrofitClient.getClient()
                .startTransaction(sharedPrefs.getToken(), compID, participantID, feeAmount);
        call.enqueue(new Callback<TransactionInitiate>() {
            @Override
            public void onResponse(@NotNull Call<TransactionInitiate> call, @NotNull Response<TransactionInitiate> response) {
                TransactionInitiate initiate = response.body();
                if (initiate != null && initiate.isSuccess()) {
                    id = initiate.getTransaction().getId();
                    openGooglePay();
                } else {
                    Toast.makeText(getContext(), "Unable to initiate transaction", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<TransactionInitiate> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "Unable to initiate transaction", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAgeGroupInfo() {
        String id = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString("ID");
        Call<SelectCompetition> call = RetrofitClient.getClient().getSelectedCompetition(sharedPrefs.getToken(), id);
        call.enqueue(new Callback<SelectCompetition>() {
            @Override
            public void onResponse(@NotNull Call<SelectCompetition> call, @NotNull Response<SelectCompetition> response) {
                SelectCompetition competition = response.body();
                compID = Objects.requireNonNull(competition).getCompetition().getCompId();
                feeAmount = competition.getCompetition().getFees();

                List<AgeGroup> ageGroups = competition.getCompetition().getAgeGroups();
                int childAge = Integer.parseInt(age.substring(0, age.indexOf('y')));

                if (ageGroups != null) {
                    for (AgeGroup group: ageGroups) {
                        if (Integer.parseInt(group.getEndAge()) > childAge) {
                            Log.d("PAYMENT_FRAGMENT", group.getName());
                            ageGroup += group.getStartAge() + " to " + group.getEndAge() + " years";
                            theme += group.getName();
                            ageGroupID = group.getGrpId();
                            paymentBinding.ageGroupInfo.setText(ageGroup);
                            paymentBinding.ageGroupThemeInfo.setText(theme);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<SelectCompetition> call, @NotNull Throwable t) {
                Log.d("SELECTED COMPETITION", "failed");
                Log.d("SELECTED COMPETITION", sharedPrefs.getToken());
            }
        });
    }

    private void openGooglePay(){
        Uri uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa","")
                .appendQueryParameter("pn","")
                .appendQueryParameter("tn","Test transaction")
//                        .appendQueryParameter("mc","1234")
                .appendQueryParameter("tr","")
                .appendQueryParameter("am","1.00")
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
                String status = null; // "SUCCESS" if transaction is successful, else "FAILURE"
                String transactionId = "";
                if (data != null) {
                    status = data.getStringExtra("Status");
                    transactionId = data.getStringExtra("txnId");
                }
                assert status != null;
                if (status.equals("SUCCESS")) {
                    message = "Payment successful";
                    updateTransaction(id, transactionId, "SUCCESSFUL");
                } else{
                    message = "Payment Failed";
                }
            } catch (Exception e) {
                message = "Some error occurred, could not open Google Pay";
            }
        }
        if(!message.equals(""))
            Toast.makeText(paymentBinding.getRoot().getContext(), message,Toast.LENGTH_SHORT).show();
    }

    private void updateTransaction(String transID, String gpayID, String status) {

        Call<TransactionInitiate> call = RetrofitClient.getClient().updateTransaction(sharedPrefs.getToken(), transID, gpayID, status);
        call.enqueue(new Callback<TransactionInitiate>() {
            @Override
            public void onResponse(@NotNull Call<TransactionInitiate> call, @NotNull Response<TransactionInitiate> response) {
                Log.d("TRANSACTION_UPDATE", "Update successful");
                registerParticipant();
            }

            @Override
            public void onFailure(@NotNull Call<TransactionInitiate> call, @NotNull Throwable t) {

            }
        });
    }

    private void registerParticipant() {
        Call<RegistrationResponse> call = RetrofitClient.getClient().registerParticipant(
                sharedPrefs.getToken(),
                ageGroupID,
                participantID,
                id,
                compID,
                sharedPrefs.getFcmToken()
        );
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                RegistrationResponse res = response.body();
                if (res != null) {
                    Toast.makeText(getContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity())
                            .getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_competitions_frame, new UpcomingCompIntroFragment())
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Error registering participant", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error registering participant", Toast.LENGTH_SHORT).show();
            }
        });
    }
}