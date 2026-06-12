package com.ecommerce.model;

import java.time.LocalDateTime;

public class PaymentResult {
    private final boolean success;
    private final String transactionId;
    private final String message;
    private final LocalDateTime timestamp;

    public PaymentResult(boolean success, String transactionId, String message) {
        this.success = success;
        this.transactionId = transactionId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}