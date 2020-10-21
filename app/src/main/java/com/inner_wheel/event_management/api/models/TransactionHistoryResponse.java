package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionHistoryResponse {
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("transactions")
    @Expose
    private List<TransactionItem> transactionItems;

    public boolean isSuccess() { return success; }

    public List<TransactionItem> getTransactionItems() { return transactionItems; }

    public void setSuccess(boolean success) { this.success = success; }

    public void setTransactionItems(List<TransactionItem> transactionItems) { this.transactionItems = transactionItems; }
}
