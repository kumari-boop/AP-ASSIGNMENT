package com.ecommerce.order;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;

public class RegularOrderProcessor implements IOrderProcessor {

    @Override
    public Order processOrder(Order order) {
        System.out.println("Processing regular order: " + order.getOrderId());
        order.setStatus(OrderStatus.PROCESSING);
        return order;
    }

    @Override
    public double calculateFinalAmount(Order order) {
        return order.getTotalAmount();
    }

    @Override
    public int getProcessingPriority() {
        return 2;
    }
}