package com.ecommerce.order;

import com.ecommerce.model.Order;

public interface IOrderProcessor {
    Order processOrder(Order order);
    double calculateFinalAmount(Order order);
    int getProcessingPriority();
}