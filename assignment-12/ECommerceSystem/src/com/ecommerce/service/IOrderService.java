package com.ecommerce.service;

import com.ecommerce.model.PaymentDetails;
import com.ecommerce.payment.IPaymentMethod;

public interface IOrderService {
    OrderResult createOrder(OrderRequest request);
    OrderResult processOrderPayment(String orderId, IPaymentMethod paymentMethod,
                                    PaymentDetails paymentDetails);
}