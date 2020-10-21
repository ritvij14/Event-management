package com.inner_wheel.event_management.adapters;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.SelectedTransactionResponse;
import com.inner_wheel.event_management.api.models.TransactionItem;
import com.inner_wheel.event_management.utils.SharedPrefs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {

    List<TransactionItem> list;
    Context context;
    SharedPrefs sharedPrefs;

    public TransactionHistoryAdapter(List<TransactionItem> list, Context context) {
        this.list = list;
        this.context = context;
        sharedPrefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public TransactionHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryAdapter.ViewHolder holder, int position) {
        TransactionItem item = list.get(position);
        if (item.getStatus().equals("SUCCESSFUL")) {
            holder.transactionCard.setCardBackgroundColor(context.getResources().getColor(R.color.green));
        } else if (item.getStatus().equals("PENDING")) {
            holder.transactionCard.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else if (item.getStatus().equals("FAILED") || item.getStatus().equals("FAILURE")) {
            holder.transactionCard.setCardBackgroundColor(context.getResources().getColor(R.color.red));
        }
        String amount = "Rs " + item.getAmount() + "/-";
        // formatting date
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        Date date = null;
        try {
            date = dateTimeFormat.parse(item.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String paymentDate = dateFormat.format(Objects.requireNonNull(date));

        holder.transactionCardAmount.setText(amount);
        holder.transactionCardStatus.setText(item.getStatus());
        holder.transactionCardDate.setText(paymentDate);

        // pulling data for each transaction
        fetchSelectedTransactionData(holder, item);

        holder.itemView.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(holder.transactionCard);
            if (holder.transactionCardDetailsHeading.getVisibility() == View.VISIBLE) {
                holder.transactionCardDetailsHeading.setVisibility(View.GONE);
                holder.transactionCardIdLayout.setVisibility(View.GONE);
                holder.transactionCardCompLayout.setVisibility(View.GONE);
                holder.transactionCardParticipantLayout.setVisibility(View.GONE);
                holder.transactionCardAgeLayout.setVisibility(View.GONE);
                notifyItemChanged(position);
            } else {
                holder.transactionCardDetailsHeading.setVisibility(View.VISIBLE);
                holder.transactionCardIdLayout.setVisibility(View.VISIBLE);
                holder.transactionCardCompLayout.setVisibility(View.VISIBLE);
                holder.transactionCardParticipantLayout.setVisibility(View.VISIBLE);
                holder.transactionCardAgeLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void fetchSelectedTransactionData(ViewHolder holder, TransactionItem item) {
        Call<SelectedTransactionResponse> call = RetrofitClient.getClient()
                .getSelectedTransaction(sharedPrefs.getToken(), item.getTransactionID());
        call.enqueue(new Callback<SelectedTransactionResponse>() {
            @Override
            public void onResponse(Call<SelectedTransactionResponse> call, Response<SelectedTransactionResponse> response) {
                SelectedTransactionResponse res = response.body();
                if (res != null) {
                    if (res.isSuccess()) {
                        String age = res.getDetails().getParticipant().getAge() + " yrs.";
                        holder.transactionCardID.setText(res.getDetails().getTransactionID());
                        holder.transactionCardCompetition.setText(res.getDetails().getCompetition().getTitle());
                        holder.transactionCardParticipant.setText(res.getDetails().getParticipant().getName());
                        holder.transactionCardAge.setText(age);
                    }
                }
            }

            @Override
            public void onFailure(Call<SelectedTransactionResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView transactionCard;
        MaterialTextView transactionCardAmount, transactionCardStatus, transactionCardDate, transactionCardDetailsHeading,
                transactionCardID, transactionCardCompetition, transactionCardParticipant, transactionCardAge;
        LinearLayout transactionCardIdLayout, transactionCardCompLayout, transactionCardParticipantLayout, transactionCardAgeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionCard = itemView.findViewById(R.id.transaction_card);
            transactionCardAmount = itemView.findViewById(R.id.transaction_card_amount);
            transactionCardStatus = itemView.findViewById(R.id.transaction_card_status);
            transactionCardDate = itemView.findViewById(R.id.transaction_card_date);
            transactionCardDetailsHeading = itemView.findViewById(R.id.transaction_card_details_heading);
            transactionCardID = itemView.findViewById(R.id.transaction_card_id_text);
            transactionCardCompetition = itemView.findViewById(R.id.transaction_card_competition);
            transactionCardParticipant = itemView.findViewById(R.id.transaction_card_participant);
            transactionCardAge = itemView.findViewById(R.id.transaction_card_participant_age);
            transactionCardIdLayout = itemView.findViewById(R.id.transaction_card_id_layout);
            transactionCardCompLayout = itemView.findViewById(R.id.transaction_card_competition_layout);
            transactionCardParticipantLayout = itemView.findViewById(R.id.transaction_card_participant_layout);
            transactionCardAgeLayout = itemView.findViewById(R.id.transaction_card_age_layout);
        }
    }
}
