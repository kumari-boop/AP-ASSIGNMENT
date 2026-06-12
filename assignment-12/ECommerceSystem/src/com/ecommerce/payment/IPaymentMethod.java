package com.ecommerce.payment;

import com.ecommerce.model.PaymentDetails;
import com.ecommerce.model.PaymentResult;

public interface IPaymentMethod {
    PaymentResult processPayment(double amount, PaymentDetails details);
    boolean validatePaymentDetails(PaymentDetails details);
    String getPaymentMethodType();
}