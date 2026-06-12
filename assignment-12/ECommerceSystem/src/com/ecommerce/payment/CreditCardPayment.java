package com.ecommerce.payment;

import com.ecommerce.model.PaymentDetails;
import com.ecommerce.model.PaymentResult;
import java.util.UUID;

public class CreditCardPayment implements IPaymentMethod {

    @Override
    public PaymentResult processPayment(double amount, PaymentDetails details) {
        if (!validatePaymentDetails(details)) {
            return new PaymentResult(false, null, "Invalid credit card details");
        }

        String transactionId = "CC-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Processing credit card payment of $" + amount);

        return new PaymentResult(true, transactionId, "Payment successful");
    }

    @Override
    public boolean validatePaymentDetails(PaymentDetails details) {
        String cardNumber = details.getDetail("cardNumber");
        String cvv = details.getDetail("cvv");
        return cardNumber != null && cardNumber.length() == 16 &&
                cvv != null && cvv.length() == 3;
    }

    @Override
    public String getPaymentMethodType() {
        return "CREDIT_CARD";
    }
}