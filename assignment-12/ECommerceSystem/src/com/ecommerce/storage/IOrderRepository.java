package com.ecommerce.storage;

import com.ecommerce.model.Order;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository {
    void save(Order order);
    Optional<Order> findById(String orderId);
    List<Order> findByCustomerId(String customerId);
    void update(Order order);
    void delete(String orderId);
}