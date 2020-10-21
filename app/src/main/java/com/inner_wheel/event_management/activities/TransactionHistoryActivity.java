package com.inner_wheel.event_management.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.TransitionManager;

import com.inner_wheel.event_management.adapters.TransactionHistoryAdapter;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.TransactionHistoryResponse;
import com.inner_wheel.event_management.api.models.TransactionItem;
import com.inner_wheel.event_management.databinding.ActivityTransactionHistoryBinding;
import com.inner_wheel.event_management.utils.SharedPrefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionHistoryActivity extends AppCompatActivity {

    ActivityTransactionHistoryBinding historyBinding;
    SharedPrefs sharedPrefs;
    TransactionHistoryAdapter historyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyBinding = ActivityTransactionHistoryBinding.inflate(getLayoutInflater());
        setContentView(historyBinding.getRoot());
        sharedPrefs = new SharedPrefs(this);
        TransitionManager.beginDelayedTransition(historyBinding.transactionHistoryRv);

        fetchTransactions();
    }

    private void fetchTransactions() {
        Call<TransactionHistoryResponse> call = RetrofitClient.getClient().getTransactionHistory(sharedPrefs.getToken());
        call.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {
                TransactionHistoryResponse res = response.body();
                if (res != null) {
                    if (res.isSuccess()) {
                        List<TransactionItem> transactionItemList= res.getTransactionItems();
                        createHistoryRecyclerView(transactionItemList);
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {

            }
        });
    }

    private void createHistoryRecyclerView(List<TransactionItem> transactionItemList) {
        // setting up recyclerview
        historyBinding.transactionHistoryRv.setHasFixedSize(true);
        historyBinding.transactionHistoryRv.
                setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        historyAdapter = new TransactionHistoryAdapter(transactionItemList, this);
        historyBinding.transactionHistoryRv.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
    }
}