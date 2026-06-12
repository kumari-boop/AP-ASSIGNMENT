package com.ecommerce.model;

public enum OrderStatus {
    CREATED,
    PAYMENT_PENDING,
    PAYMENT_COMPLETED,
    PAYMENT_FAILED,
    PROCESSING,
    COMPLETED,
    CANCELLED
}