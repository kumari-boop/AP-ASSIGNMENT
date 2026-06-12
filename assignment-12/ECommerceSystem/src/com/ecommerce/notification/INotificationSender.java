package com.ecommerce.notification;

public interface INotificationSender {
    void sendNotification(String recipient, String subject, String message);
    boolean isAvailable();
}