package com.ecommerce.payment;

import com.ecommerce.model.PaymentDetails;
import com.ecommerce.model.PaymentResult;
import java.util.UUID;

public class WalletPayment implements IPaymentMethod {

    @Override
    public PaymentResult processPayment(double amount, PaymentDetails details) {
        String walletId = details.getDetail("walletId");

        if (!validatePaymentDetails(details)) {
            return new PaymentResult(false, null, "Invalid wallet details");
        }

        String transactionId = "WAL-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Processing wallet payment of $" + amount + " from wallet " + walletId);

        return new PaymentResult(true, transactionId, "Payment successful");
    }

    @Override
    public boolean validatePaymentDetails(PaymentDetails details) {
        String walletId = details.getDetail("walletId");
        return walletId != null && !walletId.isEmpty();
    }

    @Override
    public String getPaymentMethodType() {
        return "WALLET";
    }
}