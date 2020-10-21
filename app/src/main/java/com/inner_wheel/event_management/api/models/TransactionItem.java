package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionItem {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("transaction_id")
    @Expose
    private String transactionID;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("date")
    @Expose
    private String date;

    public String getId() { return id; }

    public String getTransactionID() { return transactionID; }

    public String getAmount() { return amount; }

    public String getStatus() { return status; }

    public String getDate() { return date; }

    public void setId(String id) { this.id = id; }

    public void setTransactionID(String transactionID) { this.transactionID = transactionID; }

    public void setAmount(String amount) { this.amount = amount; }

    public void setStatus(String status) { this.status = status; }

    public void setDate(String date) { this.date = date; }
}
