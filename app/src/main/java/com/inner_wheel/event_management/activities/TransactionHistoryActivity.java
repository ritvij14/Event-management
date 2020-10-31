package com.inner_wheel.event_management.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.widget.Toast;

import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.adapters.TransactionHistoryAdapter;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.TransactionHistoryResponse;
import com.inner_wheel.event_management.api.models.TransactionItem;
import com.inner_wheel.event_management.databinding.ActivityTransactionHistoryBinding;
import com.inner_wheel.event_management.utils.SharedPrefs;

import net.steamcrafted.loadtoast.LoadToast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionHistoryActivity extends AppCompatActivity {

    ActivityTransactionHistoryBinding historyBinding;
    SharedPrefs sharedPrefs;
    TransactionHistoryAdapter historyAdapter;
    LoadToast lt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyBinding = ActivityTransactionHistoryBinding.inflate(getLayoutInflater());
        setContentView(historyBinding.getRoot());
        sharedPrefs = new SharedPrefs(this);
        lt = new LoadToast(this);
        lt.setText("Loading...");
        lt.setTranslationY(100).setBorderColor(getResources().getColor(R.color.color_accent));
        lt.show();
        TransitionManager.beginDelayedTransition(historyBinding.transactionHistoryRv);

        fetchTransactions();

        historyBinding.backButton.setOnClickListener(v -> onBackPressed());
    }

    private void fetchTransactions() {
        Call<TransactionHistoryResponse> call = RetrofitClient.getClient().getTransactionHistory(sharedPrefs.getToken());
        call.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(@NotNull Call<TransactionHistoryResponse> call, @NotNull Response<TransactionHistoryResponse> response) {
                TransactionHistoryResponse res = response.body();
                if (res != null) {
                    if (res.isSuccess()) {
                        lt.success();
                        lt.hide();
                        List<TransactionItem> transactionItemList= res.getTransactionItems();
                        createHistoryRecyclerView(transactionItemList);
                    }
                } else {
                    lt.error();
                    lt.hide();
                    Toast.makeText(TransactionHistoryActivity.this, "No past transactions found at the moment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<TransactionHistoryResponse> call, @NotNull Throwable t) {
                lt.error();
                lt.hide();
                Toast.makeText(TransactionHistoryActivity.this, "Error!", Toast.LENGTH_SHORT).show();
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