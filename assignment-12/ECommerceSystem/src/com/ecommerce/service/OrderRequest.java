package com.ecommerce.service;

import com.ecommerce.model.OrderItem;
import com.ecommerce.model.OrderType;
import java.util.List;

public class OrderRequest {
    private final String customerId;
    private final List<OrderItem> items;
    private final OrderType orderType;
    private final String customerEmail;
    private final String customerPhone;
    private final String deviceToken;

    public OrderRequest(String customerId, List<OrderItem> items,
                        OrderType orderType, String customerEmail,
                        String customerPhone, String deviceToken) {
        this.customerId = customerId;
        this.items = items;
        this.orderType = orderType;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.deviceToken = deviceToken;
    }

    public String getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return items; }
    public OrderType getOrderType() { return orderType; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public String getDeviceToken() { return deviceToken; }
}