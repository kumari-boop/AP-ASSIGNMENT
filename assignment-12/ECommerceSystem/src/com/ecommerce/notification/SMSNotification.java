package com.ecommerce.notification;

public class SMSNotification implements INotificationSender {

    @Override
    public void sendNotification(String recipient, String subject, String message) {
        String smsMessage = subject + ": " + message;
        System.out.println("=== SMS NOTIFICATION ===");
        System.out.println("To: " + recipient);
        System.out.println("Message: " + smsMessage);
        System.out.println("==========================");
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}