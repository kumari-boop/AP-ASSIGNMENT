package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.PaymentResult;

public class OrderResult {
    private final Order order;
    private final PaymentResult paymentResult;
    private final String message;
    private final boolean success;

    public OrderResult(Order order, PaymentResult paymentResult, String message, boolean success) {
        this.order = order;
        this.paymentResult = paymentResult;
        this.message = message;
        this.success = success;
    }

    public Order getOrder() { return order; }
    public PaymentResult getPaymentResult() { return paymentResult; }
    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }
}