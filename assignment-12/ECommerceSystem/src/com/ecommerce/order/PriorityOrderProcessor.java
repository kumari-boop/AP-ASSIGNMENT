package com.ecommerce.order;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;

public class PriorityOrderProcessor implements IOrderProcessor {
    private static final double PREMIUM = 0.10;

    @Override
    public Order processOrder(Order order) {
        System.out.println("Processing priority order: " + order.getOrderId());
        order.setStatus(OrderStatus.PROCESSING);
        return order;
    }

    @Override
    public double calculateFinalAmount(Order order) {
        return order.getTotalAmount() * (1 + PREMIUM);
    }

    @Override
    public int getProcessingPriority() {
        return 1;
    }
}