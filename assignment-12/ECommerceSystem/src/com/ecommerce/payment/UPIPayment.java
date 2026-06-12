package com.ecommerce.payment;

import com.ecommerce.model.PaymentDetails;
import com.ecommerce.model.PaymentResult;
import java.util.UUID;

public class UPIPayment implements IPaymentMethod {

    @Override
    public PaymentResult processPayment(double amount, PaymentDetails details) {
        String upiId = details.getDetail("upiId");

        if (!validatePaymentDetails(details)) {
            return new PaymentResult(false, null, "Invalid UPI details");
        }

        String transactionId = "UPI-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Processing UPI payment of $" + amount + " from " + upiId);

        return new PaymentResult(true, transactionId, "Payment successful");
    }

    @Override
    public boolean validatePaymentDetails(PaymentDetails details) {
        String upiId = details.getDetail("upiId");
        return upiId != null && upiId.contains("@");
    }

    @Override
    public String getPaymentMethodType() {
        return "UPI";
    }
}