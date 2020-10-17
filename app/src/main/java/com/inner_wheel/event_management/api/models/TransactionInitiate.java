package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionInitiate {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("transaction")
    @Expose
    private Transaction transaction;

    public boolean isSuccess() { return success; }

    public Transaction getTransaction() { return transaction; }

    public void setSuccess(boolean success) { this.success = success; }

    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public class Transaction {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("amount")
        @Expose
        private String amount;

        @SerializedName("status")
        @Expose
        private String status;

        public String getId() { return id; }

        public String getAmount() { return amount; }

        public String getStatus() { return status; }

        public void setId(String id) { this.id = id; }

        public void setAmount(String amount) { this.amount = amount; }

        public void setStatus(String status) { this.status = status; }
    }
}
