package com.ecommerce.storage;

import com.ecommerce.model.Order;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseOrderRepository implements IOrderRepository {
    private final Map<String, Order> database;

    public DatabaseOrderRepository() {
        this.database = new HashMap<>();
        System.out.println("Initialized Database Storage");
    }

    @Override
    public void save(Order order) {
        database.put(order.getOrderId(), order);
        System.out.println("Order saved to DATABASE: " + order.getOrderId());
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(database.get(orderId));
    }

    @Override
    public List<Order> findByCustomerId(String customerId) {
        return database.values().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Order order) {
        database.put(order.getOrderId(), order);
        System.out.println("Order updated in DATABASE: " + order.getOrderId());
    }

    @Override
    public void delete(String orderId) {
        database.remove(orderId);
        System.out.println("Order deleted from DATABASE: " + orderId);
    }
}