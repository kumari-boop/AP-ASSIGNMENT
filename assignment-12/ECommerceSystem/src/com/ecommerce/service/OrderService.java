package com.ecommerce.service;

import com.ecommerce.model.*;
import com.ecommerce.notification.NotificationService;
import com.ecommerce.order.IOrderProcessor;
import com.ecommerce.payment.IPaymentMethod;
import com.ecommerce.storage.IOrderRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;
    private final NotificationService notificationService;
    private final Map<OrderType, IOrderProcessor> orderProcessors;

    public OrderService(IOrderRepository orderRepository,
                        NotificationService notificationService,
                        Map<OrderType, IOrderProcessor> orderProcessors) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
        this.orderProcessors = orderProcessors;
    }

    @Override
    public OrderResult createOrder(OrderRequest request) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);

        Order order = new Order(orderId, request.getCustomerId(),
                request.getItems(), request.getOrderType());

        IOrderProcessor processor = orderProcessors.get(request.getOrderType());
        if (processor == null) {
            return new OrderResult(order, null, "No processor found", false);
        }

        double finalAmount = processor.calculateFinalAmount(order);
        System.out.println("Final amount for order " + orderId + ": $" + finalAmount);

        processor.processOrder(order);
        orderRepository.save(order);

        return new OrderResult(order, null, "Order created successfully", true);
    }

    @Override
    public OrderResult processOrderPayment(String orderId, IPaymentMethod paymentMethod,
                                           PaymentDetails paymentDetails) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            return new OrderResult(null, null, "Order not found", false);
        }

        Order order = orderOpt.get();
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        orderRepository.update(order);

        PaymentResult paymentResult = paymentMethod.processPayment(
                order.getTotalAmount(), paymentDetails);

        if (paymentResult.isSuccess()) {
            order.setStatus(OrderStatus.PAYMENT_COMPLETED);
            orderRepository.update(order);
            sendNotifications(order);
            return new OrderResult(order, paymentResult, "Payment successful", true);
        } else {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            orderRepository.update(order);
            return new OrderResult(order, paymentResult, "Payment failed", false);
        }
    }

    private void sendNotifications(Order order) {
        String customerEmail = "customer@example.com";
        String customerPhone = "+1234567890";
        String deviceToken = "device_token_123";

        notificationService.sendOrderConfirmation(order, customerEmail,
                customerPhone, deviceToken);
    }
}