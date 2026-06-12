package com.ecommerce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final String orderId;
    private final String customerId;
    private final List<OrderItem> items;
    private final double totalAmount;
    private final OrderType orderType;
    private OrderStatus status;
    private final LocalDateTime createdAt;

    public Order(String orderId, String customerId, List<OrderItem> items, OrderType orderType) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.totalAmount = calculateTotal();
        this.orderType = orderType;
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    private double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total = total + (item.getPrice() * item.getQuantity());
        }
        return total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', totalAmount=" + totalAmount +
                ", orderType=" + orderType + ", status=" + status + '}';
    }
}