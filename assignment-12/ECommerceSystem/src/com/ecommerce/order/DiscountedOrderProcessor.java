package com.ecommerce.order;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;

public class DiscountedOrderProcessor implements IOrderProcessor {
    private static final double DISCOUNT = 0.10;

    @Override
    public Order processOrder(Order order) {
        System.out.println("Processing discounted order: " + order.getOrderId());
        order.setStatus(OrderStatus.PROCESSING);
        return order;
    }

    @Override
    public double calculateFinalAmount(Order order) {
        return order.getTotalAmount() * (1 - DISCOUNT);
    }

    @Override
    public int getProcessingPriority() {
        return 2;
    }
}