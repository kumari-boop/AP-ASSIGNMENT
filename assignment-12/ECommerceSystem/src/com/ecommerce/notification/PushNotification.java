package com.ecommerce.notification;

public class PushNotification implements INotificationSender {

    @Override
    public void sendNotification(String recipient, String subject, String message) {
        System.out.println("=== PUSH NOTIFICATION ===");
        System.out.println("Device: " + recipient);
        System.out.println("Title: " + subject);
        System.out.println("Body: " + message);
        System.out.println("===========================");
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}