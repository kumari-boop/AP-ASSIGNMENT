package com.ecommerce.notification;

public class EmailNotification implements INotificationSender {

    @Override
    public void sendNotification(String recipient, String subject, String message) {
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + recipient);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + message);
        System.out.println("===========================");
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}